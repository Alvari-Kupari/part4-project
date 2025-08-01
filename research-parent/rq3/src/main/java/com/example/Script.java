package com.example;

import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;
import com.example.depanalyzer.analyzer.dependencycollection.PomReader;
import com.example.dependencyupdate.DependencyUpdate;
import com.example.dependencyupdate.NoDependencyUpdateException;
import com.example.breakingchange.BreakingChangeAnalyzer;
import com.example.breakingchange.BreakingChange;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.version.InvalidVersionSpecificationException;

public class Script {

  public static void main(String[] args) throws IOException, XmlPullParserException {


    Path pomFile = Paths.get("/Users/tonyyin/Desktop/Projects/repos/breaking-good", "pom.xml");

    if (!Files.exists(pomFile)) {
      throw new IOException("Pom file not found at: " + pomFile);
    }

    RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
    RepositorySystemSession session = RepositorySystemFactory.newSession(system);

    PomReader pom = new PomReader(pomFile);
    List<Dependency> deps = pom.getDependencies();

    // Initialize the breaking change analyzer
    BreakingChangeAnalyzer breakingChangeAnalyzer = new BreakingChangeAnalyzer(system, session);

    // Create CSV file for output
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
    String csvFileName = "breaking_changes_analysis_" + timestamp + ".csv";
    Path csvPath = Paths.get(csvFileName);
    
    try (FileWriter csvWriter = new FileWriter(csvPath.toFile())) {
      // Write CSV header
      csvWriter.append("Dependency,Current_Version,Latest_Minor_Version,Class_Name,Member_Name,Change_Type,Description,Binary_Compatible,Source_Compatible\n");
      
      int totalBreakingChanges = 0;

    for (Dependency dep : deps) {
      System.out.println();
      System.out.println("Inspecting dependency: " + dep.toString());

      DependencyUpdate update = new DependencyUpdate(dep, system, session);

      try {
        // PART 1: Get the latest minor version
        Dependency latestMinor = update.getLatestMinorVersion();
        System.out.println("Latest minor version: " + latestMinor.toString());

        // PART 2: Analyze breaking changes between current and latest minor version
        if (!dep.getArtifact().getVersion().equals(latestMinor.getArtifact().getVersion())) {
          System.out.println("Analyzing breaking changes between versions...");
          
          List<BreakingChange> breakingChanges = breakingChangeAnalyzer.analyzeBreakingChanges(dep, latestMinor);
          
          if (breakingChanges.isEmpty()) {
            System.out.println("No breaking changes detected.");
          } else {
            System.out.println("Found " + breakingChanges.size() + " breaking changes:");
            for (BreakingChange change : breakingChanges) {
              System.out.println("  - " + change);
              
              // Write to CSV
              csvWriter.append(escapeCSV(dep.getArtifact().getGroupId() + ":" + dep.getArtifact().getArtifactId()))
                      .append(",")
                      .append(escapeCSV(dep.getArtifact().getVersion()))
                      .append(",")
                      .append(escapeCSV(latestMinor.getArtifact().getVersion()))
                      .append(",")
                      .append(escapeCSV(change.getClassName() != null ? change.getClassName() : ""))
                      .append(",")
                      .append(escapeCSV(change.getMemberName() != null ? change.getMemberName() : ""))
                      .append(",")
                      .append(escapeCSV(change.getChangeType() != null ? change.getChangeType() : ""))
                      .append(",")
                      .append(escapeCSV(change.getDescription() != null ? change.getDescription() : ""))
                      .append(",")
                      .append(String.valueOf(change.isBinaryCompatible()))
                      .append(",")
                      .append(String.valueOf(change.isSourceCompatible()))
                      .append("\n");
              
              totalBreakingChanges++;
            }
          }
        } else {
          System.out.println("Already using the latest minor version.");
        }

      } catch (VersionRangeResolutionException e) {
        e.printStackTrace();
      } catch (InvalidVersionSpecificationException e) {
        e.printStackTrace();
      } catch (NoDependencyUpdateException e) {
        System.out.println("No update available for dependency: " + dep);
      }
    }
    
    System.out.println("\n=== Analysis Complete ===");
    System.out.println("Total breaking changes found: " + totalBreakingChanges);
    System.out.println("CSV report saved to: " + csvPath.toAbsolutePath());
    
    } // End of try-with-resources for CSV writer
  }
  
  /**
   * Escapes CSV values by wrapping in quotes and escaping internal quotes
   */
  private static String escapeCSV(String value) {
    if (value == null) {
      return "";
    }
    // Escape quotes by doubling them and wrap the entire value in quotes
    if (value.contains("\"") || value.contains(",") || value.contains("\n")) {
      return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    return value;
  }
}
