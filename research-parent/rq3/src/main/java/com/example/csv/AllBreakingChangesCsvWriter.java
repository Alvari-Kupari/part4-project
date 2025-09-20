package com.example.csv;

import com.example.BreakingChangeUse;
import com.example.SubModule;
import com.example.breakingchange.BreakingChange;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.eclipse.aether.graph.Dependency;

/**
 * CSV writer specifically for outputting all breaking changes found during analysis, regardless of
 * whether they are used in client code. This is useful for manual inspection and testing of the
 * client analysis.
 */
public class AllBreakingChangesCsvWriter {
  private final Path path;
  private final FileWriter writer;
  private boolean headerWritten = false;

  public AllBreakingChangesCsvWriter(Path path) throws IOException {
    this.path = path;
    boolean fileExists = Files.exists(path);
    this.writer = new FileWriter(path.toFile(), true); // Append mode
    this.headerWritten = fileExists;
    if (!fileExists) {
      writeHeader();
    }
  }

  public AllBreakingChangesCsvWriter(SubModule submodule, Path csvFolder, String suffix)
      throws IOException {
    String csvFileName =
        submodule.getRepo().getName() + "_" + submodule.getName() + suffix + ".csv";
    this.path = csvFolder.resolve(csvFileName);
    boolean fileExists = Files.exists(this.path);
    this.writer = new FileWriter(this.path.toFile(), true); // Append mode
    this.headerWritten = fileExists;
    if (!fileExists) {
      writeHeader();
    }
  }

  private void writeHeader() throws IOException {
    writer.append(
        "Library_Name,Old_Version,New_Version,Class_Name,Member_Name,Change_Type,Description,"
            + "Binary_Compatible,Source_Compatible,Is_Transitive,Depth,Direct_Parent_Dependency,"
            + "Direct_Dependency_Old_Version,Direct_Dependency_New_Version,"
            + "Release_Type,Is_Used_In_Client\n");
    writer.flush();
  }

  public void writeAllBreakingChanges(
      List<BreakingChange> directBreakingChanges, List<BreakingChange> transitiveBreakingChanges)
      throws IOException {
    // Write direct breaking changes
    for (BreakingChange bc : directBreakingChanges) {
      writeBreakingChange(
          bc, false, false); // false = direct, false = not used (unknown at this stage)
    }

    // Write transitive breaking changes
    for (BreakingChange bc : transitiveBreakingChanges) {
      writeBreakingChange(
          bc, true, false); // true = transitive, false = not used (unknown at this stage)
    }

    writer.flush();
  }

  public void writeAllBreakingChangesWithUsage(
      List<BreakingChange> directBreakingChanges,
      List<BreakingChange> transitiveBreakingChanges,
      List<BreakingChangeUse> usageResults)
      throws IOException {
    // Create a set of used breaking changes for quick lookup
    java.util.Set<String> usedBreakingChanges = new java.util.HashSet<>();
    for (BreakingChangeUse use : usageResults) {
      if (use.isUsedInClient()) {
        BreakingChange bc = use.getBreakingChange();
        String key = createBreakingChangeKey(bc);
        usedBreakingChanges.add(key);
      }
    }

    // Write direct breaking changes
    for (BreakingChange bc : directBreakingChanges) {
      String key = createBreakingChangeKey(bc);
      boolean isUsed = usedBreakingChanges.contains(key);
      writeBreakingChange(bc, false, isUsed); // false = direct, isUsed = actual usage
    }

    // Write transitive breaking changes
    for (BreakingChange bc : transitiveBreakingChanges) {
      String key = createBreakingChangeKey(bc);
      boolean isUsed = usedBreakingChanges.contains(key);
      writeBreakingChange(bc, true, isUsed); // true = transitive, isUsed = actual usage
    }

    writer.flush();
  }

  private String createBreakingChangeKey(BreakingChange bc) {
    // Create a unique key for each breaking change to match usage results
    return bc.getClassName() + "." + bc.getMemberName() + "." + bc.getChangeType();
  }

  private void writeBreakingChange(BreakingChange bc, boolean isTransitive, boolean isUsedInClient)
      throws IOException {
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

    // Direct dependency versions (only for transitive dependencies)
    String directDepOldVersion = "";
    String directDepNewVersion = "";
    if (isTransitive) {
      // For transitive dependencies, get the direct parent dependency versions
      if (bc.getOldDirectParentDependency() != null) {
        directDepOldVersion = getDependencyVersion(bc.getOldDirectParentDependency());
      }
      if (bc.getDirectParentDependency() != null) {
        directDepNewVersion = getDependencyVersion(bc.getDirectParentDependency());
      }
    }

    // Version analysis (no major version checks since you only do minor updates)
    String releaseType = determineReleaseType(oldVersion, newVersion);

    // Write CSV row
    writer.append(
        String.format(
            "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%d,%s,%s,%s,%s,%s\n",
            libraryName,
            oldVersion,
            newVersion,
            className,
            memberName,
            changeType,
            description,
            binaryCompatible,
            sourceCompatible,
            isTransitive,
            depth,
            directParent,
            directDepOldVersion,
            directDepNewVersion,
            releaseType,
            isUsedInClient));
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

  private String determineReleaseType(String oldVersion, String newVersion) {
    if (oldVersion == null || newVersion == null) return "UNKNOWN";

    try {
      String[] oldParts = oldVersion.split("\\.");
      String[] newParts = newVersion.split("\\.");

      if (oldParts.length >= 2 && newParts.length >= 2) {
        int oldMinor = Integer.parseInt(oldParts[1]);
        int newMinor = Integer.parseInt(newParts[1]);

        if (newMinor > oldMinor) {
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

  public void close() throws IOException {
    writer.close();
  }
}
