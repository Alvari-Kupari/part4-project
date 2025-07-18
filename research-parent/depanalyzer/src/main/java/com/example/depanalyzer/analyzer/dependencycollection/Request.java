package com.example.depanalyzer.analyzer.dependencycollection;

import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;

public class Request {

  private RepositorySystem repoSystem;
  private RepositorySystemSession session;

  public Request(RepositorySystem repoSystem, RepositorySystemSession session) {

    this.repoSystem = repoSystem;
    this.session = session;
  }

  public DependencyNode execute(Dependency rootDependency) throws DependencyCollectionException {
    CollectRequest collectRequest = new CollectRequest();
    collectRequest.setRoot(rootDependency);
    collectRequest.setRepositories(Repositories.repositories);

    CollectResult collectResult = repoSystem.collectDependencies(session, collectRequest);
    return collectResult.getRoot();
  }

  public Set<Artifact> resolve(Dependency dependency) {
    CollectRequest collectRequest = new CollectRequest();
    collectRequest.setRoot(dependency);
    collectRequest.setRepositories(Repositories.repositories);
    DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, null);

    DependencyResult result;
    try {
      result = repoSystem.resolveDependencies(session, dependencyRequest);
    } catch (DependencyResolutionException e) {
      System.out.println("Unable to resolve dependency: " + dependency + ". " + e.getMessage());
      return Set.of();
    }

    return result.getArtifactResults().stream()
        .map(ArtifactResult::getArtifact)
        .collect(Collectors.toSet());
  }
}
