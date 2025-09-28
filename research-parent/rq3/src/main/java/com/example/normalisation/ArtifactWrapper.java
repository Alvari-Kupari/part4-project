package com.example.normalisation;

import org.eclipse.aether.artifact.Artifact;

public class ArtifactWrapper {
  private final Artifact artifact;
  private final boolean isTransitive;

  public ArtifactWrapper(Artifact artifact, boolean isTransitive) {
    this.artifact = artifact;
    this.isTransitive = isTransitive;
  }

  public Artifact getArtifact() {
    return artifact;
  }

  public boolean isTransitive() {
    return isTransitive;
  }
}
