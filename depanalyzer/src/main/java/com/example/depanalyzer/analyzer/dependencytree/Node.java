package com.example.depanalyzer.analyzer.dependencytree;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Node<T> {
  private T dep;
  private Set<Node<T>> children;

  public Node(T dep, Collection<T> children) {
    this.dep = dep;
    this.children = new HashSet<>();
    children.forEach(child -> this.children.add(new Node<>(dep)));
  }

  private Node(T dep) {
    this.dep = dep;
  }

  public T getDependency() {
    return dep;
  }
}
