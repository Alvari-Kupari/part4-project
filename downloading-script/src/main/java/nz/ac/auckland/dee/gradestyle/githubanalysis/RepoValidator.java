package nz.ac.auckland.dee.gradestyle.githubanalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RepoValidator {

  public static final int maxJavaFiles = 5000;
  public static final int minJavaFiles = 1;

  private File repoDir;

  public RepoValidator(File repoDir) {
    this.repoDir = repoDir;
  }

  public List<Path> getValidProjects() {

    try (Stream<Path> paths = Files.walk(repoDir.toPath())) {
      return paths.filter(path -> validate(path)).collect(Collectors.toList());
    } catch (IOException e) {
      System.out.println("Failed to navigate repo directories: " + e.getMessage());
      return null;
    }
  }

  private boolean validate(Path moduleDir) {
    try {

      if (!hasSingleRootPom(moduleDir))
        return false;
      if (!hasSingleValidSrcMainJava(moduleDir))
        return false;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  private boolean hasSingleRootPom(Path projectDir) {
    return Files.isRegularFile(projectDir.resolve("pom.xml"));
  }

  private boolean hasSingleValidSrcMainJava(Path projectDir) {
    Path srcMainJava = projectDir.resolve("src/main/java");

    if (!Files.isDirectory(srcMainJava)) {
      return false;
    }

    try (Stream<Path> javaFiles = Files.walk(srcMainJava)) {
      long javaFileCount = javaFiles
          .filter(Files::isRegularFile)
          .filter(p -> p.toString().endsWith(".java"))
          .count();

      return javaFileCount >= minJavaFiles && javaFileCount <= maxJavaFiles;
    } catch (IOException e) {
      System.err.println("Error checking src/main/java: " + e.getMessage());
      return false;
    }
  }
}
