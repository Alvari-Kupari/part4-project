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
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.aether.graph.Dependency;

public class Script {
  public static final Path csvFolder =
      Paths.get("/Users/tonyyin/Desktop/Projects/csv");

  private static final Path reposFolder =
      Paths.get("/Users/tonyyin/Desktop/Projects/rq3repos");

  // Cache to prevent duplicate dependency analysis across submodules
  private static final ConcurrentHashMap<String, DependencyAnalysisResult> dependencyCache = 
      new ConcurrentHashMap<>();

  private static final Logger LOGGER = Logger.getLogger(Script.class.getName());

  // Cache result wrapper
  private static class DependencyAnalysisResult {
    final List<BreakingChange> directBreakingChanges;
    final List<BreakingChange> transitiveBreakingChanges;
    
    DependencyAnalysisResult(List<BreakingChange> direct, List<BreakingChange> transitive) {
      this.directBreakingChanges = direct != null ? direct : java.util.Collections.emptyList();
      this.transitiveBreakingChanges = transitive != null ? transitive : java.util.Collections.emptyList();
    }
  }

  // private static final String startRepo = "apache__xmlgraphics-batik";

  public static void main(String[] args) throws IOException {

    if (!Files.isDirectory(reposFolder)) {
      throw new IOException("Repos folder not found at: " + reposFolder);
    }

    Files.createDirectories(csvFolder);

    List<Repo> repos = Repo.getRepos(reposFolder);

    for (Repo repo : repos) {

      // if (repo.getName().equals(startRepo)) {
      //   seen = true;
      // }

      // if (!seen) continue;

      LOGGER.info(
          "\n\n========== STARTING ANALYSIS FOR REPOSITORY: " + repo.getName() + " ==========\n");

      List<SubModule> subModules = repo.getSubModules();

      if (subModules.isEmpty()) {
        LOGGER.severe("No submodules found for repo: " + repo.getName());
        continue;
      }

      inner:
      for (SubModule submodule : subModules) {

        if (submodule.hasTooManyDeps()) {
          LOGGER.warning("Skipping analysis for submodule '" + submodule.getName() + "' due to excessive dependencies.");
          continue;
        }

        if (submodule.hasTooManyLOC()) {
          LOGGER.warning("Skipping analysis for submodule '" + submodule.getName() + "' due to excessive lines of code.");
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

    LOGGER.info("\n\n========== ALL ANALYSIS COMPLETE ==========");
    LOGGER.info("Dependency cache statistics:");
    LOGGER.info("  - Total cached dependencies: " + dependencyCache.size());
    LOGGER.info("Check output folders in: " + csvFolder.toAbsolutePath());
    LOGGER.info("============================================\n");
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
    List<BreakingChange> allDirectBreakingChanges = new java.util.concurrent.CopyOnWriteArrayList<>();
    List<BreakingChange> allTransitiveBreakingChanges = new java.util.concurrent.CopyOnWriteArrayList<>();
    List<BreakingChangeUse> allUsageResults = new java.util.concurrent.CopyOnWriteArrayList<>();

    // Process dependencies in parallel for better performance
    ExecutorService executor = Executors.newFixedThreadPool(Math.min(deps.size(), Runtime.getRuntime().availableProcessors()));
    
    LOGGER.info("Processing " + deps.size() + " dependencies in parallel using " + 
                Math.min(deps.size(), Runtime.getRuntime().availableProcessors()) + " threads");
    
    List<CompletableFuture<Void>> futures = new java.util.ArrayList<>();

    for (Dependency dep : deps) {
      CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
        try {
          processDependency(dep, submodule, failureTracker, usedBcWriter, 
                          allDirectBreakingChanges, allTransitiveBreakingChanges, allUsageResults);
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Error processing dependency " + dep + ": " + e.getMessage(), e);
        }
      }, executor);
      futures.add(future);
    }

    // Wait for all dependencies to complete
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    
    executor.shutdown();
    try {
      if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      executor.shutdownNow();
    }

    // Calculate totals after all parallel processing is complete
    totalDirectBreakingChanges = allDirectBreakingChanges.size();
    totalTransitiveBreakingChanges = allTransitiveBreakingChanges.size();
    totalUsedBreakingChanges = (int) allUsageResults.stream()
        .filter(BreakingChangeUse::isUsedInClient)
        .count();

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

  private static void processDependency(Dependency dep, SubModule submodule, FailureTracker failureTracker,
                                       UsedBreakingChangesCsvWriter usedBcWriter,
                                       List<BreakingChange> allDirectBreakingChanges,
                                       List<BreakingChange> allTransitiveBreakingChanges,
                                       List<BreakingChangeUse> allUsageResults) {
    
    // Create cache key from dependency coordinates
    String cacheKey = dep.getArtifact().getGroupId() + ":" + 
                     dep.getArtifact().getArtifactId() + ":" + 
                     dep.getArtifact().getVersion();
                     
    // Check cache first
    DependencyAnalysisResult cachedResult = dependencyCache.get(cacheKey);
    List<BreakingChange> directBreakingChanges;
    List<BreakingChange> transitiveBreakingChanges;
    
    if (cachedResult != null) {
      LOGGER.info("Using cached analysis for dependency: " + dep);
      directBreakingChanges = cachedResult.directBreakingChanges;
      transitiveBreakingChanges = cachedResult.transitiveBreakingChanges;
    } else {
      // Perform analysis and cache result
      DependencyAnalysis dependencyAnalysis = new DependencyAnalysis(dep, failureTracker);

      try {
        dependencyAnalysis.execute();
      } catch (VersionResolutionException e) {
        LOGGER.log(Level.SEVERE, "Version resolution failed for " + dep, e);
        return;
      } catch (NoDependencyUpdateException e) {
        LOGGER.warning("No update available for dependency: " + dep + ". Skipping.");
        return;
      } catch (RuntimeException e) {
        LOGGER.log(Level.SEVERE, "Analysis failed for dependency: " + dep + " with error: " + e.getMessage(), e);
        return;
      }

      directBreakingChanges = dependencyAnalysis.getDirectBreakingChanges();
      transitiveBreakingChanges = dependencyAnalysis.getTransitiveBreakingChanges();

      // Null-safe fallback to avoid NPEs when analysis fails
      if (directBreakingChanges == null) {
        directBreakingChanges = java.util.Collections.emptyList();
      }
      if (transitiveBreakingChanges == null) {
        transitiveBreakingChanges = java.util.Collections.emptyList();
      }
      
      // Cache the result for future use
      dependencyCache.put(cacheKey, new DependencyAnalysisResult(directBreakingChanges, transitiveBreakingChanges));
      LOGGER.info("Cached analysis for dependency: " + dep);
    }

    LOGGER.info("Found " + directBreakingChanges.size() + " direct and " + 
                transitiveBreakingChanges.size() + " transitive breaking changes for dependency: " + dep);

    // Add to thread-safe collections
    allDirectBreakingChanges.addAll(directBreakingChanges);
    allTransitiveBreakingChanges.addAll(transitiveBreakingChanges);

    // Only proceed with client analysis if we found breaking changes
    if (!directBreakingChanges.isEmpty() || !transitiveBreakingChanges.isEmpty()) {
      try {
        // Run client analysis to see which ones are actually used
        ClientAnalysis clientAnalysis = new ClientAnalysis(submodule, directBreakingChanges, transitiveBreakingChanges);
        List<BreakingChangeUse> allBreakingChangeUses = clientAnalysis.findClientBreakingChanges();

        // Collect usage results for final writing
        allUsageResults.addAll(allBreakingChangeUses);

        // Synchronize CSV writing to avoid conflicts
        synchronized(usedBcWriter) {
          usedBcWriter.writeUsedBreakingChanges(allBreakingChangeUses);
        }

        long usedCount = allBreakingChangeUses.stream().filter(BreakingChangeUse::isUsedInClient).count();
        LOGGER.info("Client analysis complete for dependency " + dep + ". " + usedCount + 
                   " breaking changes used in client code out of " + 
                   (directBreakingChanges.size() + transitiveBreakingChanges.size()) + " total");
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Client analysis failed for dependency " + dep, e);
      }
    } else {
      LOGGER.info("No breaking changes found for dependency " + dep + " - skipping client analysis");
    }
  }
}
