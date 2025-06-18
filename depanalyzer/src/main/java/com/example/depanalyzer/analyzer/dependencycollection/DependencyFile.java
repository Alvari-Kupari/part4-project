package com.example.depanalyzer.analyzer.dependencycollection;

import java.io.File;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;

public class DependencyFile {
  private Dependency dependency;

  public DependencyFile(Dependency dependency) {
    this.dependency = dependency;
  }

  public DependencyFile(Artifact artifact) {
    this.dependency = new Dependency(artifact, null);
  }

  public File getFile() {
    return dependency.getArtifact().getFile();
  }

  @Override
  public String toString() {
    Artifact art = dependency.getArtifact();

    String groupId = art.getGroupId();
    String artifactId = art.getArtifactId();
    String version = art.getVersion();
    return groupId + ":" + artifactId + ":" + version;
  }

  public Dependency getDependency() {
    return dependency;
  }

  @Override
  public int hashCode() {
    Artifact art = dependency.getArtifact();
    int result = 17;
    result = 31 * result + (art.getGroupId() != null ? art.getGroupId().hashCode() : 0);
    result = 31 * result + (art.getArtifactId() != null ? art.getArtifactId().hashCode() : 0);
    result = 31 * result + (art.getVersion() != null ? art.getVersion().hashCode() : 0);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    DependencyFile other = (DependencyFile) obj;
    Artifact a1 = dependency.getArtifact();
    Artifact a2 = other.dependency.getArtifact();
    return a1.getGroupId().equals(a2.getGroupId())
        && a1.getArtifactId().equals(a2.getArtifactId())
        && a1.getVersion().equals(a2.getVersion());
  }
}
