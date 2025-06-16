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
  private final Type type;

  public Usage(int lineLocation, File fileLocation, Type type, int columnStart, int columnEnd) {
    this.lineNumber = lineLocation;
    this.columnStart = columnStart;
    this.columnEnd = columnEnd;
    this.fileLocation = fileLocation;
    this.type = type;

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

    return sb.toString();
  }

  public enum Type {
    METHOD_CALL("You called a method here"),
    NAME_EXPRESSION("You used a variable here"),
    OBJECT_CREATION("You instantiated an object here"),
    FIELD_ACCESS("You accessed a field here"),
    CLASS_TYPE("You referenced a class/interface here"),
    VARIABLE_DECLARATION("You declared a variable here");

    private final String message;

    Type(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }
}
