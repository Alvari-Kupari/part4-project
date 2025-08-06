package com.example.stage1;

import com.example.Repo;
import com.example.SubModule;
import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;
import com.example.pom.PomException;
import com.example.pom.PomFile;
import com.example.stage1.breakingchange.BreakingChange;
import com.example.stage1.breakingchange.BreakingChangeAnalyzer;
import com.example.stage1.breakingchange.TransitiveDependencyAnalyzer;
import com.example.stage1.dependencyupdate.DependencyUpdate;
import com.example.stage1.dependencyupdate.NoDependencyUpdateException;
import com.example.stage1.dependencyupdate.VersionResolutionException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;

public class Script1 {
  private static final Path csvFolder =
      Paths.get(
          "C:\\Users\\Tony\\Desktop\\csv");

  private static final Path reposFolder =
      Paths.get("C:\\Users\\Tony\\Desktop\\repos");
  private static final RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
  private static final RepositorySystemSession session = RepositorySystemFactory.newSession(system);
  private static final BreakingChangeAnalyzer breakingChangeAnalyzer =
      new BreakingChangeAnalyzer(system, session);
  private static final TransitiveDependencyAnalyzer transitiveDependencyAnalyzer =
      new TransitiveDependencyAnalyzer(system, session, breakingChangeAnalyzer);
  private static final Logger LOGGER = Logger.getLogger(Script1.class.getName());
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

    LOGGER.info(
        "\n=== Starting Analysis  for submodule '"
            + submodule.getName()
            + "'\n at "
            + submodule.getDir().toAbsolutePath());

    CsvWriter csv = CsvWriter.createCsv(submodule, csvFolder);
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
      Dependency originalDep, List<Dependency> updates, CsvWriter csv) throws IOException {

    Dependency current = originalDep;
    for (Dependency nextVersion : updates) {
      LOGGER.info(
          "Analyzing breaking changes between dependencies " + current + " and " + nextVersion);

      try {
        // 1. Analyze direct dependency breaking changes
        List<BreakingChange> directBreakingChanges =
            breakingChangeAnalyzer.analyzeBreakingChanges(current, nextVersion);

        // 2. Find transitive dependency version changes
        List<TransitiveDependencyAnalyzer.TransitiveDependencyChange> transitiveChanges =
            transitiveDependencyAnalyzer.findTransitiveDependencyChanges(current, nextVersion);

        // 3. Analyze breaking changes in each transitive dependency update
        List<BreakingChange> transitiveBreakingChanges = new ArrayList<>();
        for (TransitiveDependencyAnalyzer.TransitiveDependencyChange transitiveChange : transitiveChanges) {
          LOGGER.info("Analyzing transitive breaking changes for: " + transitiveChange);
          List<BreakingChange> changes = 
              transitiveDependencyAnalyzer.analyzeTransitiveBreakingChanges(transitiveChange);
          transitiveBreakingChanges.addAll(changes);
        }

        // 4. Log and write results
        int totalBreakingChanges = directBreakingChanges.size() + transitiveBreakingChanges.size();
        
        if (totalBreakingChanges == 0) {
          LOGGER.info("No breaking changes detected (direct or transitive).");
          failureTracker.recordSuccess();
        } else {
          LOGGER.info(String.format("Found %d total breaking changes (%d direct, %d transitive):",
              totalBreakingChanges, directBreakingChanges.size(), transitiveBreakingChanges.size()));

          // Write direct dependency breaking changes
          for (BreakingChange change : directBreakingChanges) {
            csv.writeBreakingChange(current, nextVersion, change);
          }

          // Write transitive dependency breaking changes
          for (BreakingChange change : transitiveBreakingChanges) {
            csv.writeBreakingChange(current, nextVersion, change);
          }
          
          failureTracker.recordSuccess();
        }
      } catch (Exception e) {
        LOGGER.log(Level.WARNING, 
            "Failed to analyze breaking changes between " + current + " and " + nextVersion + ": " + e.getMessage(), e);
        failureTracker.recordFailure(current, nextVersion, e);
      }

      current = nextVersion;
    }
  }
}
