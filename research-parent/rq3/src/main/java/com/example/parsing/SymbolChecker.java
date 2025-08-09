package com.example.parsing;

import com.example.BreakingChangeUse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SymbolChecker {
  private final Set<String> usedClasses = new HashSet<>();
  private final Set<String> usedMethods = new HashSet<>();
  private final Set<String> usedFields = new HashSet<>();

  // Back-compat method (unused now)
  public void checkNameUsage(
      String fullyQualifiedName, List<BreakingChangeUse> uses, String exprType) {
    // No-op; kept for compatibility
  }

  public void addClassUse(String fqn) {
    if (fqn != null && !fqn.isBlank()) usedClasses.add(fqn);
  }

  public void addMethodUse(String key) {
    if (key != null && !key.isBlank()) usedMethods.add(key);
  }

  public void addFieldUse(String key) {
    if (key != null && !key.isBlank()) usedFields.add(key);
  }

  public Set<String> getUsedClasses() {
    return usedClasses;
  }

  public Set<String> getUsedMethods() {
    return usedMethods;
  }

  public Set<String> getUsedFields() {
    return usedFields;
  }
}
