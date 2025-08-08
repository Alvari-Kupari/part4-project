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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;

/** Finds the uses of breaking changes in a submodule */
public class ClientAnalysis {
  private static final RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
  private static final RepositorySystemSession session = RepositorySystemFactory.newSession(system);
  private static final SymbolChecker SymbolChecker = new SymbolChecker();
  private static final Logger LOGGER = Logger.getLogger(ClientAnalysis.class.getName());

  private SubModule subModule;
  private List<BreakingChange> directBreakingChanges;
  private List<BreakingChange> transitiveBreakingChanges;

  public ClientAnalysis(
      SubModule submodule,
      List<BreakingChange> directBreakingChanges,
      List<BreakingChange> transitiveBreakingChanges) {

    this.subModule = submodule;
    this.directBreakingChanges = directBreakingChanges;
    this.transitiveBreakingChanges = transitiveBreakingChanges;
  }

  public List<BreakingChangeUse> execute() throws IOException, PomException {
    PomFile pom = new PomFile(subModule.getDir());
    List<Dependency> deps = pom.getDependencies();
    Set<Artifact> artifacts = new HashSet<>();

    deps.forEach(
        dep -> {
          Request request = new Request(system, session);
          artifacts.addAll(request.resolve(dep));
        });
    LanguageLevel javaVersion = pom.getJavaVersion();

    Parser parser = new Parser(subModule.getDir(), artifacts, javaVersion);
    Visitor visitor = new Visitor(SymbolChecker);

    List<BreakingChangeUse> breakingChangeUses = new ArrayList<>();

    for (Path javaFile : parser.getJavaFiles()) {
      ParseResult<CompilationUnit> result = parser.parse(javaFile);

      // This is the main step
      // result.getResult().ifPresent(cu -> visitor.visit(cu, breakingChangeUses));
    }

    return breakingChangeUses;
  }
}
