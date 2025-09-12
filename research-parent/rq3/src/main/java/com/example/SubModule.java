package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;

import com.example.pom.PomException;
import com.example.pom.PomFile;

public class SubModule {
  private static final int MAX_DEPENDENCIES = 3765; // TO CHECK VALUE
  private static final int MAX_LOC = 13000; // TO CHECK VALUE

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

  private Path getPom() {
    return dir.resolve("effective-pom.xml");
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

  public boolean hasTooManyDeps() {
   
    try {
      PomFile pom = new PomFile(dir);
      return pom.getDependencies().size() > MAX_DEPENDENCIES;
    } catch (IOException | PomException e) {
      return true;
    }
  }

  public boolean hasTooManyLOC() {
    Path javaSrc = dir.resolve("src/main/java");
    if (!Files.exists(javaSrc)) {
      return true;
    }

    try {
      long totalLines =
          Files.walk(javaSrc)
              .filter(p -> p.toString().endsWith(".java"))
              .mapToLong(
                  p -> {
                    try {
                      return Files.lines(p).count();
                    } catch (IOException e) {
                      return 0L;
                    }
                  })
              .sum();

      return totalLines > MAX_LOC;
    } catch (IOException e) {
      return true;
    }
  }
}
