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
    String description = bc.getDescription();

    // Add class to our tracking set
    classNames.add(className);
    
    // Add to class-based lookup
    breakingChangesByClass.computeIfAbsent(className, k -> new ArrayList<>()).add(bc);

    // Categorize by type for more specific lookups
    if (changeType != null && changeType.contains("METHOD")) {
      String methodKey = className + "." + memberName;
      methodBreakingChanges.put(methodKey, bc);
      System.out.println("📝 Added METHOD breaking change: " + methodKey + " -> " + description);
    } 
    else if (changeType != null && changeType.contains("FIELD")) {
      String fieldKey = className + "." + memberName;
      fieldBreakingChanges.put(fieldKey, bc);
      System.out.println("📝 Added FIELD breaking change: " + fieldKey + " -> " + description);
    } 
    else if (changeType != null && changeType.contains("CONSTRUCTOR")) {
      String constructorKey = className + ".<init>";
      constructorBreakingChanges.put(constructorKey, bc);
      System.out.println("📝 Added CONSTRUCTOR breaking change: " + constructorKey + " -> " + description);
    } 
    else if (changeType != null && changeType.contains("CLASS")) {
      System.out.println("📝 Added CLASS breaking change: " + className + " -> " + description);
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

    System.out.println("🔍 Checking usage: " + fullyQualifiedName + " (type: " + exprType + ")");

    // Find the most appropriate breaking change based on usage context
    BreakingChange matchedBreakingChange = findMostAppropriateBreakingChange(fullyQualifiedName, exprType);

    if (matchedBreakingChange == null) {
      System.out.println("❌ No breaking change found for: " + fullyQualifiedName);
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
    System.out.println("🔍 Finding breaking change for: " + fqn + " with usage type: " + usageType);
    
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
              System.out.println("🔧 Extracted class name for constructor: " + className);
            }
          }
        }
      }
      
      // Look for constructor breaking changes first
      String constructorKey = className + ".<init>";
      if (constructorBreakingChanges.containsKey(constructorKey)) {
        System.out.println("✅ Found CONSTRUCTOR breaking change for: " + constructorKey);
        return constructorBreakingChanges.get(constructorKey);
      }
      
      // Check if any class breaking changes are constructor-related
      if (classNames.contains(className)) {
        List<BreakingChange> classBreakingChanges = breakingChangesByClass.get(className);
        if (classBreakingChanges != null) {
          // Prefer constructor-related breaking changes for object creation
          for (BreakingChange bc : classBreakingChanges) {
            if (bc.getChangeType() != null && bc.getChangeType().contains("CONSTRUCTOR")) {
              System.out.println("✅ Found CONSTRUCTOR-related breaking change for class: " + className);
              return bc;
            }
          }
          // Fall back to class-level changes for object creation context
          for (BreakingChange bc : classBreakingChanges) {
            if (bc.getChangeType() != null && bc.getChangeType().contains("CLASS")) {
              System.out.println("✅ Found CLASS breaking change for ObjectCreation: " + className);
              return bc;
            }
          }
          // Final fallback
          System.out.println("✅ Found generic breaking change for class: " + className);
          return classBreakingChanges.get(0);
        }
      }
    }
    
    // For VariableDeclarationExpr and ClassOrInterfaceType, prioritize class-level breaking changes
    else if ("VariableDeclarationExpr".equals(usageType) || "ClassOrInterfaceType".equals(usageType)) {
      if (classNames.contains(fqn)) {
        List<BreakingChange> classBreakingChanges = breakingChangesByClass.get(fqn);
        if (classBreakingChanges != null) {
          // Prefer class-level breaking changes for type usage
          for (BreakingChange bc : classBreakingChanges) {
            if (bc.getChangeType() != null && bc.getChangeType().contains("CLASS")) {
              System.out.println("✅ Found CLASS breaking change for: " + fqn);
              return bc;
            }
          }
          // Fall back to any breaking change for this class
          System.out.println("✅ Found breaking change for class: " + fqn);
          return classBreakingChanges.get(0);
        }
      }
    }
    
    // For MethodCallExpr, look for method breaking changes
    else if ("MethodCallExpr".equals(usageType)) {
      // Check if it's a direct method signature match
      if (methodBreakingChanges.containsKey(fqn)) {
        System.out.println("✅ Found METHOD breaking change for: " + fqn);
        return methodBreakingChanges.get(fqn);
      }
      
      // Extract class and method name from qualified signature
      int lastDot = fqn.lastIndexOf('.');
      if (lastDot > 0) {
        String className = fqn.substring(0, lastDot);
        String methodName = fqn.substring(lastDot + 1);
        
        // Remove parameter information if present (e.g., "methodName(param)" -> "methodName")
        if (methodName.contains("(")) {
          methodName = methodName.substring(0, methodName.indexOf("("));
        }
        
        // Look for method breaking changes in this exact class only
        String methodKey = className + "." + methodName;
        if (methodBreakingChanges.containsKey(methodKey)) {
          System.out.println("✅ Found METHOD breaking change for: " + methodKey);
          return methodBreakingChanges.get(methodKey);
        }
        
        // DO NOT do fallback matching to other classes - this was causing false positives
        System.out.println("❌ No exact method match found for: " + methodKey);
      }
    }
    
    // For FieldAccessExpr, look for field breaking changes
    else if ("FieldAccessExpr".equals(usageType)) {
      if (fieldBreakingChanges.containsKey(fqn)) {
        System.out.println("✅ Found FIELD breaking change for: " + fqn);
        return fieldBreakingChanges.get(fqn);
      }
    }
    
    // Generic fallback: check if any class matches EXACTLY
    // Only match exact class names or class names followed by member access
    // BUT be very strict about method calls to avoid false positives
    for (String className : classNames) {
      if (fqn.equals(className)) {
        // Exact class match
        List<BreakingChange> classBreakingChanges = breakingChangesByClass.get(className);
        if (classBreakingChanges != null && !classBreakingChanges.isEmpty()) {
          System.out.println("✅ Found exact class match for: " + className);
          return classBreakingChanges.get(0);
        }
      } else if (fqn.startsWith(className + ".") && fqn.indexOf(".", className.length() + 1) == -1) {
        // Class name followed by single member (e.g., "com.example.Class.method" but not "com.example.ClassOther.method")
        // BUT don't match method calls through generic fallback - too risky for false positives
        if ("MethodCallExpr".equals(usageType)) {
          System.out.println("❌ Skipping generic fallback for MethodCallExpr to avoid false positives: " + className);
          continue;
        }
        
        List<BreakingChange> classBreakingChanges = breakingChangesByClass.get(className);
        if (classBreakingChanges != null && !classBreakingChanges.isEmpty()) {
          System.out.println("✅ Found member access match for class: " + className);
          return classBreakingChanges.get(0);
        }
      }
    }
    
    System.out.println("❌ No matching breaking change found for: " + fqn);
    return null;
  }
}
