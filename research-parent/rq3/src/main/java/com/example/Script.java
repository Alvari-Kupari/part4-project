package com.example;

import com.example.breakingchange.BreakingChange;
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
import org.eclipse.aether.graph.Dependency;

public class Script {
  public static final Path csvFolder =
      Paths.get("/Users/tonyyin/Desktop/Projects/csv");

  private static final Path reposFolder =
      Paths.get("/Users/tonyyin/Desktop/Projects/repo");

  private static final Logger LOGGER = Logger.getLogger(Script.class.getName());
  private static FailureTracker failureTracker;

  public static void main(String[] args) throws IOException {

    if (!Files.isDirectory(reposFolder)) {
      throw new IOException("Repos folder not found at: " + reposFolder);
    }

    Files.createDirectories(csvFolder);

    // Initialize failure tracker
    failureTracker = new FailureTracker(csvFolder);

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
        LOGGER.info(
            "\n=== Starting Analysis  for submodule '"
                + submodule.getName()
                + "'\n at "
                + submodule.getDir().toAbsolutePath());
        try {

          performSubmoduleAnalysis(submodule);
          LOGGER.info("\n=== Analysis Complete for submodule '" + submodule.getName() + "' ===\n");
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

    // Print final statistics and close failure tracker
    LOGGER.info("\n\n========== FINAL ANALYSIS STATISTICS ==========");
    LOGGER.info("Total comparisons attempted: " + failureTracker.getTotalComparisons());
    LOGGER.info("Failed comparisons: " + failureTracker.getFailedComparisons());
    LOGGER.info("Success rate: " + String.format("%.2f%%", failureTracker.getSuccessRate()));
    LOGGER.info("See detailed failure log in the CSV folder: " + csvFolder.toAbsolutePath());
    LOGGER.info("================================================\n");

    failureTracker.close();
  }

  private static void performSubmoduleAnalysis(SubModule submodule)
      throws IOException, PomException {

    PomFile pom = new PomFile(submodule.getDir());
    List<Dependency> deps = pom.getDependencies();

    for (Dependency dep : deps) {

      DependencyAnalysis dependencyAnalysis = new DependencyAnalysis(dep, failureTracker);

      try {
        dependencyAnalysis.execute();
      } catch (VersionResolutionException e) {
        LOGGER.log(Level.SEVERE, "Version resolution failed", e);
        continue;

      } catch (NoDependencyUpdateException e) {
        LOGGER.warning(
            "No update available for dependency: " + dep + ". Continuing to the next dependency");
        continue;
      } catch (RuntimeException e) {
        // Catch any analyzer-level exceptions so the script can continue
        LOGGER.log(
            Level.SEVERE,
            "Analysis failed for dependency: " + dep + " with error: " + e.getMessage(),
            e);
        // failureTracker is already recording inside DependencyAnalysis, continue
        continue;
      }

      List<BreakingChange> directBreakingChanges = dependencyAnalysis.getDirectBreakingChanges();
      List<BreakingChange> transitiveBreakingChanges =
          dependencyAnalysis.getTransitiveBreakingChanges();

      ClientAnalysis clientAnalysis =
          new ClientAnalysis(submodule, directBreakingChanges, transitiveBreakingChanges);

      List<BreakingChangeUse> breakingChangeUses = clientAnalysis.execute();

      CsvWriter csv = new CsvWriter(submodule, csvFolder);

      csv.writeResults(breakingChangeUses);
    }
  }
}
