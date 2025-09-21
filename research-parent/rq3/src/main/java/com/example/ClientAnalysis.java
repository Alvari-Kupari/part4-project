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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
  private static final SymbolChecker symbolChecker = new SymbolChecker();
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

  public List<BreakingChangeUse> findClientBreakingChanges() throws IOException, PomException {
    LOGGER.info("Starting client code analysis for submodule: " + subModule.getName());
    LOGGER.info(
        "Looking for usage of "
            + directBreakingChanges.size()
            + " direct and "
            + transitiveBreakingChanges.size()
            + " transitive breaking changes");

    // Initialize the symbol checker with the breaking changes we're looking for
    symbolChecker.setBreakingChanges(directBreakingChanges, transitiveBreakingChanges);

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
    Visitor visitor = new Visitor(symbolChecker);

    // Start with empty map - we'll only add used breaking changes
    Map<String, BreakingChangeUse> allBreakingChanges = new HashMap<>();

    LOGGER.info(
        "Scanning " + parser.getJavaFiles().size() + " Java files for breaking change usage...");
    int fileCount = 0;
    int usageFoundCount = 0;
    
    // Track which breaking changes have been used (for deduplication by line and context)
    Set<String> usedBreakingChangeKeys = new HashSet<>();

    for (Path javaFile : parser.getJavaFiles()) {
      fileCount++;
      if (fileCount % 20 == 0) {
        LOGGER.info(
            "Processed "
                + fileCount
                + "/"
                + parser.getJavaFiles().size()
                + " files, found "
                + usageFoundCount
                + " usages so far...");
      }

      try {
        ParseResult<CompilationUnit> result = parser.parse(javaFile);

        if (result.getResult().isPresent()) {
          visitor.setCurrentFile(javaFile.toString());
          CompilationUnit cu = result.getResult().get();

          // Find usages in this file
          List<BreakingChangeUse> fileUsages = new ArrayList<>();
          visitor.visit(cu, fileUsages);

          // Add each breaking change usage as a separate symbol (include line number for uniqueness)
          for (BreakingChangeUse usage : fileUsages) {
            BreakingChange bc = usage.getBreakingChange();
            // Create unique key based on breaking change + line number + usage type for proper symbol counting
            String key = bc.getClassName() + "." + bc.getMemberName() + "." + bc.getChangeType() + 
                        "@" + usage.getUsageLocation() + ":" + usage.getLineNumber() + 
                        "[" + usage.getUsageType() + "]";
            
            if (!usedBreakingChangeKeys.contains(key)) {
              // Each unique usage is a separate symbol for normalization
              allBreakingChanges.put(key, usage);
              usedBreakingChangeKeys.add(key);
              usageFoundCount++;
            }
          }
        } else {
          LOGGER.warning("Failed to parse file: " + javaFile + ". Errors: " + result.getProblems());
        }
      } catch (Exception e) {
        LOGGER.warning("Error analyzing file " + javaFile + ": " + e.getMessage());
        // Continue with other files
      }
    }

    List<BreakingChangeUse> allResults = new ArrayList<>(allBreakingChanges.values());
    long actualUsedCount = allResults.size(); // All entries are used since we only add used ones

    LOGGER.info("Client analysis complete:");
    LOGGER.info("  - Breaking changes used in client code: " + actualUsedCount);
    LOGGER.info("  - Each usage represents a separate symbol for normalization");
    LOGGER.info("  - Total symbol usages detected: " + usageFoundCount);

    return allResults;
  }
}
