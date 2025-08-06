package com.example.stage2;

import com.example.Repo;
import com.example.SubModule;
import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;
import com.example.depanalyzer.analyzer.dependencycollection.Request;
import com.example.pom.PomException;
import com.example.pom.PomFile;
import com.example.stage2.parsing.Parser;
import com.example.stage2.parsing.Visitor;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.ast.CompilationUnit;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;

public class Script2 {
  private static final Path csvFolder =
      Paths.get(
          "C:\\Users\\Alvari\\Documents\\UNI\\softeng_700\\part4-project\\r"
              + "esearch-parent\\rq3\\data");

  private static final Path reposFolder =
      Paths.get("C:\\Users\\Alvari\\Documents\\UNI\\archive\\SOFTENG_206\\repos");
  private static final RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
  private static final RepositorySystemSession session = RepositorySystemFactory.newSession(system);
  private static final SymbolChecker SymbolChecker = new SymbolChecker();
  private static final Logger LOGGER = Logger.getLogger(Script2.class.getName());

  public static void main(String[] args) throws IOException {

    if (!Files.isDirectory(reposFolder)) {
      throw new IOException("Repos folder not found at: " + reposFolder);
    }

    if (!Files.isDirectory(csvFolder)) {
      throw new IOException("CSV folder not found at: " + csvFolder);
    }

    List<Repo> repos = Repo.getRepos(reposFolder);

    for (Repo repo : repos) {
      LOGGER.info(
          "\n\n========== STARTING ANALYSIS FOR REPOSITORY: " + repo.getName() + " ==========\n");

      List<SubModule> subModules = repo.getSubModules();

      if (subModules.isEmpty()) {
        throw new RuntimeException("No submodules found for repo: " + repo.getName());
      }

      inner:
      for (SubModule submodule : subModules) {
        try {
          performSubmoduleAnalysis(submodule);
        } catch (PomException e) {
          LOGGER.log(
              Level.SEVERE,
              "Failed to parse POM for submodule '"
                  + submodule.getName()
                  + "' at "
                  + submodule.getDir()
                  + ". Skipping this submodule.",
              e);
          continue inner;
        }
      }
    }
  }

  private static void performSubmoduleAnalysis(SubModule submodule)
      throws IOException, PomException {

    LOGGER.info(
        "\n=== Starting Analysis  for submodule '"
            + submodule.getName()
            + "'\n at "
            + submodule.getDir().toAbsolutePath());

    CsvReader csvReader = CsvReader.getCsv(submodule, csvFolder);
    PomFile pom = new PomFile(submodule.getDir());
    List<Dependency> deps = pom.getDependencies();
    Set<Artifact> artifacts = new HashSet<>();

    deps.forEach(
        dep -> {
          Request request = new Request(system, session);
          artifacts.addAll(request.resolve(dep));
        });
    LanguageLevel javaVersion = pom.getJavaVersion();

    Parser parser = new Parser(submodule.getDir(), artifacts, javaVersion);
    Visitor visitor = new Visitor(SymbolChecker);

    for (Path javaFile : parser.getJavaFiles()) {
      ParseResult<CompilationUnit> result = parser.parse(javaFile);

      // This is the main step
      result.getResult().ifPresent(cu -> visitor.visit(cu, null));
    }

    LOGGER.info("\n=== Analysis Complete for submodule '" + submodule.getName() + "' ===\n");
  }
}
