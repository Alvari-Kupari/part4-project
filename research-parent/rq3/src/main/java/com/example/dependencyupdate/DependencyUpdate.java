package com.example.dependencyupdate;

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
import org.eclipse.aether.version.InvalidVersionSpecificationException;
import org.eclipse.aether.version.Version;

public class DependencyUpdate {
  private static final String GET_ALL_VERSIONS = "[0,)";
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

  /**
   * Returns the latest MINOR version of the dependency. For example would update 1.2.4 to 1.9.9
   *
   * @return The latest minor version.
   * @throws VersionRangeResolutionException
   * @throws InvalidVersionSpecificationException
   */
  public Dependency getLatestMinorVersion()
      throws VersionRangeResolutionException, InvalidVersionSpecificationException {

    String range =
        "["
            + getMajorVersion(dependency.getArtifact())
            + ".0, "
            + (getMajorVersion(dependency.getArtifact()) + 1)
            + ".0)";

    Artifact artifact =
        new DefaultArtifact(
            dependency.getArtifact().getGroupId(),
            dependency.getArtifact().getArtifactId(),
            dependency.getArtifact().getClassifier(),
            dependency.getArtifact().getExtension(),
            range);

    VersionRangeRequest request = new VersionRangeRequest(artifact, repos, null);
    VersionRangeResult result = repoSystem.resolveVersionRange(session, request);

    Version latestMinor = result.getHighestVersion();

    artifact.setVersion(latestMinor.toString());

    return new Dependency(artifact, dependency.getScope());
  }

  private int getMajorVersion(Artifact artifact) {
    String[] parts = artifact.getVersion().split("\\.");

    return Integer.parseInt(parts[0]);
  }
}
