package com.example.depanalyzer.analyzer.dependencytree;

import com.example.depanalyzer.analyzer.analysis.DependencyUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.DefaultDependencyNode;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;

public class Tree {
  private List<Branch> branches;

  public Tree() {
    this.branches = new ArrayList<>();
  }

  public void addRoot(DependencyNode root) {
    this.branches.add(new Branch(root));
  }

  public Set<Dependency> getDirectDependencies() {
    return branches.stream()
        .map(branch -> branch.getRoot().getDependency())
        .collect(Collectors.toSet());
  }

  public Set<Dependency> getTransitiveDependencies() {

    Set<Dependency> directDeps = getDirectDependencies();

    Set<Dependency> transDeps = getClosestUniqueDependencies();

    transDeps.removeIf(
        dep -> directDeps.stream().anyMatch(directDep -> DependencyUtils.areEqual(directDep, dep)));

    return transDeps;
  }

  private Set<Dependency> getClosestUniqueDependencies() {
    Set<String> alreadyVisited = new HashSet<>();

    List<DependencyNode> roots =
        branches.stream().map(branch -> branch.getRoot()).collect(Collectors.toList());

    DependencyNode king =
        new DefaultDependencyNode(
            new Dependency(
                new DefaultArtifact(
                    "beagle-group", "beagle-artifact", "beagle-extension", "beagle-version"),
                "beagle-scope"));

    king.setChildren(roots);

    return bfs(alreadyVisited, king);
  }

  private Set<Dependency> bfs(Set<String> alreadyVisited, DependencyNode king) {
    Queue<DependencyNode> que = new LinkedList<>();
    que.add(king);

    Set<Dependency> deps = new HashSet<>();

    while (!que.isEmpty()) {
      DependencyNode next = que.poll();

      for (DependencyNode child : next.getChildren()) {
        que.add(child);

        boolean doesntContain =
            alreadyVisited.add(DependencyUtils.getLibraryName(child.getDependency()));

        if (doesntContain) {
          deps.add(child.getDependency());
        }
      }
    }

    return deps;
  }

  public Set<Dependency> getAllDependencies() {
    Set<Dependency> deps = new HashSet<>();

    for (Branch branch : branches) {
      deps.addAll(branch.getAllDeps());
    }

    return deps;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Branch branch : branches) {
      sb.append(branch.toString());
    }
    return sb.toString();
  }

  public long size() {
    return branches.stream().mapToLong(Branch::size).sum();
  }
}
