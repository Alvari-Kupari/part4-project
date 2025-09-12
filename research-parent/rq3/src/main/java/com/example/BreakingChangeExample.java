package com.example;

import com.example.breakingchange.BreakingChange;
import com.example.breakingchange.BreakingChangeAnalyzer;
import com.example.breakingchange.BreakingChangeAnalysisException;
import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;
import java.util.List;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;

/**
 * Example demonstrating how to use the BreakingChangeAnalyzer to detect breaking changes between
 * two versions of a dependency.
 */
public class BreakingChangeExample {

  public static void main(String[] args) {
    try {
      // Set up Aether repository system
      // PERFORMANCE OPTIMIZATION: Use shared repository system instances
      RepositorySystem system = RepositorySystemFactory.getSharedRepositorySystem();
      RepositorySystemSession session = RepositorySystemFactory.getSharedSession();

      // Create analyzer
      BreakingChangeAnalyzer analyzer = new BreakingChangeAnalyzer(system, session);

      // Example: Compare two versions of JUnit
      Dependency oldVersion =
          new Dependency(new DefaultArtifact("junit", "junit", "jar", "4.12"), "compile");
      Dependency newVersion =
          new Dependency(new DefaultArtifact("junit", "junit", "jar", "4.13"), "compile");

      System.out.println("Analyzing breaking changes between JUnit 4.12 and 4.13...");

      List<BreakingChange> breakingChanges =
          analyzer.analyzeBreakingChanges(oldVersion, newVersion);

      if (breakingChanges.isEmpty()) {
        System.out.println("No breaking changes detected!");
      } else {
        System.out.println("Found " + breakingChanges.size() + " breaking changes:");
        for (BreakingChange change : breakingChanges) {
          System.out.println("  " + change);
        }
      }

    } catch (BreakingChangeAnalysisException e) {
      System.err.println("Breaking change analysis failed: " + e.getMessage());
      e.printStackTrace();
    } catch (Exception e) {
      System.err.println("Unexpected error during analysis: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
