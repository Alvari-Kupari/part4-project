package com.example;

import com.example.depanalyzer.analyzer.dependencycollection.Repositories;
import java.util.List;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.util.version.GenericVersionScheme;
import org.eclipse.aether.version.InvalidVersionSpecificationException;
import org.eclipse.aether.version.Version;
import org.eclipse.aether.version.VersionScheme;

public class DependencyUpdate {
  private Dependency dependency;
  private final List<RemoteRepository> repos = Repositories.repositories;
  private RepositorySystem repoSystem;
  private RepositorySystemSession session;

  public DependencyUpdate(
      Dependency dep, RepositorySystem repoSystem, RepositorySystemSession session) {
    this.dependency = dep;
    this.repoSystem = repoSystem;
    this.session = session;
  }

  // returns true if its the last version available
  public boolean update()
      throws VersionRangeResolutionException, InvalidVersionSpecificationException {
    VersionScheme scheme = new GenericVersionScheme();

    String versionString = dependency.getArtifact().getVersion();
    Version current = scheme.parseVersion(versionString);

    Artifact artifact =
        new DefaultArtifact(
            dependency.getArtifact().getGroupId(),
            dependency.getArtifact().getArtifactId(),
            dependency.getArtifact().getClassifier(),
            dependency.getArtifact().getExtension(),
            "[0,)"); // This requests all available versions

    VersionRangeRequest request = new VersionRangeRequest(artifact, repos, null);
    VersionRangeResult result = repoSystem.resolveVersionRange(session, request);

    List<Version> versions = result.getVersions();

    // System.out.println("Versions size: " + versions.size());
    // versions.forEach(System.out::println);

    for (int i = 0; i < versions.size(); i++) {
      Version v = versions.get(i);

      if (v.equals(current) && i + 1 < versions.size()) {

        if (i + 1 == versions.size()) {
          return true;
        }

        Version next = versions.get(i + 1);
        dependency.getArtifact().setVersion(next.toString());
        return false;
      }
    }

    throw new IllegalStateException("Couldn't find a version match for dependency: " + dependency);
  }
}
