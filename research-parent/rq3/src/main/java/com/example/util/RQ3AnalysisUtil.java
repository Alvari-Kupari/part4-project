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
   * Classifies breaking changes by their dependency type and release type
   */
  public static class BreakingChangeClassification {
    public int directMajorCount = 0;
    public int directNonMajorCount = 0;
    public int transitiveMajorCount = 0;
    public int transitiveNonMajorCount = 0;
    
    public double getDirectNonMajorRate() {
      int total = directMajorCount + directNonMajorCount;
      return total > 0 ? (double) directNonMajorCount / total : 0.0;
    }
    
    public double getTransitiveNonMajorRate() {
      int total = transitiveMajorCount + transitiveNonMajorCount;
      return total > 0 ? (double) transitiveNonMajorCount / total : 0.0;
    }
    
    @Override
    public String toString() {
      return String.format(
          "Direct: %d major, %d non-major (%.2f%% non-major)\n" +
          "Transitive: %d major, %d non-major (%.2f%% non-major)",
          directMajorCount, directNonMajorCount, getDirectNonMajorRate() * 100,
          transitiveMajorCount, transitiveNonMajorCount, getTransitiveNonMajorRate() * 100
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
    
    // Analyze direct breaking changes
    for (BreakingChange bc : directBreakingChanges) {
      if (isMajorVersionChange(bc)) {
        result.directMajorCount++;
      } else {
        result.directNonMajorCount++;
      }
    }
    
    // Analyze transitive breaking changes
    for (BreakingChange bc : transitiveBreakingChanges) {
      if (isMajorVersionChange(bc)) {
        result.transitiveMajorCount++;
      } else {
        result.transitiveNonMajorCount++;
      }
    }
    
    return result;
  }
  
  /**
   * Determines if a breaking change involves a major version change
   */
  public static boolean isMajorVersionChange(BreakingChange bc) {
    String oldVersion = bc.getOldDependency() != null ? 
        bc.getOldDependency().getArtifact().getVersion() : null;
    String newVersion = bc.getNewDependency() != null ? 
        bc.getNewDependency().getArtifact().getVersion() : null;
    
    return isMajorVersionChange(oldVersion, newVersion);
  }
  
  /**
   * Determines if the version change represents a major version bump
   */
  public static boolean isMajorVersionChange(String oldVersion, String newVersion) {
    if (oldVersion == null || newVersion == null) return false;
    
    try {
      String[] oldParts = oldVersion.split("\\.");
      String[] newParts = newVersion.split("\\.");
      
      if (oldParts.length > 0 && newParts.length > 0) {
        int oldMajor = Integer.parseInt(oldParts[0]);
        int newMajor = Integer.parseInt(newParts[0]);
        return newMajor > oldMajor;
      }
    } catch (NumberFormatException e) {
      // Can't parse version numbers, assume non-major for safety
    }
    
    return false;
  }
  
  /**
   * Determines the type of version change (major, minor, patch)
   */
  public static String determineReleaseType(String oldVersion, String newVersion) {
    if (oldVersion == null || newVersion == null) return "UNKNOWN";
    
    try {
      String[] oldParts = oldVersion.split("\\.");
      String[] newParts = newVersion.split("\\.");
      
      if (oldParts.length >= 2 && newParts.length >= 2) {
        int oldMajor = Integer.parseInt(oldParts[0]);
        int newMajor = Integer.parseInt(newParts[0]);
        int oldMinor = Integer.parseInt(oldParts[1]);
        int newMinor = Integer.parseInt(newParts[1]);
        
        if (newMajor > oldMajor) {
          return "MAJOR";
        } else if (newMinor > oldMinor) {
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
