package com.example;

import com.example.breakingchange.BreakingChange;
import com.example.csv.AllBreakingChangesCsvWriter;
import com.example.csv.ClientSymbolCsvWriter;
import com.example.csv.UsedBreakingChangesCsvWriter;
import com.example.dependencyupdate.NoDependencyUpdateException;
import com.example.dependencyupdate.VersionResolutionException;
import com.example.normalisation.NormalisationAnalysis;
import com.example.normalisation.Symbol;
import com.example.pom.PomException;
import com.example.pom.PomFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
<<<<<<< HEAD
import java.util.ArrayList;
=======
import java.nio.charset.StandardCharsets;
>>>>>>> feat/powershell
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.aether.graph.Dependency;

public class Script {
  public static final Path csvFolder =
      Paths.get("/Users/tonyyin/Desktop/Projects/csv2");

  private static final Path reposFolder =
<<<<<<< HEAD
      Paths.get("/Users/tonyyin/Desktop/Projects/rq3repos");

  private static final Logger LOGGER = Logger.getLogger(Script.class.getName());

  // private static final String startRepo = "escaipe-room-beta-and-final-team-27";
=======
      Paths.get("C:\\Users\\tyin363\\Documents\\repos");

  private static final Path progressFile =
      csvFolder.resolve("progress.txt");

  private static final Logger LOGGER = Logger.getLogger(Script.class.getName());

  // Remove the hardcoded startRepo - we'll determine it dynamically
  // private static final String startRepo = "castlemock__castlemock";
>>>>>>> feat/powershell

  public static void main(String[] args) throws IOException {

    if (!Files.isDirectory(reposFolder)) {
      throw new IOException("Repos folder not found at: " + reposFolder);
    }

    Files.createDirectories(csvFolder);

    List<Repo> repos = Repo.getRepos(reposFolder);

    // Determine where to start based on progress file
    String lastProcessedRepo = getLastProcessedRepo();
    boolean seen = (lastProcessedRepo == null); // If no progress file, start from beginning
    
    if (lastProcessedRepo != null) {
      LOGGER.info("Resuming from after repository: " + lastProcessedRepo);
    } else {
      LOGGER.info("Starting analysis from the beginning");
    }

    for (Repo repo : repos) {

<<<<<<< HEAD
      // if (repo.getName().equals(startRepo)) {
      //   seen = true;
      // }

      // if (!seen) continue;

=======
      // Skip until we reach the repo after the last processed one
      if (!seen) {
        if (repo.getName().equals(lastProcessedRepo)) {
          seen = true;
        }
        continue;
      }

>>>>>>> feat/powershell
      LOGGER.info(
          "\n\n========== STARTING ANALYSIS FOR REPOSITORY: " + repo.getName() + " ==========\n");

      // Update progress file at the start of each repo
      updateProgressFile(repo.getName());

      List<SubModule> subModules = repo.getSubModules();

      if (subModules.isEmpty()) {
        LOGGER.severe("No submodules found for repo: " + repo.getName());
        continue;
      }

      inner:
      for (SubModule submodule : subModules) {

        if (submodule.hasTooManyDeps()) {
          LOGGER.warning(
              "Skipping analysis for submodule '"
                  + submodule.getName()
                  + "' due to excessive dependencies.");
          continue;
        }

        if (submodule.hasTooManyLOC()) {
          LOGGER.warning(
              "Skipping analysis for submodule '"
                  + submodule.getName()
                  + "' due to excessive lines of code.");
          continue;
        }

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

    // Clear progress file when completely done
    clearProgressFile();
    
    LOGGER.info("\n\n========== ALL ANALYSIS COMPLETE ==========");
    LOGGER.info("Check output folders in: " + csvFolder.toAbsolutePath());
    LOGGER.info("============================================\n");
  }

  private static String getLastProcessedRepo() {
    try {
      if (Files.exists(progressFile)) {
        List<String> lines = Files.readAllLines(progressFile, StandardCharsets.UTF_8);
        if (!lines.isEmpty()) {
          return lines.get(0).trim();
        }
      }
    } catch (IOException e) {
      LOGGER.warning("Could not read progress file: " + e.getMessage());
    }
    return null;
  }

  private static void updateProgressFile(String repoName) {
    try {
      Files.write(progressFile, repoName.getBytes(StandardCharsets.UTF_8));
      LOGGER.fine("Updated progress file with: " + repoName);
    } catch (IOException e) {
      LOGGER.warning("Could not update progress file: " + e.getMessage());
    }
  }

  private static void clearProgressFile() {
    try {
      if (Files.exists(progressFile)) {
        Files.delete(progressFile);
        LOGGER.info("Cleared progress file - analysis complete");
      }
    } catch (IOException e) {
      LOGGER.warning("Could not clear progress file: " + e.getMessage());
    }
  }

  private static void performSubmoduleAnalysis(SubModule submodule)
      throws IOException, PomException {

    // Create dedicated output folder for this submodule
    String submoduleFolderName = submodule.getRepo().getName() + "_" + submodule.getName();
    Path submoduleOutputFolder = csvFolder.resolve(submoduleFolderName);
    Files.createDirectories(submoduleOutputFolder);

    // Initialize failure tracker for this specific submodule
    FailureTracker failureTracker = new FailureTracker(submoduleOutputFolder);

    PomFile pom = new PomFile(submodule.getDir());
    List<Dependency> deps = pom.getDependencies();

    // Initialize CSV writers for this submodule in its dedicated folder
    AllBreakingChangesCsvWriter allBcWriter =
        new AllBreakingChangesCsvWriter(submodule, submoduleOutputFolder, "-all-breaking-changes");
    UsedBreakingChangesCsvWriter usedBcWriter =
        new UsedBreakingChangesCsvWriter(
            submodule, submoduleOutputFolder, "-used-breaking-changes");

    ClientSymbolCsvWriter symbolWriter =
        new ClientSymbolCsvWriter(submodule, submoduleOutputFolder, "-client-symbol-uses");

    int totalDirectBreakingChanges = 0;
    int totalTransitiveBreakingChanges = 0;
    int totalUsedBreakingChanges = 0;

    // Collect all breaking changes and usage results for final writing
    List<BreakingChange> allDirectBreakingChanges = new ArrayList<>();
    List<BreakingChange> allTransitiveBreakingChanges = new ArrayList<>();
    List<BreakingChangeUse> allUsageResults = new ArrayList<>();

    // FIRST PASS: Collect all breaking changes from all dependencies
    for (Dependency dep : deps) {

      DependencyAnalysis dependencyAnalysis = new DependencyAnalysis(dep, failureTracker);
      Pair<List<BreakingChange>, List<BreakingChange>> result;
      try {
        result = dependencyAnalysis.execute();
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

      List<BreakingChange> directBreakingChanges = result.getFirst();
      List<BreakingChange> transitiveBreakingChanges = result.getSecond();

      LOGGER.info(
          "Found "
              + directBreakingChanges.size()
              + " direct and "
              + transitiveBreakingChanges.size()
              + " transitive breaking changes for dependency: "
              + dep);

      // Update totals
      totalDirectBreakingChanges += directBreakingChanges.size();
      totalTransitiveBreakingChanges += transitiveBreakingChanges.size();

      // Collect breaking changes for final writing
      allDirectBreakingChanges.addAll(directBreakingChanges);
      allTransitiveBreakingChanges.addAll(transitiveBreakingChanges);
    }

    // SECOND PASS: Now run client analysis with ALL breaking changes
    if (!allDirectBreakingChanges.isEmpty() || !allTransitiveBreakingChanges.isEmpty()) {
      LOGGER.info(
          "Running client analysis with ALL breaking changes: "
              + allDirectBreakingChanges.size()
              + " direct + "
              + allTransitiveBreakingChanges.size()
              + " transitive");

      ClientAnalysis clientAnalysis =
          new ClientAnalysis(submodule, allDirectBreakingChanges, allTransitiveBreakingChanges);

      List<BreakingChangeUse> allBreakingChangeUses = clientAnalysis.findClientBreakingChanges();

      // Collect usage results for final writing
      allUsageResults.addAll(allBreakingChangeUses);

      // Append to CSV with only breaking changes that are used in client code
      usedBcWriter.writeUsedBreakingChanges(allBreakingChangeUses);

      // Count for logging
      long usedCount =
          allBreakingChangeUses.stream().filter(BreakingChangeUse::isUsedInClient).count();
      totalUsedBreakingChanges += (int) usedCount;

      LOGGER.info(
          "Client analysis complete. "
              + usedCount
              + " breaking changes used in client code out of "
              + (allDirectBreakingChanges.size() + allTransitiveBreakingChanges.size())
              + " total");
    } else {
      LOGGER.info("No breaking changes found - skipping client analysis");
    }

    // find all the symbols for later normalisation
    NormalisationAnalysis normalisationAnalysis = new NormalisationAnalysis(submodule);

    Set<Symbol> clientSymbols = normalisationAnalysis.getAllDependencySymbolUses();

    symbolWriter.writeAllSymbols(clientSymbols);

    // Write all breaking changes with correct usage information
    allBcWriter.writeAllBreakingChangesWithUsage(
        allDirectBreakingChanges, allTransitiveBreakingChanges, allUsageResults);

    // Close the CSV writers and failure tracker after processing all dependencies
    allBcWriter.close();
    usedBcWriter.close();
    failureTracker.close();
    symbolWriter.close();

    LOGGER.info("=== SUBMODULE SUMMARY for '" + submodule.getName() + "' ===");
    LOGGER.info("Output folder: " + submoduleOutputFolder.toAbsolutePath());
    LOGGER.info("Total direct breaking changes: " + totalDirectBreakingChanges);
    LOGGER.info("Total transitive breaking changes: " + totalTransitiveBreakingChanges);
    LOGGER.info("Total used breaking changes: " + totalUsedBreakingChanges);
    LOGGER.info(
        "Total breaking changes: " + (totalDirectBreakingChanges + totalTransitiveBreakingChanges));
    LOGGER.info("Comparisons attempted: " + failureTracker.getTotalComparisons());
    LOGGER.info("Failed comparisons: " + failureTracker.getFailedComparisons());
    LOGGER.info("Success rate: " + String.format("%.2f%%", failureTracker.getSuccessRate()));
  }
}