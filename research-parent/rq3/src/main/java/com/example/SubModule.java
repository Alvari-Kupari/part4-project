package com.example;

import java.nio.file.Files;
import java.nio.file.Path;

public class SubModule {
  private final Path dir;
  private final String name;
  private final Repo repo;

  public SubModule(Path dir, Repo repo) {
    this.dir = dir;
    this.name = dir.getFileName().toString();
    this.repo = repo;

    if (!Files.exists(getPom())) {
      throw new RuntimeException("Pom file not found at: " + getPom());
    }
  }

  public Path getDir() {
    return dir;
  }

  public String getName() {
    return name;
  }

  public Path getPom() {
    return dir.resolve("pom.xml");
  }

  public Repo getRepo() {
    return repo;
  }
}
