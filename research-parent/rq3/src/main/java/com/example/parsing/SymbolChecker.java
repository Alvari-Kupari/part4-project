package com.example.parsing;

import com.example.BreakingChangeUse;
import com.example.breakingchange.BreakingChange;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SymbolChecker {
  // Store all breaking changes organized by class name
  private final Map<String, List<BreakingChange>> breakingChangesByClass;
  private final Set<String> classNames;
  
  // Store direct lookups for specific signatures
  private final Map<String, BreakingChange> methodBreakingChanges;
  private final Map<String, BreakingChange> fieldBreakingChanges;
  private final Map<String, BreakingChange> constructorBreakingChanges;

  public SymbolChecker() {
    this.breakingChangesByClass = new HashMap<>();
    this.classNames = new HashSet<>();
    this.methodBreakingChanges = new HashMap<>();
    this.fieldBreakingChanges = new HashMap<>();
    this.constructorBreakingChanges = new HashMap<>();
  }

  /** Initialize the checker with breaking changes to look for */
  public void setBreakingChanges(
      List<BreakingChange> directBreakingChanges, List<BreakingChange> transitiveBreakingChanges) {
    
    // Clear all previous data
    breakingChangesByClass.clear();
    classNames.clear();
    methodBreakingChanges.clear();
    fieldBreakingChanges.clear();
    constructorBreakingChanges.clear();

    // Add all breaking changes to our lookup maps
    for (BreakingChange bc : directBreakingChanges) {
      addBreakingChangeToMaps(bc);
    }
    for (BreakingChange bc : transitiveBreakingChanges) {
      addBreakingChangeToMaps(bc);
    }
    
    System.out.println("🔧 SymbolChecker initialized with " + 
                      (directBreakingChanges.size() + transitiveBreakingChanges.size()) + 
                      " breaking changes for " + classNames.size() + " classes");
  }

  private void addBreakingChangeToMaps(BreakingChange bc) {
    String className = bc.getClassName();
    String memberName = bc.getMemberName();
    String changeType = bc.getChangeType();

    // Add class to our tracking set
    classNames.add(className);
    
    // Add to class-based lookup
    breakingChangesByClass.computeIfAbsent(className, k -> new ArrayList<>()).add(bc);

    // Categorize by type for more specific lookups
    if (changeType != null && changeType.contains("METHOD")) {
      String methodKey = className + "." + memberName;
      methodBreakingChanges.put(methodKey, bc);
    } 
    else if (changeType != null && changeType.contains("FIELD")) {
      String fieldKey = className + "." + memberName;
      fieldBreakingChanges.put(fieldKey, bc);
    } 
    else if (changeType != null && changeType.contains("CONSTRUCTOR")) {
      String constructorKey = className + ".<init>";
      constructorBreakingChanges.put(constructorKey, bc);
    }
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

    // Find the most appropriate breaking change based on usage context
    BreakingChange matchedBreakingChange = findMostAppropriateBreakingChange(fullyQualifiedName, exprType);

    if (matchedBreakingChange == null) {
      return;
    }

    String location = usageLocation != null ? usageLocation : "unknown";
    int line = lineNumber > 0 ? lineNumber : -1;

    BreakingChangeUse use =
        BreakingChangeUse.used(matchedBreakingChange, location, line, fullyQualifiedName, exprType);
    uses.add(use);

    System.out.printf(
        "✅ BREAKING CHANGE USAGE DETECTED: %s in %s at %s:%d (BC Type: %s, Description: %s)%n",
        exprType, fullyQualifiedName, location, line, 
        matchedBreakingChange.getChangeType(), matchedBreakingChange.getDescription());
  }

  private BreakingChange findMostAppropriateBreakingChange(String fqn, String usageType) {
    
    // For ObjectCreationExpr, prioritize constructor breaking changes
    if ("ObjectCreationExpr".equals(usageType)) {
      // Extract the class name from constructor signatures like "io.netty.handler.codec.xml.XmlFrameDecoder.XmlFrameDecoder(int)"
      String className = fqn;
      if (fqn.contains(".") && fqn.contains("(")) {
        // Find the last occurrence of a repeated class name pattern
        int lastDot = fqn.lastIndexOf('.');
        if (lastDot > 0) {
          String beforeLastDot = fqn.substring(0, lastDot);
          String afterLastDot = fqn.substring(lastDot + 1);
          
          // Check if it's a constructor pattern (ClassName.ClassName(params))
          if (afterLastDot.contains("(")) {
            String methodName = afterLastDot.substring(0, afterLastDot.indexOf('('));
            if (beforeLastDot.endsWith("." + methodName)) {
              // This looks like a constructor signature
              className = beforeLastDot;
            }
          }
        }
      }
      
      // Look for constructor breaking changes first
      String constructorKey = className + ".<init>";
      if (constructorBreakingChanges.containsKey(constructorKey)) {
        return constructorBreakingChanges.get(constructorKey);
      }
      
      // Check if any class breaking changes are constructor-related
      if (classNames.contains(className)) {
        List<BreakingChange> classBreakingChanges = breakingChangesByClass.get(className);
        if (classBreakingChanges != null) {
          // Prefer constructor-related breaking changes for object creation
          for (BreakingChange bc : classBreakingChanges) {
            if (bc.getChangeType() != null && bc.getChangeType().contains("CONSTRUCTOR")) {
              return bc;
            }
          }
          // Fall back to class-level changes for object creation context
          for (BreakingChange bc : classBreakingChanges) {
            if (bc.getChangeType() != null && bc.getChangeType().contains("CLASS")) {
              return bc;
            }
          }
          // Final fallback
          return classBreakingChanges.get(0);
        }
      }
    }
    
    // For VariableDeclarationExpr and ClassOrInterfaceType, prioritize class-level breaking changes
    else if ("VariableDeclarationExpr".equals(usageType) || "ClassOrInterfaceType".equals(usageType)) {
      if (classNames.contains(fqn)) {
        List<BreakingChange> classBreakingChanges = breakingChangesByClass.get(fqn);
        if (classBreakingChanges != null) {
          // ONLY return class-level or constructor breaking changes for class/type usage
          // Do NOT return method breaking changes for variable declarations
          for (BreakingChange bc : classBreakingChanges) {
            if (bc.getChangeType() != null && (bc.getChangeType().contains("CLASS") || bc.getChangeType().contains("CONSTRUCTOR"))) {
              return bc;
            }
          }
          // Do not fall back to method breaking changes for type usage
        }
      }
    }
    
    // For MethodCallExpr, look for method breaking changes
    else if ("MethodCallExpr".equals(usageType)) {
      // Check if it's a direct method signature match
      if (methodBreakingChanges.containsKey(fqn)) {
        return methodBreakingChanges.get(fqn);
      }
      
      // Extract method name by removing parameter types
      String methodKeyWithoutParams = fqn;
      if (fqn.contains("(")) {
        methodKeyWithoutParams = fqn.substring(0, fqn.indexOf("("));
      }
      
      if (methodBreakingChanges.containsKey(methodKeyWithoutParams)) {
        return methodBreakingChanges.get(methodKeyWithoutParams);
      }
    }
    
    // For FieldAccessExpr, look for field breaking changes
    else if ("FieldAccessExpr".equals(usageType)) {
      if (fieldBreakingChanges.containsKey(fqn)) {
        return fieldBreakingChanges.get(fqn);
      }
    }
    
    // Generic fallback: check if any class matches EXACTLY
    // Only match exact class names or class names followed by member access
    // BUT be very strict about method calls to avoid false positives
    for (String className : classNames) {
      if (fqn.equals(className)) {
        // Exact class match - but only return appropriate breaking changes based on usage type
        List<BreakingChange> classBreakingChanges = breakingChangesByClass.get(className);
        if (classBreakingChanges != null && !classBreakingChanges.isEmpty()) {
          // For type usage, only return class/constructor breaking changes
          if ("ClassOrInterfaceType".equals(usageType) || "VariableDeclarationExpr".equals(usageType)) {
            for (BreakingChange bc : classBreakingChanges) {
              if (bc.getChangeType() != null && (bc.getChangeType().contains("CLASS") || bc.getChangeType().contains("CONSTRUCTOR"))) {
                return bc;
              }
            }
            continue;
          }
          // For other usage types, return the first match
          return classBreakingChanges.get(0);
        }
      } else if (fqn.startsWith(className + ".") && fqn.indexOf(".", className.length() + 1) == -1) {
        // Class name followed by single member (e.g., "com.example.Class.method" but not "com.example.ClassOther.method")
        // BUT don't match method calls through generic fallback - too risky for false positives
        if ("MethodCallExpr".equals(usageType)) {
          continue;
        }
        
        List<BreakingChange> classBreakingChanges = breakingChangesByClass.get(className);
        if (classBreakingChanges != null && !classBreakingChanges.isEmpty()) {
          return classBreakingChanges.get(0);
        }
      }
    }
    
    return null;
  }
}
