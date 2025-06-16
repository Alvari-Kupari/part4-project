package com.example.depanalyzer.analyzer.dependencycollection;

import java.io.File;
import org.eclipse.aether.artifact.Artifact;

public class DependencyFile {
  private File file;
  private String groupId;
  private String artifactId;
  private String version;
  private String classifier;

  public DependencyFile(Artifact artifact) {
    this.file = artifact.getFile();
    this.groupId = artifact.getGroupId();
    this.artifactId = artifact.getArtifactId();
    this.version = artifact.getVersion();
    this.classifier = artifact.getClassifier();
  }

  public File getFile() {
    return file;
  }

  public String getLibraryName() {
    return classifier == null || classifier.isEmpty()
        ? groupId + ":" + artifactId + ":" + version
        : groupId + ":" + artifactId + ":" + version + ":" + classifier;
  }
}
