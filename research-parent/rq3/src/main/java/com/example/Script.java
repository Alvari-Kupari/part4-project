package com.example;

import com.example.breakingchange.BreakingChange;
import com.example.breakingchange.BreakingChangeAnalyzer;
import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;
import com.example.dependencyupdate.DependencyUpdate;
import com.example.dependencyupdate.NoDependencyUpdateException;
import com.example.dependencyupdate.VersionResolutionException;
import com.example.pom.PomException;
import com.example.pom.PomFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;

public class Script {
  private static final Path csvFolder =
      Paths.get(
          "C:\\Users\\Alvari\\Documents\\UNI\\softeng_700\\part4-project\\r"
              + "esearch-parent\\rq3\\data");

  private static final Path reposFolder =
      Paths.get("C:\\Users\\Alvari\\Documents\\UNI\\archive\\SOFTENG_206\\repos");
  private static final RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
  private static final RepositorySystemSession session = RepositorySystemFactory.newSession(system);
  private static final BreakingChangeAnalyzer breakingChangeAnalyzer =
      new BreakingChangeAnalyzer(system, session);
  private static final Logger LOGGER = Logger.getLogger(Script.class.getName());

  public static void main(String[] args) throws IOException {

    if (!Files.isDirectory(reposFolder)) {
      throw new IOException("Repos folder not found at: " + reposFolder);
    }

    Files.createDirectories(csvFolder);

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

    Csv csv = Csv.createCsv(submodule, csvFolder);
    PomFile pom = new PomFile(submodule.getDir());
    List<Dependency> deps = pom.getDependencies();

    for (Dependency dep : deps) {

      DependencyUpdate update = new DependencyUpdate(dep, system, session);

      try {
        List<Dependency> updates = update.getMinorUpdates();

        analyzeBreakingChanges(dep, updates, csv);
      } catch (VersionResolutionException e) {
        LOGGER.log(Level.SEVERE, "Version resolution failed", e);

      } catch (NoDependencyUpdateException e) {
        LOGGER.warning(
            "No update available for dependency: " + dep + ". Continuing to the next dependency");
      }
    }

    LOGGER.info("\n=== Analysis Complete for submodule '" + submodule.getName() + "' ===\n");
  }

  private static void analyzeBreakingChanges(
      Dependency originalDep, List<Dependency> updates, Csv csv) throws IOException {

    Dependency current = originalDep;
    for (Dependency nextVersion : updates) {
      LOGGER.info(
          "Analyzing breaking changes between dependencies " + current + " and " + nextVersion);

      List<BreakingChange> breakingChanges =
          breakingChangeAnalyzer.analyzeBreakingChanges(current, nextVersion);

      if (breakingChanges.isEmpty()) {
        LOGGER.info("No breaking changes detected.");
        continue;
      }

      LOGGER.info("Found " + breakingChanges.size() + " breaking changes:");

      for (BreakingChange change : breakingChanges) {
        csv.writeBreakingChange(current, nextVersion, change);
      }

      current = nextVersion;
    }
  }
}
