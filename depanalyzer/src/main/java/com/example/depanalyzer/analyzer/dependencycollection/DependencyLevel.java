package com.example.depanalyzer.analyzer.dependencycollection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.aether.graph.Dependency;

public class DependencyLevel {
  private Set<Dependency> dependencies;

  private final int level;

  public DependencyLevel(int level) {
    this.level = level;
    this.dependencies = new HashSet<>();
  }

  public void add(Dependency dep) {
    dependencies.add(dep);
  }

  public Set<Dependency> getDependencies() {
    return dependencies;
  }

  public int getLevel() {
    return level;
  }

  public void add(List<Dependency> dependencies) {
    this.dependencies.addAll(dependencies);
  }

  public int size() {
    return dependencies.size();
  }
}
