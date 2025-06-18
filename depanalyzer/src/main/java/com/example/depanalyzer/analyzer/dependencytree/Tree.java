package com.example.depanalyzer.analyzer.dependencytree;

import com.example.depanalyzer.analyzer.dependencycollection.DependencyFile;
import com.example.depanalyzer.analyzer.dependencycollection.TreeLevel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tree<T> {
  private static final int BASE_LEVEL = 0;
  private List<TreeLevel<T>> levels;

  public Tree(T root) {
    levels = new ArrayList<>();
    levels.add(new TreeLevel<>(BASE_LEVEL));
    levels.get(0).add(root);
  }

  public Tree() {
    levels = new ArrayList<>();
    levels.add(new TreeLevel<>(BASE_LEVEL));
  }

  public void add(T dependency, int level) {
    // if (level == 0) {
    //   throw new IllegalArgumentException("Can only add roots after tree is created");
    // }
    if (level > levels.size()) {
      throw new IllegalStateException(
          "Cannot be adding a level more than 1 deeper than the trees max depth");
    } else if (level == levels.size()) {
      levels.add(new TreeLevel<>(level));
    }
    levels.get(level).add(dependency);
  }

  public Set<T> getDirectDependencies() {
    return levels.get(BASE_LEVEL).getDependencies();
  }

  public Set<T> getTransitiveDependencies() {
    Set<T> direct = getDirectDependencies();
    Set<T> transitive = getAllDependencies();
    transitive.removeIf(dep -> direct.contains(dep));

    return transitive;
  }

  public Set<T> getDependenciesAtlevel(int level) {
    return levels.get(level).getDependencies();
  }

  public Set<T> getAllDependencies() {
    Set<T> deps = new HashSet<>();

    levels.forEach(level -> deps.addAll(level.getDependencies()));

    return deps;
  }

  public int getMaxDepth() {
    return levels.size();
  }

  public void print() {
    for (int i = 0; i < levels.size(); i++) {
      TreeLevel<T> level = levels.get(i);
      for (T dep : level.getDependencies()) {
        String indent = "  ".repeat(i);
        System.out.println("[" + i + "] " + indent + "- " + dep);
      }
    }
  }

  public int size() {
    return levels.stream().mapToInt(level -> level.size()).sum();
  }

  private String getKey(T dep) {
    return dep.toString();
    // var artifact = dep.getArtifact();
    // return artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
  }

  public boolean contains(T dep) {
    return levels.stream().anyMatch(level -> levels.contains(dep));
  }

  public boolean containsDirect(DependencyFile dep) {
    if (levels.isEmpty()) {
      return false;
    }
    return levels.get(BASE_LEVEL).getDependencies().stream().anyMatch(dep2 -> dep2.equals(dep));
  }
}
