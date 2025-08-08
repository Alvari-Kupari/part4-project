package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

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
        "Dependency,Current_Version,Latest_Minor_Version,Class_Name,Member_Name,Change_Type,Description,Binary_Compatible,Source_Compatible,Is_Transitive,Direct_Dependency_Updated,Direct_Dependency_Old_Version,Direct_Dependency_New_Version\n");
    writer.flush();
  }

  public void writeResults(List<BreakingChangeUse> breakingChangeUses) throws IOException {
    for (BreakingChangeUse use : breakingChangeUses) {
      writer.append("TODO");
    }

    writer.flush();
  }

  public void close() throws IOException {
    writer.close();
  }
}
