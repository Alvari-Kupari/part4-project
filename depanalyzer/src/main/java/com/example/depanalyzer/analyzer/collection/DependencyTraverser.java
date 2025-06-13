package com.example.depanalyzer.analyzer.collection;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.repository.RemoteRepository;

public class DependencyTraverser {
  private static final String CENTRAL_REPO_URL = "https://repo.maven.apache.org/maven2";
  private static final RemoteRepository MAVEN_REMOTE_REPOSITORY =
      new RemoteRepository.Builder("central", "default", CENTRAL_REPO_URL).build();

  private Dependency rootDependency;
  private RepositorySystem repoSystem;
  private RepositorySystemSession session;
  private Set<String> alreadyVisited;

  public DependencyTraverser(
      Dependency rootDependency, RepositorySystem repoSystem, RepositorySystemSession session) {
    this.rootDependency = rootDependency;
    this.repoSystem = repoSystem;
    this.session = session;
    this.alreadyVisited = new HashSet<>();
  }

  public void traverse(DependencyTree tree) throws DependencyCollectionException {

    CollectRequest collectRequest = new CollectRequest();
    collectRequest.setRoot(rootDependency);
    collectRequest.addRepository(MAVEN_REMOTE_REPOSITORY);

    CollectResult collectResult = repoSystem.collectDependencies(session, collectRequest);
    tree.add(rootDependency, 0);
    collectChildren(collectResult.getRoot(), tree, 1);
  }

  private void collectChildren(DependencyNode node, DependencyTree tree, int level) {

    if (alreadyVisited.contains(node.getDependency().toString())) {
      return;
    }

    for (DependencyNode child : node.getChildren()) {
      tree.add(child.getDependency(), level);
      alreadyVisited.add(child.getDependency().toString());
    }

    for (DependencyNode child : node.getChildren()) {
      collectChildren(child, tree, level + 1);
    }
  }
}
