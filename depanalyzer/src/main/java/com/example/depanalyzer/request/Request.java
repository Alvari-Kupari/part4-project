package com.example.depanalyzer.request;

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
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;

public class Request {
  private static final String CENTRAL_REPO_URL = "https://repo.maven.apache.org/maven2";
  private static final RemoteRepository MAVEN_REMOTE_REPOSITORY =
      new RemoteRepository.Builder("central", "default", CENTRAL_REPO_URL).build();

  private RepositorySystem repoSystem;
  private RepositorySystemSession session;

  public Request(RepositorySystem repoSystem, RepositorySystemSession session) {

    this.repoSystem = repoSystem;
    this.session = session;
  }

  public DependencyNode execute(Dependency rootDependency) throws DependencyCollectionException {
    CollectRequest collectRequest = new CollectRequest();
    collectRequest.setRoot(rootDependency);
    collectRequest.addRepository(MAVEN_REMOTE_REPOSITORY);

    CollectResult collectResult = repoSystem.collectDependencies(session, collectRequest);
    return collectResult.getRoot();
  }

  public Set<Artifact> resolve(Dependency dependency) {
    CollectRequest collectRequest = new CollectRequest();
    collectRequest.setRoot(dependency);
    collectRequest.addRepository(MAVEN_REMOTE_REPOSITORY);
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
