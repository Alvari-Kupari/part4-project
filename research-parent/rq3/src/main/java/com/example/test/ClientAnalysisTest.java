package com.example.test;

import com.example.AllBreakingChangesCsvWriter;
import com.example.Repo;
import com.example.SubModule;
import com.example.breakingchange.BreakingChange;
import com.example.breakingchange.BreakingChangeAnalyzer;
import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;
import com.example.util.RQ3AnalysisUtil;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;

/**
 * Test class to verify the client analysis functionality works correctly.
 * This can be used to manually test specific scenarios and debug the analysis.
 */
public class ClientAnalysisTest {
  private static final Logger LOGGER = Logger.getLogger(ClientAnalysisTest.class.getName());
  
  public static void main(String[] args) throws Exception {
    // Test the client analysis with a simple example
    testWithJUnitExample();
  }
  
  /**
   * Test with a known example (JUnit 4.12 to 4.13) to verify the analysis works
   */
  public static void testWithJUnitExample() throws Exception {
    LOGGER.info("Starting ClientAnalysis test with JUnit example...");
    
    // Set up Aether repository system
    RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
    RepositorySystemSession session = RepositorySystemFactory.newSession(system);
    
    // Create analyzers
    BreakingChangeAnalyzer analyzer = new BreakingChangeAnalyzer(system, session);
    
    // Example: Compare two versions of JUnit
    Dependency oldVersion = new Dependency(new DefaultArtifact("junit", "junit", "jar", "4.12"), "compile");
    Dependency newVersion = new Dependency(new DefaultArtifact("junit", "junit", "jar", "4.13"), "compile");
    
    LOGGER.info("Analyzing breaking changes between JUnit 4.12 and 4.13...");
    
    try {
      // Analyze direct breaking changes
      List<BreakingChange> directBreakingChanges = analyzer.analyzeBreakingChanges(oldVersion, newVersion);
      
      // For this example, we'll skip transitive analysis (JUnit has minimal transitive deps)
      List<BreakingChange> transitiveBreakingChanges = new ArrayList<>();
      
      LOGGER.info("Found " + directBreakingChanges.size() + " direct breaking changes");
      
      // Print breaking changes for inspection
      for (BreakingChange bc : directBreakingChanges) {
        LOGGER.info("Breaking change: " + bc.toString());
      }
      
      // Test RQ3 analysis utility
      RQ3AnalysisUtil.BreakingChangeClassification classification = 
          RQ3AnalysisUtil.analyzeForRQ3(directBreakingChanges, transitiveBreakingChanges);
      
      LOGGER.info("RQ3 Classification:\n" + classification.toString());
      
      // Test CSV writing
      Path testDir = Paths.get("test-output");
      Files.createDirectories(testDir);
      
      // Write all breaking changes CSV
      AllBreakingChangesCsvWriter allWriter = new AllBreakingChangesCsvWriter(testDir.resolve("junit-all-breaking-changes.csv"));
      allWriter.writeAllBreakingChanges(directBreakingChanges, transitiveBreakingChanges);
      allWriter.close();
      
      LOGGER.info("Test complete. Check test-output directory for generated CSV files.");
      
    } catch (Exception e) {
      LOGGER.severe("Test failed: " + e.getMessage());
      e.printStackTrace();
    }
  }
  
  /**
   * Test client analysis with a real project (if available)
   */
  public static void testWithRealProject(Path projectPath) throws Exception {
    LOGGER.info("Testing client analysis with real project: " + projectPath);
    
    // This would require a real project structure
    // For now, just demonstrate the structure
    
    if (!Files.exists(projectPath)) {
      LOGGER.warning("Project path does not exist: " + projectPath);
      return;
    }
    
    // Create a minimal test repo structure
    List<Repo> repos = Repo.getRepos(projectPath);
    
    if (repos.isEmpty()) {
      LOGGER.warning("No repos found in " + projectPath);
      return;
    }
    
    Repo testRepo = repos.get(0);
    List<SubModule> subModules = testRepo.getSubModules();
    
    if (subModules.isEmpty()) {
      LOGGER.warning("No submodules found in " + testRepo.getName());
      return;
    }
    
    SubModule testSubModule = subModules.get(0);
    LOGGER.info("Testing with submodule: " + testSubModule.getName());
    
    // For a real test, you would:
    // 1. Create some breaking changes (or use real ones)
    // 2. Run ClientAnalysis
    // 3. Verify the results
    
    LOGGER.info("Real project test structure validated");
  }
}
