package com.example.depanalyzer.analyzer;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;

public class DependencyTraverser {
  private Dependency rootDependency;
  private RepositorySystem repoSystem;
  private RepositorySystemSession session;

  public DependencyTraverser(
      Dependency rootDependency, RepositorySystem repoSystem, RepositorySystemSession session) {
    this.rootDependency = rootDependency;
    this.repoSystem = repoSystem;
    this.session = session;
  }

  public List<Dependency> traverse() throws Exception {
    CollectRequest collectRequest = new CollectRequest();
    collectRequest.setRoot(rootDependency);

    CollectResult collectResult = repoSystem.collectDependencies(session, collectRequest);

    List<Dependency> dependencies = new ArrayList<>();
    collectChildren(collectResult.getRoot(), dependencies);
    return dependencies;
  }

  private void collectChildren(DependencyNode node, List<Dependency> dependencies) {
    for (DependencyNode child : node.getChildren()) {
      dependencies.add(child.getDependency());
      collectChildren(child, dependencies);
    }
  }
}
