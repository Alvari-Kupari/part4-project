package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Repo {
  private Path dir;

  public Repo(Path dir) {
    this.dir = dir;
  }

  public List<SubModule> getSubModules() throws IOException {
    try (Stream<Path> stream = Files.walk(dir)) {
      return stream
          .filter(Files::isDirectory)
          .filter(this::isValidSubModule)
          .map(path -> new SubModule(path, this))
          .toList();
    }
  }

  private boolean isValidSubModule(Path path) {

    return Files.exists(path.resolve("effective-pom.xml"))
        && Files.isDirectory(path.resolve("src/main/java"));
  }

  public static List<Repo> getRepos(Path reposFolder) throws IOException {
    List<Repo> repos = new ArrayList<>();
    try (Stream<Path> paths = Files.list(reposFolder)) {
      paths.filter(Files::isDirectory).forEach(path -> repos.add(new Repo(path)));
    }
    return repos;
  }

  public String getName() {
    return dir.getFileName().toString();
  }
}
