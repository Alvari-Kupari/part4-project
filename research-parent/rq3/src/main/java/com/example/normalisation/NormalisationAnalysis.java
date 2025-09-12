package com.example.normalisation;

import com.example.SubModule;
import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;
import com.example.depanalyzer.analyzer.dependencycollection.Request;
import com.example.parsing.Parser;
import com.example.pom.PomException;
import com.example.pom.PomFile;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.ast.CompilationUnit;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;

public class NormalisationAnalysis {

  // PERFORMANCE OPTIMIZATION: Use shared repository system instances to avoid recreation
  private static final RepositorySystem system = RepositorySystemFactory.getSharedRepositorySystem();
  private static final RepositorySystemSession session = RepositorySystemFactory.getSharedSession();
  private static final Logger LOGGER = Logger.getLogger(NormalisationAnalysis.class.getName());

  private SubModule subModule;
  private SymbolDatabase symbolDatabase;

  public NormalisationAnalysis(SubModule submodule) {
    this.subModule = submodule;
    this.symbolDatabase = new SymbolDatabase();
  }

  public Set<Symbol> getAllDependencySymbolUses() throws IOException, PomException {
    LOGGER.info("Starting client code analysis for submodule: " + subModule.getName());

    PomFile pom = new PomFile(subModule.getDir());
    List<Dependency> deps = pom.getDependencies();
    Set<Artifact> artifacts = new HashSet<>();

    deps.forEach(
        dep -> {
          Request request = new Request(system, session);
          artifacts.addAll(request.resolve(dep));
        });

    String rootPackage = subModule.getClientRootPackage();

    DependencyDatabase dependencyDatabase = new DependencyDatabase(rootPackage, artifacts);
    LanguageLevel javaVersion = pom.getJavaVersion();

    Parser parser = new Parser(subModule.getDir(), artifacts, javaVersion);
    SymbolVisitor visitor = new SymbolVisitor(dependencyDatabase);

    LOGGER.info(
        "Scanning " + parser.getJavaFiles().size() + " Java files for breaking change usage...");
    int fileCount = 0;

    for (Path javaFile : parser.getJavaFiles()) {
      fileCount++;
      if (fileCount % 20 == 0) {
        LOGGER.info("Processed " + fileCount + "/" + parser.getJavaFiles().size() + " files");
      }

      try {
        ParseResult<CompilationUnit> result = parser.parse(javaFile);

        if (result.getResult().isPresent()) {
          visitor.setCurrentFile(javaFile.toString());
          CompilationUnit cu = result.getResult().get();
          visitor.visit(cu, symbolDatabase);

        } else {
          LOGGER.warning("Failed to parse file: " + javaFile + ". Errors: " + result.getProblems());
        }
      } catch (Exception e) {
        LOGGER.warning("Error analyzing file " + javaFile + ": " + e.getMessage());
        // Continue with other files
      }
    }

    LOGGER.info("Normalisation analysis complete:");
    LOGGER.info("  - Total symbols found: " + symbolDatabase.getSize());

    return symbolDatabase.getSymbols();
  }
}
