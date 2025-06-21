package com.example.depanalyzer.analyzer.dependencycollection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TreeLevel<T> {
  private Set<T> dependencies;

  private final int level;

  public TreeLevel(int level) {
    this.level = level;
    this.dependencies = new HashSet<>();
  }

  public void add(T dep) {
    dependencies.add(dep);
  }

  public Set<T> getDependencies() {
    return dependencies;
  }

  public int getLevel() {
    return level;
  }

  public void add(Collection<T> dependencies) {
    this.dependencies.addAll(dependencies);
  }

  public int size() {
    return dependencies.size();
  }
}
