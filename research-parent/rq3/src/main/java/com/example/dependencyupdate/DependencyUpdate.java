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
import org.eclipse.aether.util.version.GenericVersionScheme;
import org.eclipse.aether.version.InvalidVersionSpecificationException;
import org.eclipse.aether.version.Version;
import org.eclipse.aether.version.VersionScheme;

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
   * @return The latest minor version, never null.
   * @throws VersionRangeResolutionException
   * @throws InvalidVersionSpecificationException
   * @throws NoDependencyUpdateException
   */
  public Dependency getLatestMinorVersion()
      throws VersionRangeResolutionException,
          InvalidVersionSpecificationException,
          NoDependencyUpdateException {

    String range =
        "["
            + getMajorVersion(dependency.getArtifact().getVersion())
            + ".0, "
            + (getMajorVersion(dependency.getArtifact().getVersion()) + 1)
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

    List<Version> versions = result.getVersions();

    Version latestMinor = null;
    VersionScheme scheme = new GenericVersionScheme();
    Version current = scheme.parseVersion(dependency.getArtifact().getVersion());

    boolean flag = false;

    if (!versions.isEmpty() && versions.getLast().equals(current)) {
      throw new NoDependencyUpdateException(
          "Dependency is already at it's latest minor version: " + dependency);
    }

    for (int i = 0; i < versions.size(); i++) {
      Version version = versions.get(i);

      // debugging to make sure that the current version is actually found at some point.
      if (version.equals(current)) {
        flag = true;
      }

      if (version.compareTo(current) < 0 || hasPreRelease(version)) {
        continue;
      }

      latestMinor = version;
    }

    if (!flag) {
      throw new IllegalStateException(
          "something has gone seriously wrong, the current pom dependency version was not found in"
              + " the request. POM dep: "
              + dependency);
    }

    if (latestMinor == null) {
      throw new NoDependencyUpdateException(
          "No valid minor dependency updates were found. Likely occurred if there are no minor"
              + " updates after the current one, or all the minor updates have prelease tags"
              + " (alpha, beta, etc).");
    }

    Artifact resolved =
        new DefaultArtifact(
            dependency.getArtifact().getGroupId(),
            dependency.getArtifact().getArtifactId(),
            dependency.getArtifact().getClassifier(),
            dependency.getArtifact().getExtension(),
            latestMinor.toString());
    return new Dependency(resolved, dependency.getScope());
  }

  private int getMajorVersion(String version) {
    String[] parts = version.split("\\.");

    return Integer.parseInt(parts[0]);
  }

  private boolean hasPreRelease(Version version) {
    String vStr = version.toString();

    return vStr.contains("-") || vStr.contains("alpha") || vStr.contains("beta");
  }
}
