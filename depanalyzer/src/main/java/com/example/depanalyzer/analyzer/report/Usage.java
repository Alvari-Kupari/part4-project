package com.example.depanalyzer.analyzer.report;

import java.io.File;

public class Usage {
  private final int lineLocation;
  private final File fileLocation;
  private final Type type;

  public Usage(int lineLocation, File fileLocation, Type type) {
    this.lineLocation = lineLocation;
    this.fileLocation = fileLocation;
    this.type = type;
  }

  @Override
  public String toString() {
    return String.format(
        "%s%n   -> at %s:%d", type.getMessage(), fileLocation.getPath(), lineLocation);
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
