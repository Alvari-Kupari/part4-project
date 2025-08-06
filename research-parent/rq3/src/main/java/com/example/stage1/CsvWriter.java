package com.example.stage1;

import com.example.SubModule;
import com.example.stage1.breakingchange.BreakingChange;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import org.eclipse.aether.graph.Dependency;

public class CsvWriter {
  private final Path path;
  private final FileWriter writer;
  private int totalBreakingChanges = 0;

  public CsvWriter(Path path) throws IOException {
    this.path = path;
    this.writer = new FileWriter(path.toFile());
    writeHeader();
  }

  private void writeHeader() throws IOException {
    writer.append(
        "Dependency,Current_Version,Latest_Minor_Version,Class_Name,Member_Name,Change_Type,Description,Binary_Compatible,Source_Compatible\n");
    writer.flush();
  }

  public void writeBreakingChange(Dependency dep, Dependency latestMinor, BreakingChange change)
      throws IOException {
    writer
        .append(escape(dep.getArtifact().getGroupId() + ":" + dep.getArtifact().getArtifactId()))
        .append(",")
        .append(escape(dep.getArtifact().getVersion()))
        .append(",")
        .append(escape(latestMinor.getArtifact().getVersion()))
        .append(",")
        .append(escape(change.getClassName()))
        .append(",")
        .append(escape(change.getMemberName()))
        .append(",")
        .append(escape(change.getChangeType()))
        .append(",")
        .append(escape(change.getDescription()))
        .append(",")
        .append(String.valueOf(change.isBinaryCompatible()))
        .append(",")
        .append(String.valueOf(change.isSourceCompatible()))
        .append("\n");
    writer.flush();
    totalBreakingChanges++;
  }

  public int getTotalBreakingChanges() {
    return totalBreakingChanges;
  }

  public Path getPath() {
    return path;
  }

  public void close() throws IOException {
    writer.close();
  }

  private String escape(String value) {
    if (value == null) return "";
    if (value.contains("\"") || value.contains(",") || value.contains("\n")) {
      return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    return value;
  }

  public static CsvWriter createCsv(SubModule submodule, Path csvFolder) throws IOException {
    String csvFileName = submodule.getRepo().getName() + "_" + submodule.getName() + ".csv";
    Path csvPath = csvFolder.resolve(csvFileName);
    return new CsvWriter(csvPath);
  }
}
