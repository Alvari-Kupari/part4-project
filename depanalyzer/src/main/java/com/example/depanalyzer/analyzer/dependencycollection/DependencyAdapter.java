package com.example.depanalyzer.analyzer.dependencycollection;


public class DependencyAdapter {

  public static org.eclipse.aether.graph.Dependency toAether(
      org.apache.maven.model.Dependency mavenDep) {
    org.eclipse.aether.artifact.Artifact artifact =
        new org.eclipse.aether.artifact.DefaultArtifact(
            mavenDep.getGroupId(),
            mavenDep.getArtifactId(),
            mavenDep.getClassifier(),
            mavenDep.getType(),
            mavenDep.getVersion());
    return new org.eclipse.aether.graph.Dependency(artifact, mavenDep.getScope());
  }

  public static org.apache.maven.model.Dependency toMaven(
      org.eclipse.aether.graph.Dependency aetherDep) {
    org.eclipse.aether.artifact.Artifact artifact = aetherDep.getArtifact();
    org.apache.maven.model.Dependency mavenDep = new org.apache.maven.model.Dependency();
    mavenDep.setGroupId(artifact.getGroupId());
    mavenDep.setArtifactId(artifact.getArtifactId());
    mavenDep.setClassifier(artifact.getClassifier());
    mavenDep.setType(artifact.getExtension());
    mavenDep.setVersion(artifact.getVersion());
    mavenDep.setScope(aetherDep.getScope());
    return mavenDep;
  }
}
