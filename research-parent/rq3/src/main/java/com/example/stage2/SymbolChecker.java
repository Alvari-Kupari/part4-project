package com.example.stage2;

import java.util.HashSet;
import java.util.Set;

public class SymbolChecker {
  private final Set<String> names;

  public SymbolChecker() {
    // todo - load the names of the classes from the csv. probably need to change this class a bit
    // to also carry other information, like whether
    this.names = new HashSet<>();
  }

  public void checkNameUsage(String fullyQualifiedName, String exprType) {
    if (names.contains(fullyQualifiedName)) {
      // do something
    }
    // stub — you can replace with your actual logic
    System.out.printf("Used %s: %s%n", exprType, fullyQualifiedName);
  }
}
