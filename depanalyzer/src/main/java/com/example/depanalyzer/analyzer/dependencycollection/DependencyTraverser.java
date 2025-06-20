package com.example.depanalyzer.analyzer.dependencycollection;

import com.example.depanalyzer.analyzer.dependencytree.Tree;
import com.example.depanalyzer.request.Request;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;

public class DependencyTraverser {

  private RepositorySystem repoSystem;
  private RepositorySystemSession session;
  private Dependency root;

  public DependencyTraverser(
      Dependency rootDependency, RepositorySystem repoSystem, RepositorySystemSession session) {
    this.repoSystem = repoSystem;
    this.session = session;
    this.root = rootDependency;
  }

  public void traverse(Tree tree) throws DependencyCollectionException {

    Request request = new Request(repoSystem, session);
    try {
      DependencyNode root = request.execute(this.root);

      tree.addRoot(root);
    } catch (DependencyCollectionException e) {
      System.out.println("Unable to collect dependency: " + root + ". " + e.getMessage());
      return;
    }
  }
}
