package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;

public class SubModule {
  private final Path dir;
  private final String name;
  private final Repo repo;
  private final String clientRootPackage;

  public SubModule(Path dir, Repo repo) {
    this.dir = dir;
    this.name = dir.getFileName().toString();
    this.repo = repo;

    if (!Files.exists(getPom())) {
      throw new RuntimeException("Pom file not found at: " + getPom());
    }

    this.clientRootPackage = findClientRootPackage();
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

  public String getClientRootPackage() {
    return clientRootPackage;
  }

  private String findClientRootPackage() {
    Path javaSrc = dir.resolve("src/main/java");
    if (!Files.exists(javaSrc)) {
      return null;
    }

    try {
      Optional<Path> shallowestFile =
          Files.walk(javaSrc)
              .filter(p -> p.toString().endsWith(".java"))
              .min(Comparator.comparingInt(p -> javaSrc.relativize(p).getNameCount()));

      if (shallowestFile.isEmpty()) {
        return null;
      }

      Path relative = javaSrc.relativize(shallowestFile.get()).getParent();
      if (relative == null) {
        return null;
      }

      return relative.toString().replace("/", ".").replace("\\", ".");
    } catch (IOException e) {
      return null;
    }
  }
}
