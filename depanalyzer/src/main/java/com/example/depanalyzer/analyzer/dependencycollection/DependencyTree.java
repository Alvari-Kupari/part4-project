package com.example.depanalyzer.analyzer.dependencycollection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.aether.graph.Dependency;

public class DependencyTree {
  private static final int BASE_LEVEL = 0;
  private List<DependencyLevel> levels;

  public DependencyTree(Dependency root) {
    levels = new ArrayList<>();
    levels.add(new DependencyLevel(BASE_LEVEL));
    levels.get(0).add(root);
  }

  public DependencyTree() {
    levels = new ArrayList<>();
  }

  public void add(Dependency dependency, int level) {
    if (level > levels.size()) {
      throw new IllegalStateException(
          "Cannot be adding a level more than 1 deeper than the trees max depth");
    } else if (level == levels.size()) {
      levels.add(new DependencyLevel(level));
    }
    levels.get(level).add(dependency);
  }

  public Set<Dependency> getDirectDependencies() {
    return levels.get(BASE_LEVEL).getDependencies();
  }

  public Set<Dependency> getTransitiveDependencies() {

    Set<Dependency> transitive = new HashSet<>();

    Set<String> alreadyVisited = new HashSet<>();

    // add root deps
    alreadyVisited.addAll(
        levels.get(BASE_LEVEL).getDependencies().stream()
            .map(dep -> getKey(dep))
            .collect(Collectors.toList()));

    // now iterate and add all transitive deps not already visited
    for (int i = BASE_LEVEL + 1; i < levels.size(); i++) {

      for (Dependency dep : levels.get(i).getDependencies()) {
        String key = getKey(dep);
        if (!alreadyVisited.add(key)) {
          continue;
        }

        transitive.add(dep);
      }
    }
    return transitive;
  }

  public Set<Dependency> getDependenciesAtlevel(int level) {
    return levels.get(level).getDependencies();
  }

  public List<Dependency> getAllDependencies() {
    List<Dependency> deps = new ArrayList<>();

    levels.forEach(level -> deps.addAll(level.getDependencies()));

    return deps;
  }

  public int getMaxDepth() {
    return levels.size();
  }

  public void print() {
    for (int i = 0; i < levels.size(); i++) {
      DependencyLevel level = levels.get(i);
      for (Dependency dep : level.getDependencies()) {
        String indent = "  ".repeat(i);
        System.out.println("[" + i + "] " + indent + "- " + dep);
      }
    }
  }

  public int size() {
    return levels.stream().mapToInt(level -> level.size()).sum();
  }

  private String getKey(Dependency dep) {
    var artifact = dep.getArtifact();
    return artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
  }
}
