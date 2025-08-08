package com.example.dependencyupdate;

import com.example.depanalyzer.analyzer.dependencycollection.Repositories;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
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
  private static final Logger LOGGER = Logger.getLogger(DependencyUpdate.class.getName());

  public DependencyUpdate(
      Dependency dep, RepositorySystem repoSystem, RepositorySystemSession session) {
    this.dependency = dep;
    this.repoSystem = repoSystem;
    this.session = session;
  }

  /**
   * Gives you all the minor dependencies after the current one up to the next major version. It
   * excludes prerelease (alpha, beta) versions and the dependency itself from the result.
   *
   * @return a list of all minor dependencies after the given one, excluding the original and
   *     prerelease versions.
   * @throws VersionResolutionException if an error occurs. (major problem)
   * @throws NoDependencyUpdateException if the dpeendency is already fully updated to the latest
   *     version. Should skip and move onto the next dependency if this occurs.
   */
  public List<Dependency> getMinorUpdates()
      throws VersionResolutionException, NoDependencyUpdateException {

    String range =
        "[" + getMajorVersion(dependency) + ".0, " + (getMajorVersion(dependency) + 1) + ".0)";

    Artifact artifact =
        new DefaultArtifact(
            dependency.getArtifact().getGroupId(),
            dependency.getArtifact().getArtifactId(),
            dependency.getArtifact().getClassifier(),
            dependency.getArtifact().getExtension(),
            range);

    VersionRangeRequest request = new VersionRangeRequest(artifact, repos, null);

    VersionScheme scheme = new GenericVersionScheme();
    Version current;
    VersionRangeResult result;

    try {
      current = scheme.parseVersion(dependency.getArtifact().getVersion());
      result = repoSystem.resolveVersionRange(session, request);
    } catch (InvalidVersionSpecificationException e) {
      throw new VersionResolutionException(e);
    } catch (VersionRangeResolutionException e) {
      throw new VersionResolutionException(e);
    }

    List<Version> versions = result.getVersions();

    System.out.println("versions before for dep: " + dependency + ": " + versions);

    // filter our version below the one in the pom, and filter out pre release versions.
    for (int i = versions.size() - 1; i >= 0; i--) {
      Version version = versions.get(i);
      if (version.equals(current)) continue;
      if (version.compareTo(current) < 0 || hasPreRelease(version)) {
        versions.remove(i);
      }
    }

    System.out.println("versions after for dep: " + dependency + ": " + versions);

    boolean wasRemoved = versions.remove(current);

    if (!wasRemoved) {
      throw new VersionResolutionException(
          "something has gone seriously wrong, the current pom dependency version was not found in"
              + " the request. POM dep: "
              + dependency);
    }

    if (versions.isEmpty()) {
      throw new NoDependencyUpdateException(
          "Dependency is already at it's latest minor version: " + dependency);
    }

    if (versions.contains(current)) {
      throw new VersionResolutionException("Bug occurred; shouldnt have reached here.");
    }

    return convertVersionsToDependencies(versions);
  }

  private List<Dependency> convertVersionsToDependencies(List<Version> versions) {
    List<Dependency> deps = new ArrayList<>();

    for (Version version : versions) {
      Artifact artifact =
          new DefaultArtifact(
              dependency.getArtifact().getGroupId(),
              dependency.getArtifact().getArtifactId(),
              dependency.getArtifact().getClassifier(),
              dependency.getArtifact().getExtension(),
              version.toString());
      Dependency dep = new Dependency(artifact, dependency.getScope());

      deps.add(dep);
    }

    return deps;
  }

  private int getMajorVersion(Dependency dep) {
    String version = dep.getArtifact().getVersion();
    String[] parts = version.split("\\.");

    try {
      return Integer.parseInt(parts[0]);
    } catch (NumberFormatException e) {
      LOGGER.severe("Failed to parse major version from: '" + version + "' in dependency: " + dep);
      throw new IllegalArgumentException("Invalid version format: " + version, e);
    }
  }

  private boolean hasPreRelease(Version version) {
    String vStr = version.toString().toLowerCase();

    return vStr.contains("-")
        || vStr.contains("alpha")
        || vStr.contains("beta")
        || vStr.contains("rc")
        || vStr.contains("m")
        || // e.g., M1, M2
        vStr.contains("snapshot")
        || vStr.contains("preview");
  }
}
