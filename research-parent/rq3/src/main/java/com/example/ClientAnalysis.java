package com.example;

import com.example.breakingchange.BreakingChange;
import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;
import com.example.depanalyzer.analyzer.dependencycollection.Request;
import com.example.parsing.Parser;
import com.example.parsing.SymbolChecker;
import com.example.parsing.Visitor;
import com.example.pom.PomException;
import com.example.pom.PomFile;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.ast.CompilationUnit;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;

/** Finds the uses of breaking changes in a submodule */
public class ClientAnalysis {
  private static final RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
  private static final RepositorySystemSession session = RepositorySystemFactory.newSession(system);
  private static final Logger LOGGER = Logger.getLogger(ClientAnalysis.class.getName());

  private final SubModule subModule;
  private final List<BreakingChange> directBreakingChanges;
  private final List<BreakingChange> transitiveBreakingChanges;

  public ClientAnalysis(
      SubModule submodule,
      List<BreakingChange> directBreakingChanges,
      List<BreakingChange> transitiveBreakingChanges) {

    this.subModule = submodule;
    this.directBreakingChanges =
        directBreakingChanges != null ? directBreakingChanges : java.util.Collections.emptyList();
    this.transitiveBreakingChanges =
        transitiveBreakingChanges != null ? transitiveBreakingChanges : java.util.Collections.emptyList();
  }

  public List<BreakingChangeUse> execute() throws IOException, PomException {
    // 1) Resolve build classpath for this submodule (via POM)
    PomFile pom = new PomFile(subModule.getDir());
    List<Dependency> deps = pom.getDependencies();
    Set<Artifact> artifacts = new HashSet<>();

    deps.forEach(
        dep -> {
          Request request = new Request(system, session);
          artifacts.addAll(request.resolve(dep));
        });
    LanguageLevel javaVersion = pom.getJavaVersion();

    // 2) Parse source files and collect symbol usages
    Parser parser = new Parser(subModule.getDir(), artifacts, javaVersion);
    SymbolChecker symbolChecker = new SymbolChecker();
    Visitor visitor = new Visitor(symbolChecker);

    for (Path javaFile : parser.getJavaFiles()) {
      ParseResult<CompilationUnit> result = parser.parse(javaFile);
      result.getResult().ifPresent(cu -> visitor.visit(cu, symbolChecker));
    }

    // 3) Build class owner index: FQN/simpleName -> "groupId:artifactId"
    Map<String, String> classOwnerIndex = buildClassOwnerIndex(artifacts);

    // 4) Aggregate used symbols per dependency (g:a)
    Set<String> usedClasses = symbolChecker.getUsedClasses();
    Set<String> usedMethods = symbolChecker.getUsedMethods();
    Set<String> usedFields = symbolChecker.getUsedFields();

    // Map class symbol (FQN) to owning lib (filter out unresolved to avoid NPE in toMap)
    Map<String, String> classToGA =
        usedClasses.stream()
            .map(cls -> new java.util.AbstractMap.SimpleEntry<>(cls, resolveOwner(classOwnerIndex, cls)))
            .filter(e -> e.getValue() != null)
            .collect(
                java.util.stream.Collectors.toMap(
                    java.util.Map.Entry::getKey,
                    java.util.Map.Entry::getValue,
                    (a, b) -> a,
                    java.util.LinkedHashMap::new));
    // Filter unresolved
    classToGA.values().removeIf(Objects::isNull);

    // Map method/field symbols to GA via their class part
    Map<String, String> methodToGA = mapMemberSymbolsToGA(usedMethods, classOwnerIndex);
    Map<String, String> fieldToGA = mapMemberSymbolsToGA(usedFields, classOwnerIndex);

    // Unique_Symbols_Used per GA = distinct classes + methods + fields used from that GA
    Map<String, Set<String>> uniqueUsedPerGA = new HashMap<>();
    classToGA.forEach((cls, ga) -> uniqueUsedPerGA.computeIfAbsent(ga, k -> new HashSet<>()).add(cls));
    methodToGA.forEach(
        (m, ga) -> uniqueUsedPerGA.computeIfAbsent(ga, k -> new HashSet<>()).add(m));
    fieldToGA.forEach((f, ga) -> uniqueUsedPerGA.computeIfAbsent(ga, k -> new HashSet<>()).add(f));

    // 5) For each breaking change, determine if the API element is used
    List<BreakingChange> allChanges = new ArrayList<>();
    allChanges.addAll(directBreakingChanges);
    allChanges.addAll(transitiveBreakingChanges);

    // Precompute affected symbols per GA
    Map<String, Set<String>> affectedPerGA = new HashMap<>();

    List<BreakingChangeUse> out = new ArrayList<>();
    for (BreakingChange bc : allChanges) {
      String ga = getGA(bc.getOldDependency()); // library being upgraded
      String oldVersion = bc.getOldDependency() != null ? bc.getOldDependency().getArtifact().getVersion() : "";
      String newVersion = bc.getNewDependency() != null ? bc.getNewDependency().getArtifact().getVersion() : "";

      String symbolKey = toSymbolKey(bc);

      boolean used = false;
      String ct = bc.getChangeType();
      if ("CLASS_CHANGE".equals(ct)) {
        used = usedClasses.contains(bc.getClassName());
      } else if ("METHOD_CHANGE".equals(ct)) {
        used = usedMethods.contains(symbolKey);
      } else if ("FIELD_CHANGE".equals(ct)) {
        used = usedFields.contains(symbolKey);
      } else if ("CONSTRUCTOR_CHANGE".equals(ct)) {
        boolean byClass = usedClasses.contains(bc.getClassName());
        boolean byCtor = usedMethods.contains(symbolKey);
        used = byClass || byCtor;
      }

      if (used) {
        affectedPerGA.computeIfAbsent(ga, k -> new HashSet<>()).add(symbolKey);
      }

      int uniqueSymbolsUsed =
          uniqueUsedPerGA.getOrDefault(ga, java.util.Collections.<String>emptySet()).size();
      int affectedSymbols =
          affectedPerGA.getOrDefault(ga, java.util.Collections.<String>emptySet()).size();

      out.add(
          new BreakingChangeUse(
              bc,
              used,
              uniqueSymbolsUsed,
              affectedSymbols,
              ga,
              oldVersion,
              newVersion));
    }

    return out;
  }

  private static String getGA(Dependency dep) {
    if (dep == null || dep.getArtifact() == null) return "";
    return dep.getArtifact().getGroupId() + ":" + dep.getArtifact().getArtifactId();
  }

  private static String toSymbolKey(BreakingChange bc) {
    String cls = bc.getClassName();
    String mem = bc.getMemberName();
    return (mem == null || mem.trim().isEmpty()) ? cls : (cls + "#" + mem);
  }

  private static Map<String, String> mapMemberSymbolsToGA(
      Set<String> memberSymbols, Map<String, String> classOwnerIndex) {
    Map<String, String> out = new HashMap<>();
    for (String sym : memberSymbols) {
      int hash = sym.lastIndexOf('#');
      if (hash < 0) continue;
      String cls = sym.substring(0, hash);
      String ga = resolveOwner(classOwnerIndex, cls);
      if (ga != null) out.put(sym, ga);
    }
    return out;
  }

  private static String resolveOwner(Map<String, String> index, String classFqnOrSimple) {
    // Prefer FQN mapping
    String ga = index.get(classFqnOrSimple);
    if (ga != null) return ga;
    // Fallback to simple-name mapping if unique
    int lastDot = classFqnOrSimple.lastIndexOf('.');
    String simple = lastDot >= 0 ? classFqnOrSimple.substring(lastDot + 1) : classFqnOrSimple;
    String simpleKey = "*:" + simple; // special key for simple-name index
    return index.get(simpleKey);
  }

  private static Map<String, String> buildClassOwnerIndex(Set<Artifact> artifacts) {
    Map<String, String> fqnIndex = new HashMap<>();
    Map<String, Set<String>> simpleToGAs = new HashMap<>();

    for (Artifact a : artifacts) {
      if (a.getFile() == null) continue;
      String ga = a.getGroupId() + ":" + a.getArtifactId();
      try (JarFile jar = new JarFile(a.getFile())) {
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
          JarEntry e = entries.nextElement();
          if (e.isDirectory()) continue;
          String name = e.getName();
          if (!name.endsWith(".class") || name.contains("$")) continue; // skip inner/anon classes
          String fqn = name.substring(0, name.length() - 6).replace('/', '.');

          fqnIndex.putIfAbsent(fqn, ga);
          String simple = fqn.substring(fqn.lastIndexOf('.') + 1);
          simpleToGAs.computeIfAbsent(simple, k -> new HashSet<>()).add(ga);
        }
      } catch (IOException ignored) {
      }
    }

    // Build a merged index that contains FQN keys and unique simple-name keys ("*:Simple")
    Map<String, String> merged = new HashMap<>(fqnIndex);
    for (Map.Entry<String, Set<String>> e : simpleToGAs.entrySet()) {
      if (e.getValue().size() == 1) {
        merged.put("*:" + e.getKey(), e.getValue().iterator().next());
      }
    }
    return merged;
    }
}
