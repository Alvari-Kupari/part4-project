package com.example.breakingchange;

import com.example.depanalyzer.analyzer.dependencycollection.Repositories;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator;

/** Analyzes changes in transitive dependencies when a direct dependency is updated. */
public class TransitiveDependencyAnalyzer {

  private static final Logger LOGGER =
      Logger.getLogger(TransitiveDependencyAnalyzer.class.getName());

  private final RepositorySystem repositorySystem;
  private final RepositorySystemSession session;
  private final List<RemoteRepository> repositories;
  private final BreakingChangeAnalyzer breakingChangeAnalyzer;

  public TransitiveDependencyAnalyzer(
      RepositorySystem repositorySystem,
      RepositorySystemSession session,
      BreakingChangeAnalyzer breakingChangeAnalyzer) {
    this.repositorySystem = repositorySystem;
    this.session = session;
    this.repositories = Repositories.repositories;
    this.breakingChangeAnalyzer = breakingChangeAnalyzer;
  }

  /** Represents a transitive dependency change when a direct dependency is updated. */
  public static class TransitiveDependencyChange {
    private final String groupId;
    private final String artifactId;
    private final String oldVersion;
    private final String newVersion;

    private final Dependency oldDirectDependency;
    private final Dependency newDirectDependency;

    public TransitiveDependencyChange(
        String groupId,
        String artifactId,
        String oldVersion,
        String newVersion,
        Dependency oldDirectDependency,
        Dependency newDirectDependency) {
      this.groupId = groupId;
      this.artifactId = artifactId;
      this.oldVersion = oldVersion;
      this.newVersion = newVersion;
      this.oldDirectDependency = oldDirectDependency;
      this.newDirectDependency = newDirectDependency;
    }

    public String getGroupId() {
      return groupId;
    }

    public String getArtifactId() {
      return artifactId;
    }

    public String getOldVersion() {
      return oldVersion;
    }

    public String getNewVersion() {
      return newVersion;
    }

    public Dependency getOldDirectDependency() {
      return oldDirectDependency;
    }

    public Dependency getNewDirectDependency() {
      return newDirectDependency;
    }

    @Override
    public String toString() {
      return String.format("%s:%s (%s -> %s)", groupId, artifactId, oldVersion, newVersion);
    }
  }

  /**
   * Finds all transitive dependency version changes when updating a direct dependency.
   *
   * @param oldDirectDependency The old version of the direct dependency
   * @param newDirectDependency The new version of the direct dependency
   * @return List of transitive dependency changes
   */
  public List<TransitiveDependencyChange> findTransitiveDependencyChanges(
      Dependency oldDirectDependency, Dependency newDirectDependency) {

    LOGGER.info(
        String.format(
            "Analyzing transitive dependency changes for %s (%s -> %s)",
            oldDirectDependency.getArtifact().getArtifactId(),
            oldDirectDependency.getArtifact().getVersion(),
            newDirectDependency.getArtifact().getVersion()));

    // Get transitive dependencies for both versions
    Map<String, String> oldTransitives = getTransitiveDependencyVersions(oldDirectDependency);
    Map<String, String> newTransitives = getTransitiveDependencyVersions(newDirectDependency);

    List<TransitiveDependencyChange> changes = new ArrayList<>();

    // Find version changes in transitive dependencies
    for (Map.Entry<String, String> newEntry : newTransitives.entrySet()) {
      String coordinates = newEntry.getKey(); // groupId:artifactId
      String newVersion = newEntry.getValue();
      String oldVersion = oldTransitives.get(coordinates);

      if (oldVersion != null && !oldVersion.equals(newVersion)) {
        String[] parts = coordinates.split(":");
        changes.add(
            new TransitiveDependencyChange(
                parts[0], parts[1], oldVersion, newVersion, oldDirectDependency, newDirectDependency));
      }
    }

    LOGGER.info(String.format("Found %d transitive dependency version changes", changes.size()));
    return changes;
  }

  /**
   * Analyzes breaking changes in transitive dependencies and marks them as transitive.
   *
   * @param transitiveDependencyChange The transitive dependency change to analyze
   * @return List of breaking changes found in this transitive dependency
   */
  public List<BreakingChange> analyzeTransitiveBreakingChanges(
      TransitiveDependencyChange transitiveDependencyChange) {

    try {
      // Create Dependency objects for the old and new versions of the transitive dependency
      Artifact oldArtifact =
          new org.eclipse.aether.artifact.DefaultArtifact(
              transitiveDependencyChange.getGroupId(),
              transitiveDependencyChange.getArtifactId(),
              "jar",
              transitiveDependencyChange.getOldVersion());

      Artifact newArtifact =
          new org.eclipse.aether.artifact.DefaultArtifact(
              transitiveDependencyChange.getGroupId(),
              transitiveDependencyChange.getArtifactId(),
              "jar",
              transitiveDependencyChange.getNewVersion());

      Dependency oldDep = new Dependency(oldArtifact, "compile");
      Dependency newDep = new Dependency(newArtifact, "compile");

      // Use the existing breaking change analyzer
      List<BreakingChange> base =
          breakingChangeAnalyzer.analyzeBreakingChanges(oldDep, newDep);

      // Mark all changes as transitive and set depth/parent
      List<BreakingChange> transitiveBreakingChanges = new ArrayList<>();
      for (BreakingChange change : base) {
        BreakingChange transitiveChange =
            BreakingChange.builder()
                .className(change.getClassName())
                .memberName(change.getMemberName())
                .changeType(change.getChangeType())
                .description(change.getDescription())
                .oldDependency(change.getOldDependency())
                .newDependency(change.getNewDependency())
                .isBinaryCompatible(change.isBinaryCompatible())
                .isSourceCompatible(change.isSourceCompatible())
                .isTransitive(true)
                // Depth unknown from generator; we mark as >1. Use 2 as conservative default.
                .depth(2)
                .directParentDependency(transitiveDependencyChange.getNewDirectDependency())
                .oldDirectParentDependency(transitiveDependencyChange.getOldDirectDependency())
                .build();
        transitiveBreakingChanges.add(transitiveChange);
      }

      return transitiveBreakingChanges;

    } catch (BreakingChangeAnalysisException e) {
      LOGGER.warning(
          String.format(
              "Failed to analyze transitive breaking changes for %s: %s",
              transitiveDependencyChange, e.getMessage()));
      return new ArrayList<>();
    } catch (Exception e) {
      LOGGER.warning(
          String.format(
              "Unexpected error analyzing transitive breaking changes for %s: %s",
              transitiveDependencyChange, e.getMessage()));
      return new ArrayList<>();
    }
  }

  /**
   * Gets all transitive dependency versions for a given direct dependency.
   *
   * @param dependency The direct dependency to analyze
   * @return Map from "groupId:artifactId" to version string
   */
  private Map<String, String> getTransitiveDependencyVersions(Dependency dependency) {
    Map<String, String> transitiveDeps = new HashMap<>();

    try {
      CollectRequest collectRequest = new CollectRequest();
      collectRequest.setRoot(dependency);
      collectRequest.setRepositories(repositories);

      DependencyRequest dependencyRequest = new DependencyRequest();
      dependencyRequest.setCollectRequest(collectRequest);

      DependencyResult dependencyResult =
          repositorySystem.resolveDependencies(session, dependencyRequest);

      PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
      dependencyResult.getRoot().accept(nlg);

      // Skip the root dependency (which is the direct dependency itself)
      List<Dependency> dependencies = nlg.getDependencies(false);
      for (int i = 1; i < dependencies.size(); i++) {
        Dependency dep = dependencies.get(i);
        Artifact artifact = dep.getArtifact();

        // Only include jar artifacts
        if ("jar".equals(artifact.getExtension())) {
          String coordinates = artifact.getGroupId() + ":" + artifact.getArtifactId();
          transitiveDeps.put(coordinates, artifact.getVersion());
        }
      }

    } catch (DependencyResolutionException e) {
      LOGGER.warning(
          String.format(
              "Failed to resolve transitive dependencies for %s: %s", dependency, e.getMessage()));
    }

    return transitiveDeps;
  }
}
