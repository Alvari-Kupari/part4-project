package com.example.depanalyzer.analyzer.report;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Usage {
  private static final String DEFAULT_LINE =
      "[ERROR]: Error reading this line of code. Unable to show line.";
  private final int lineNumber;
  private final String lineText;
  private final int columnStart;
  private final int columnEnd;

  private final File fileLocation;
  private final UsageType type;
  private final String libraryName;

  public Usage(
      int lineLocation,
      File fileLocation,
      UsageType type,
      int columnStart,
      int columnEnd,
      String libraryName) {
    this.lineNumber = lineLocation;
    this.columnStart = columnStart;
    this.columnEnd = columnEnd;
    this.fileLocation = fileLocation;
    this.type = type;
    this.libraryName = libraryName;

    List<String> lines;
    try {
      lines = Files.readAllLines(fileLocation.toPath());

    } catch (IOException e) {
      System.out.println(
          "[ERROR] Error reading file: "
              + fileLocation
              + ". Unable to read the line of code. "
              + e.getMessage());
      this.lineText = DEFAULT_LINE;
      return;
    }

    String line = lines.get(lineNumber - 1);
    this.lineText = line;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public String getLineText() {
    return lineText;
  }

  public int getColumnStart() {
    return columnStart;
  }

  public int getColumnEnd() {
    return columnEnd;
  }

  public File getFileLocation() {
    return fileLocation;
  }

  public UsageType getType() {
    return type;
  }

  public String getLibraryName() {
    return libraryName;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(
        String.format("%s%n   ↳ at %s:%d", type.getMessage(), fileLocation.getPath(), lineNumber));
    sb.append("\n   ").append(lineText);

    if (columnEnd >= columnStart) {
      int start = Math.max(0, columnStart - 1);
      int length = Math.max(1, columnEnd - columnStart + 1);
      sb.append("\n   ").append(" ".repeat(start)).append("^".repeat(length));
    } else {
      sb.append("\n   [multi-line expression — squiggle skipped]");
    }
    sb.append(String.format("%n   ↳ comes from library: %s", libraryName));

    return sb.toString();
  }
}
