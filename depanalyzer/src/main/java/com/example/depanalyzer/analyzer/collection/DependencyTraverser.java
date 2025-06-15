package com.example.depanalyzer.analyzer.collection;

import com.example.depanalyzer.request.Request;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;

public class DependencyTraverser {

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

    Request request = new Request(repoSystem, session);
    try {
      DependencyNode root = request.execute(rootDependency);
      tree.add(rootDependency, 0);
      collectChildren(root, tree, 1);
    } catch (DependencyCollectionException e) {
      System.out.println("Unable to collect dependency: " + rootDependency + ". " + e.getMessage());
      return;
    }
  }

  private void collectChildren(DependencyNode node, DependencyTree tree, int level) {

    if (alreadyVisited.contains(node.getDependency().toString())) {
      // return;
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
