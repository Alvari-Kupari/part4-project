package com.example;

import com.example.breakingchange.BreakingChange;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.eclipse.aether.graph.Dependency;

public class CsvWriter {
  private final Path path;
  private final FileWriter writer;

  public CsvWriter(Path path) throws IOException {
    this(path, /*append=*/false);
  }

  // New: appendable constructor; write header only if file is new/empty
  public CsvWriter(Path path, boolean append) throws IOException {
    this.path = path;
    boolean writeHeader = !append || !Files.exists(path) || Files.size(path) == 0L;
    this.writer = new FileWriter(path.toFile(), append);
    if (writeHeader) {
      writeHeader();
    }
  }

  public CsvWriter(SubModule submodule, Path csvFolder) throws IOException {
    String csvFileName = submodule.getRepo().getName() + "_" + submodule.getName() + ".csv";
    this.path = csvFolder.resolve(csvFileName);
    this.writer = new FileWriter(path.toFile());
    writeHeader();
  }

  private void writeHeader() throws IOException {
    writer
        .append(
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

      // Correctly select direct parent (old/new) for CSV
      Dependency parentOldDep =
          bc.getDirectParentDependencyOld() != null
              ? bc.getDirectParentDependencyOld()
              : // fallback: for direct BCs we used the oldDependency as parent
                (!isTransitive ? bc.getOldDependency() : bc.getDirectParentDependency());

      Dependency parentNewDep =
          bc.getDirectParentDependencyNew() != null
              ? bc.getDirectParentDependencyNew()
              : // fallback: for direct BCs we used the newDependency as parent
                (!isTransitive ? bc.getNewDependency() : bc.getDirectParentDependency());

      String parentGA = "";
      if (parentNewDep != null && parentNewDep.getArtifact() != null) {
        parentGA =
            parentNewDep.getArtifact().getGroupId()
                + ":"
                + parentNewDep.getArtifact().getArtifactId();
      } else if (parentOldDep != null && parentOldDep.getArtifact() != null) {
        parentGA =
            parentOldDep.getArtifact().getGroupId()
                + ":"
                + parentOldDep.getArtifact().getArtifactId();
      }

      String parentOld =
          parentOldDep != null && parentOldDep.getArtifact() != null
              ? safe(parentOldDep.getArtifact().getVersion())
              : "";
      String parentNew =
          parentNewDep != null && parentNewDep.getArtifact() != null
              ? safe(parentNewDep.getArtifact().getVersion())
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
    String escaped = s.replace("\"", "\"\"");
    if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
      return "\"" + escaped + "\"";
    }
    return escaped;
  }

  public void close() throws IOException {
    writer.close();
  }

  public java.nio.file.Path getPath() { return this.path; }
}
