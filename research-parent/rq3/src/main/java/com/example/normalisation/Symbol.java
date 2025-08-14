package com.example.normalisation;

import java.util.Objects;
import org.eclipse.aether.artifact.Artifact;

public class Symbol {
  private final String symbolType; // Type of usage (e.g., "MethodCallExpr", "FieldAccessExpr")
  private final String className;
  private final String symbolName;
  private final String usageLocation; // File path where it's used
  private final int lineNumber; // Line number where it's used
  private final Artifact library;

  private Symbol(Builder builder) {
    this.symbolType = builder.symbolType;
    this.className = builder.className;
    this.symbolName = builder.symbolName;
    this.usageLocation = builder.usageLocation;
    this.lineNumber = builder.lineNumber;
    this.library = builder.library;
  }

  public String getSymbolType() {
    return symbolType;
  }

  public String getClassName() {
    return className;
  }

  public String getSymbolName() {
    return symbolName;
  }

  public String getUsageLocation() {
    return usageLocation;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public Artifact getLibrary() {
    return library;
  }

  public static class Builder {
    private String symbolType;
    private String className;
    private String symbolName;
    private String usageLocation;
    private int lineNumber;
    private Artifact library;

    public Builder symbolType(String symbolType) {
      this.symbolType = symbolType;
      return this;
    }

    public Builder className(String className) {
      this.className = className;
      return this;
    }

    public Builder symbolName(String symbolName) {
      this.symbolName = symbolName;
      return this;
    }

    public Builder usageLocation(String usageLocation) {
      this.usageLocation = usageLocation;
      return this;
    }

    public Builder lineNumber(int lineNumber) {
      this.lineNumber = lineNumber;
      return this;
    }

    public Builder library(Artifact library) {
      this.library = library;
      return this;
    }

    public Symbol build() {
      if (symbolType == null || className == null || symbolName == null || usageLocation == null) {
        throw new IllegalStateException("All fields must be initialised before building Symbol");
      }
      return new Symbol(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Symbol)) return false;
    Symbol other = (Symbol) o;
    return lineNumber == other.lineNumber
        && symbolType.equals(other.symbolType)
        && className.equals(other.className)
        && symbolName.equals(other.symbolName)
        && usageLocation.equals(other.usageLocation)
        && library.equals(other.library);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbolType, className, symbolName, usageLocation, lineNumber, library);
  }
}
