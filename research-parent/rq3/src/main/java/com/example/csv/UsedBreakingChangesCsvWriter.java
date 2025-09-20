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
 * CSV writer for breaking changes that are actually used in client code. This is extracted from the
 * all CSV to focus only on the breaking changes that impact the client code.
 */
public class UsedBreakingChangesCsvWriter {
  private final Path path;
  private final FileWriter writer;
  private boolean headerWritten = false;

  public UsedBreakingChangesCsvWriter(Path path) throws IOException {
    this.path = path;
    boolean fileExists = Files.exists(path);
    this.writer = new FileWriter(path.toFile(), true); // Append mode
    this.headerWritten = fileExists;
    if (!fileExists) {
      writeHeader();
    }
  }

  public UsedBreakingChangesCsvWriter(SubModule submodule, Path csvFolder, String suffix)
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
            + "Release_Type,Usage_Location,Usage_Line,Usage_Context,Usage_Type\n");
    writer.flush();
  }

  public void writeUsedBreakingChanges(List<BreakingChangeUse> usedBreakingChanges)
      throws IOException {
    for (BreakingChangeUse use : usedBreakingChanges) {
      if (use.isUsedInClient()) {
        writeBreakingChangeUse(use);
      }
    }
    writer.flush();
  }

  private void writeBreakingChangeUse(BreakingChangeUse use) throws IOException {
    BreakingChange bc = use.getBreakingChange();

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
    boolean isTransitive = bc.isTransitive();
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

    // Usage information
    String usageLocation = escapeCSV(use.getUsageLocation());
    int usageLine = use.getLineNumber();
    String usageContext = escapeCSV(use.getUsageContext());
    String usageType = escapeCSV(use.getUsageType());

    // Write CSV row
    writer.append(
        String.format(
            "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%d,%s,%s,%s,%s,%s,%d,%s,%s\n",
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
            usageLocation,
            usageLine,
            usageContext,
            usageType));
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
