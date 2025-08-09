package com.example;

import com.example.breakingchange.BreakingChange;

/**
 * Represents a breaking change that is detected in client code analysis.
 * Contains information about the breaking change itself and where/how it's used.
 */
public class BreakingChangeUse {
  private final BreakingChange breakingChange;
  private final boolean isUsedInClient;
  private final String usageLocation; // File path where it's used
  private final int lineNumber; // Line number where it's used
  private final String usageContext; // The specific expression/symbol that was used
  private final String usageType; // Type of usage (e.g., "MethodCallExpr", "FieldAccessExpr")
  
  public BreakingChangeUse(BreakingChange breakingChange, boolean isUsedInClient, 
                          String usageLocation, int lineNumber, String usageContext, String usageType) {
    this.breakingChange = breakingChange;
    this.isUsedInClient = isUsedInClient;
    this.usageLocation = usageLocation;
    this.lineNumber = lineNumber;
    this.usageContext = usageContext;
    this.usageType = usageType;
  }
  
  // Factory method for unused breaking changes (found during analysis but not used in client code)
  public static BreakingChangeUse unused(BreakingChange breakingChange) {
    return new BreakingChangeUse(breakingChange, false, null, -1, null, null);
  }
  
  // Factory method for used breaking changes
  public static BreakingChangeUse used(BreakingChange breakingChange, String usageLocation, 
                                       int lineNumber, String usageContext, String usageType) {
    return new BreakingChangeUse(breakingChange, true, usageLocation, lineNumber, usageContext, usageType);
  }
  
  // Getters
  public BreakingChange getBreakingChange() {
    return breakingChange;
  }
  
  public boolean isUsedInClient() {
    return isUsedInClient;
  }
  
  public String getUsageLocation() {
    return usageLocation;
  }
  
  public int getLineNumber() {
    return lineNumber;
  }
  
  public String getUsageContext() {
    return usageContext;
  }
  
  public String getUsageType() {
    return usageType;
  }
  
  @Override
  public String toString() {
    if (isUsedInClient) {
      return String.format("BreakingChangeUse{used=true, change=%s, location=%s:%d, type=%s}", 
                          breakingChange.getClassName() + "." + breakingChange.getMemberName(),
                          usageLocation, lineNumber, usageType);
    } else {
      return String.format("BreakingChangeUse{used=false, change=%s}", 
                          breakingChange.getClassName() + "." + breakingChange.getMemberName());
    }
  }
}
