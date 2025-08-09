package com.example;

import com.example.breakingchange.BreakingChange;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.aether.graph.Dependency;

/**
 * CSV writer specifically for outputting all breaking changes found during analysis,
 * regardless of whether they are used in client code. This is useful for manual
 * inspection and testing of the client analysis.
 */
public class AllBreakingChangesCsvWriter {
  private final Path path;
  private final FileWriter writer;

  public AllBreakingChangesCsvWriter(Path path) throws IOException {
    this.path = path;
    this.writer = new FileWriter(path.toFile());
    writeHeader();
  }

  public AllBreakingChangesCsvWriter(SubModule submodule, Path csvFolder, String suffix) throws IOException {
    String csvFileName = submodule.getRepo().getName() + "_" + submodule.getName() + suffix + ".csv";
    this.path = csvFolder.resolve(csvFileName);
    this.writer = new FileWriter(path.toFile());
    writeHeader();
  }

  private void writeHeader() throws IOException {
    writer.append(
        "Library_Name,Old_Version,New_Version,Class_Name,Member_Name,Change_Type,Description," +
        "Binary_Compatible,Source_Compatible,Is_Transitive,Depth,Direct_Parent_Dependency," +
        "Is_Major_Release,Release_Type,Unique_Symbols_Count,Affected_Symbols_Count\n");
    writer.flush();
  }

  public void writeAllBreakingChanges(List<BreakingChange> directBreakingChanges, 
                                     List<BreakingChange> transitiveBreakingChanges) throws IOException {
    // Write direct breaking changes
    for (BreakingChange bc : directBreakingChanges) {
      writeBreakingChange(bc, false); // false = direct
    }
    
    // Write transitive breaking changes
    for (BreakingChange bc : transitiveBreakingChanges) {
      writeBreakingChange(bc, true); // true = transitive
    }
    
    writer.flush();
  }
  
  private void writeBreakingChange(BreakingChange bc, boolean isTransitive) throws IOException {
    // Extract dependency information
    String libraryName = getDependencyName(bc.getOldDependency());
    String oldVersion = getDependencyVersion(bc.getOldDependency());
    String newVersion = getDependencyVersion(bc.getNewDependency());
    
    // Basic breaking change info
    String className = escapeCSV(bc.getClassName());
    String memberName = escapeCSV(bc.getMemberName());
    String changeType = escapeCSV(bc.getChangeType());
    String description = escapeCSV(bc.getDescription());
    boolean binaryCompatible = bc.isBinaryCompatible();
    boolean sourceCompatible = bc.isSourceCompatible();
    int depth = bc.getDepth();
    String directParent = getDependencyName(bc.getDirectParentDependency());
    
    // Version analysis for RQ3
    boolean isMajorRelease = isMajorVersionChange(oldVersion, newVersion);
    String releaseType = determineReleaseType(oldVersion, newVersion);
    
    // Calculate normalization metrics
    int uniqueSymbolsCount = calculateUniqueSymbolsCount(bc);
    int affectedSymbolsCount = calculateAffectedSymbolsCount(bc);
    
    // Write CSV row
    writer.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%d,%s,%s,%s,%d,%d\n",
        libraryName, oldVersion, newVersion, className, memberName, changeType, description,
        binaryCompatible, sourceCompatible, isTransitive, depth, directParent,
        isMajorRelease, releaseType, uniqueSymbolsCount, affectedSymbolsCount));
  }
  
  private String getDependencyName(Dependency dep) {
    if (dep == null) return "";
    return dep.getArtifact().getGroupId() + ":" + dep.getArtifact().getArtifactId();
  }
  
  private String getDependencyVersion(Dependency dep) {
    if (dep == null) return "";
    return dep.getArtifact().getVersion();
  }
  
  private String escapeCSV(String value) {
    if (value == null) return "";
    // Escape quotes and wrap in quotes if contains comma, quote, or newline
    if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
      return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    return value;
  }
  
  private boolean isMajorVersionChange(String oldVersion, String newVersion) {
    if (oldVersion == null || newVersion == null) return false;
    
    try {
      String[] oldParts = oldVersion.split("\\.");
      String[] newParts = newVersion.split("\\.");
      
      if (oldParts.length > 0 && newParts.length > 0) {
        int oldMajor = Integer.parseInt(oldParts[0]);
        int newMajor = Integer.parseInt(newParts[0]);
        return newMajor > oldMajor;
      }
    } catch (NumberFormatException e) {
      // Can't parse version numbers, assume non-major
    }
    
    return false;
  }
  
  private String determineReleaseType(String oldVersion, String newVersion) {
    if (oldVersion == null || newVersion == null) return "UNKNOWN";
    
    try {
      String[] oldParts = oldVersion.split("\\.");
      String[] newParts = newVersion.split("\\.");
      
      if (oldParts.length >= 2 && newParts.length >= 2) {
        int oldMajor = Integer.parseInt(oldParts[0]);
        int newMajor = Integer.parseInt(newParts[0]);
        int oldMinor = Integer.parseInt(oldParts[1]);
        int newMinor = Integer.parseInt(newParts[1]);
        
        if (newMajor > oldMajor) {
          return "MAJOR";
        } else if (newMinor > oldMinor) {
          return "MINOR";
        } else {
          return "PATCH";
        }
      }
    } catch (NumberFormatException e) {
      // Can't parse version numbers
    }
    
    return "UNKNOWN";
  }
  
  private int calculateUniqueSymbolsCount(BreakingChange bc) {
    // For normalization: count unique symbols affected by this change
    Set<String> symbols = new HashSet<>();
    symbols.add(bc.getClassName());
    if (bc.getMemberName() != null && !bc.getMemberName().equals(bc.getClassName())) {
      symbols.add(bc.getMemberName());
    }
    return symbols.size();
  }
  
  private int calculateAffectedSymbolsCount(BreakingChange bc) {
    // For normalization: count how many symbols could potentially be affected
    // This is a placeholder - you could implement more sophisticated logic
    return 1; // Each breaking change affects at least 1 symbol
  }

  public void close() throws IOException {
    writer.close();
  }
}
