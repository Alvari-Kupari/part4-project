package com.example.depanalyzer.analyzer.analysis;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;

public class DependencyUtils {
  public static String getString(Dependency dep) {
    return getString(dep.getArtifact());
  }

  private static String getString(Artifact artifact) {
    return artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
  }

  public static boolean areEqual(Dependency dep1, Dependency dep2) {
    return getString(dep1).equals(getString(dep2));
  }

  public static boolean areSameLibrary(Dependency dep1, Dependency dep2) {
    return getLibraryName(dep1).equals(getLibraryName(dep2));
  }

  public static String getLibraryName(Dependency dep) {
    return dep.getArtifact().getGroupId() + ":" + dep.getArtifact().getArtifactId();
  }
}
