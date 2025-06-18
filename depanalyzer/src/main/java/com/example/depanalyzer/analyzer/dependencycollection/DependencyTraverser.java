package com.example.depanalyzer.analyzer.dependencycollection;

import com.example.depanalyzer.analyzer.dependencytree.Tree;
import com.example.depanalyzer.request.Request;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.DependencyNode;

public class DependencyTraverser {

  private DependencyFile rootDependency;
  private RepositorySystem repoSystem;
  private RepositorySystemSession session;

  public DependencyTraverser(
      DependencyFile rootDependency, RepositorySystem repoSystem, RepositorySystemSession session) {
    this.rootDependency = rootDependency;
    this.repoSystem = repoSystem;
    this.session = session;
  }

  public void traverse(Tree<DependencyFile> tree) throws DependencyCollectionException {

    Request request = new Request(repoSystem, session);
    try {
      DependencyNode root = request.execute(rootDependency.getDependency());
      add(rootDependency, 0, tree);
      collectChildren(root, tree, 1);
    } catch (DependencyCollectionException e) {
      System.out.println("Unable to collect dependency: " + rootDependency + ". " + e.getMessage());
      return;
    }
  }

  private void collectChildren(DependencyNode node, Tree<DependencyFile> tree, int level) {

    for (DependencyNode child : node.getChildren()) {
      add(new DependencyFile(child.getDependency()), level, tree);
    }

    for (DependencyNode child : node.getChildren()) {
      collectChildren(child, tree, level + 1);
    }
  }

  private void add(DependencyFile dep, int level, Tree<DependencyFile> tree) {
    // if (tree.containsDirect(dep)) {
    //   return;
    // }
    tree.add(dep, level);
  }
}
