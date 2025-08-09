package com.example.util;

import com.example.breakingchange.BreakingChange;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for RQ3: Analyzing if breaking changes in transitive dependencies 
 * are more likely to occur in non-major releases compared to direct dependencies.
 */
public class RQ3AnalysisUtil {
  
  /**
   * Classifies breaking changes by their dependency type
   */
  public static class BreakingChangeClassification {
    public int directCount = 0;
    public int transitiveCount = 0;
    
    @Override
    public String toString() {
      return String.format(
          "Direct: %d breaking changes\n" +
          "Transitive: %d breaking changes",
          directCount, transitiveCount
      );
    }
  }
  
  /**
   * Analyzes breaking changes for RQ3
   */
  public static BreakingChangeClassification analyzeForRQ3(
      List<BreakingChange> directBreakingChanges,
      List<BreakingChange> transitiveBreakingChanges) {
    
    BreakingChangeClassification result = new BreakingChangeClassification();
    
    // Count direct breaking changes
    result.directCount = directBreakingChanges.size();
    
    // Count transitive breaking changes  
    result.transitiveCount = transitiveBreakingChanges.size();
    
    return result;
  }
  
  /**
   * Determines the type of version change (minor, patch)
   */
  public static String determineReleaseType(String oldVersion, String newVersion) {
    if (oldVersion == null || newVersion == null) return "UNKNOWN";
    
    try {
      String[] oldParts = oldVersion.split("\\.");
      String[] newParts = newVersion.split("\\.");
      
      if (oldParts.length >= 2 && newParts.length >= 2) {
        int oldMinor = Integer.parseInt(oldParts[1]);
        int newMinor = Integer.parseInt(newParts[1]);
        
        if (newMinor > oldMinor) {
          return "MINOR";
        } else {
          return "PATCH";
        }
      }
    } catch (NumberFormatException e) {
      // Can't parse version numbers
    }
    
    return "UNKNOWN";
  }
  
  /**
   * Groups breaking changes by library for detailed analysis
   */
  public static Map<String, List<BreakingChange>> groupByLibrary(List<BreakingChange> breakingChanges) {
    return breakingChanges.stream()
        .collect(Collectors.groupingBy(bc -> 
            bc.getOldDependency() != null ? 
                bc.getOldDependency().getArtifact().getGroupId() + ":" + 
                bc.getOldDependency().getArtifact().getArtifactId() : 
                "unknown"));
  }
}
