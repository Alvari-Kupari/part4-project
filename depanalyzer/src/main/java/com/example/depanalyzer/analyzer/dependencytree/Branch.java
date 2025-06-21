package com.example.depanalyzer.analyzer.dependencytree;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;

public class Branch {
  private DependencyNode root;

  public Branch(DependencyNode root) {
    this.root = root;
  }

  public DependencyNode findNode(Dependency parent) {
    return findNodeRecursive(root, parent);
  }

  public DependencyNode getRoot() {
    return root;
  }

  public Collection<Dependency> getAllDeps() {
    Set<Dependency> deps = new HashSet<>();

    addDepsRecursive(root, deps);
    return deps;
  }

  private void addDepsRecursive(DependencyNode node, Collection<Dependency> deps) {
    deps.add(node.getDependency());

    for (DependencyNode child : node.getChildren()) {
      addDepsRecursive(child, deps);
    }
  }

  private DependencyNode findNodeRecursive(DependencyNode node, Dependency dep) {

    if (node.getDependency().equals(dep)) {
      return node;
    }

    for (DependencyNode child : node.getChildren()) {
      DependencyNode found = findNodeRecursive(child, dep);

      if (found != null) {
        return found;
      }
    }

    return null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    printNode(sb, root, "", true);
    return sb.toString();
  }

  private void printNode(StringBuilder sb, DependencyNode node, String prefix, boolean isLast) {
    sb.append(prefix);
    sb.append(isLast ? "\\- " : "+- ");
    sb.append(node.getDependency().toString());
    sb.append("\n");

    var children = node.getChildren();
    int size = children.size();
    int i = 0;
    for (DependencyNode child : children) {
      boolean last = (++i == size);
      printNode(sb, child, prefix + (isLast ? "   " : "|  "), last);
    }
  }

  public long size() {
    AtomicLong count = new AtomicLong();
    countRecursive(root, count);
    return count.get();
  }

  private void countRecursive(DependencyNode node, AtomicLong count) {

    count.incrementAndGet();

    for (DependencyNode child : node.getChildren()) {

      countRecursive(child, count);
    }
  }
}
