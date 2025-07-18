package com.example.depanalyzer.analyzer.report;

public enum UsageType {
  METHOD_CALL("You called a method here"),
  NAME_EXPRESSION("You used a variable here"),
  OBJECT_CREATION("You instantiated an object here"),
  FIELD_ACCESS("You accessed a field here"),
  CLASS_TYPE("You referenced a class/interface here"),
  VARIABLE_DECLARATION("You declared a variable here"),
  ANNOTATION("You used an annotation here");

  private final String message;

  UsageType(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
