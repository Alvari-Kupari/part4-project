package com.example.parsing;

import com.example.BreakingChangeUse;
import com.example.breakingchange.BreakingChange;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SymbolChecker {
  private final Map<String, BreakingChange> breakingChangesBySymbol;
  private final Set<String> classNames;
  private final Set<String> methodSignatures;
  private final Set<String> fieldNames;

  public SymbolChecker() {
    this.breakingChangesBySymbol = new HashMap<>();
    this.classNames = new HashSet<>();
    this.methodSignatures = new HashSet<>();
    this.fieldNames = new HashSet<>();
  }

  /** Initialize the checker with breaking changes to look for */
  public void setBreakingChanges(
      List<BreakingChange> directBreakingChanges, List<BreakingChange> transitiveBreakingChanges) {
    breakingChangesBySymbol.clear();
    classNames.clear();
    methodSignatures.clear();
    fieldNames.clear();

    // Add all breaking changes to our lookup maps
    for (BreakingChange bc : directBreakingChanges) {
      addBreakingChangeToMaps(bc);
    }
    for (BreakingChange bc : transitiveBreakingChanges) {
      addBreakingChangeToMaps(bc);
    }
  }

  private void addBreakingChangeToMaps(BreakingChange bc) {
    String className = bc.getClassName();
    String memberName = bc.getMemberName();
    String changeType = bc.getChangeType();

    // Add class name
    classNames.add(className);
    breakingChangesBySymbol.put(className, bc);

    // Add member-specific signatures
    if ("METHOD_CHANGE".equals(changeType)) {
      String methodSig = className + "." + memberName;
      methodSignatures.add(methodSig);
      breakingChangesBySymbol.put(methodSig, bc);
      breakingChangesBySymbol.put(memberName, bc); // Also check just method name
    } else if ("FIELD_CHANGE".equals(changeType)) {
      String fieldSig = className + "." + memberName;
      fieldNames.add(fieldSig);
      breakingChangesBySymbol.put(fieldSig, bc);
    } else if ("CONSTRUCTOR_CHANGE".equals(changeType)) {
      // Constructor usage typically resolved through class instantiation
      breakingChangesBySymbol.put(className, bc);
    }

    // Add full qualified name lookups
    breakingChangesBySymbol.put(className + "." + memberName, bc);
  }

  public void checkNameUsage(
      String fullyQualifiedName, List<BreakingChangeUse> uses, String exprType) {
    checkNameUsage(fullyQualifiedName, uses, exprType, null, -1);
  }

  public void checkNameUsage(
      String fullyQualifiedName,
      List<BreakingChangeUse> uses,
      String exprType,
      String usageLocation,
      int lineNumber) {
    if (fullyQualifiedName == null || fullyQualifiedName.isEmpty()) {
      return;
    }

    // Check if this symbol matches any breaking change
    BreakingChange matchedBreakingChange = findMatchingBreakingChange(fullyQualifiedName);

    if (matchedBreakingChange == null) {
      return;
    }

    String location = usageLocation != null ? usageLocation : "unknown";
    int line = lineNumber > 0 ? lineNumber : -1;

    BreakingChangeUse use =
        BreakingChangeUse.used(matchedBreakingChange, location, line, fullyQualifiedName, exprType);
    uses.add(use);

    System.out.printf(
        "🔍 BREAKING CHANGE USAGE DETECTED: %s in %s at %s:%d%n",
        exprType, fullyQualifiedName, location, line);
  }

  private BreakingChange findMatchingBreakingChange(String fqn) {
    // Direct lookup first
    if (breakingChangesBySymbol.containsKey(fqn)) {
      return breakingChangesBySymbol.get(fqn);
    }

    // Check if it's a class name
    if (classNames.contains(fqn)) {
      return breakingChangesBySymbol.get(fqn);
    }

    // Check method signatures - extract class and method
    int lastDot = fqn.lastIndexOf('.');
    if (lastDot > 0) {
      String className = fqn.substring(0, lastDot);
      String memberName = fqn.substring(lastDot + 1);

      // Check if the class has breaking changes
      if (classNames.contains(className)) {
        String memberKey = className + "." + memberName;
        if (breakingChangesBySymbol.containsKey(memberKey)) {
          return breakingChangesBySymbol.get(memberKey);
        }
      }
    }

    // Check if any class name is a prefix (for inner classes, etc.)
    for (String className : classNames) {
      if (fqn.startsWith(className)) {
        return breakingChangesBySymbol.get(className);
      }
    }

    return null;
  }
}
