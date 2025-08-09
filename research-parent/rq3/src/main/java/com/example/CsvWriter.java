package com.example;

import com.example.breakingchange.BreakingChange;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.eclipse.aether.graph.Dependency;

public class CsvWriter {
  private final Path path;
  private final FileWriter writer;

  public CsvWriter(Path path) throws IOException {
    this.path = path;
    this.writer = new FileWriter(path.toFile());
    writeHeader();
  }

  public CsvWriter(SubModule submodule, Path csvFolder) throws IOException {
    String csvFileName = submodule.getRepo().getName() + "_" + submodule.getName() + ".csv";
    this.path = csvFolder.resolve(csvFileName);
    this.writer = new FileWriter(path.toFile());
    writeHeader();
  }

  private void writeHeader() throws IOException {
    writer.append(
        "Dependency,Current_Version,Latest_Minor_Version,Class_Name,Member_Name,Change_Type,Description,")
        .append("Binary_Compatible,Source_Compatible,Is_Transitive,Depth,")
        .append("Direct_Dependency_Updated,Direct_Dependency_Old_Version,Direct_Dependency_New_Version,")
        .append("Used_In_Client,Unique_Symbols_Used,Affected_Symbols\n");
    writer.flush();
  }

  public void writeResults(List<BreakingChangeUse> breakingChangeUses) throws IOException {
    for (BreakingChangeUse use : breakingChangeUses) {
      BreakingChange bc = use.getChange();

      String depGA = use.getDependencyGA();
      String curr = safe(use.getCurrentVersion());
      String next = safe(use.getLatestMinorVersion());
      String className = safe(bc.getClassName());
      String memberName = safe(bc.getMemberName());
      String changeType = safe(bc.getChangeType());
      String description = safe(bc.getDescription());
      boolean bin = bc.isBinaryCompatible();
      boolean src = bc.isSourceCompatible();
      boolean isTransitive = bc.isTransitive();
      int depth = bc.getDepth();

      Dependency parent = bc.getDirectParentDependency();
      String parentGA =
          parent != null && parent.getArtifact() != null
              ? parent.getArtifact().getGroupId() + ":" + parent.getArtifact().getArtifactId()
              : "";
      String parentOld =
          parent != null && parent.getArtifact() != null ? safe(parent.getArtifact().getVersion()) : "";
      // For "Direct_Dependency_New_Version" in context of parent upgrade, we use bc.newDependency
      String parentNew =
          bc.getNewDependency() != null && bc.getNewDependency().getArtifact() != null
              ? safe(bc.getNewDependency().getArtifact().getVersion())
              : "";

      String used = use.isUsedInClient() ? "true" : "false";
      String unique = String.valueOf(use.getUniqueSymbolsUsed());
      String affected = String.valueOf(use.getAffectedSymbols());

      writer.append(depGA)
          .append(",")
          .append(curr)
          .append(",")
          .append(next)
          .append(",")
          .append(className)
          .append(",")
          .append(memberName)
          .append(",")
          .append(changeType)
          .append(",")
          .append(description)
          .append(",")
          .append(Boolean.toString(bin))
          .append(",")
          .append(Boolean.toString(src))
          .append(",")
          .append(Boolean.toString(isTransitive))
          .append(",")
          .append(Integer.toString(depth))
          .append(",")
          .append(parentGA)
          .append(",")
          .append(parentOld)
          .append(",")
          .append(parentNew)
          .append(",")
          .append(used)
          .append(",")
          .append(unique)
          .append(",")
          .append(affected)
          .append("\n");
    }

    writer.flush();
  }

  private static String safe(String s) {
    if (s == null) return "";
    // Escape commas/quotes
    String escaped = s.replace("\"", "\"\"");
    if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
      return "\"" + escaped + "\"";
    }
    return escaped;
  }

  public void close() throws IOException {
    writer.close();
  }
}
