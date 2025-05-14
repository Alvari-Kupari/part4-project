package nz.ac.auckland.dee.gradestyle.githubanalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nz.ac.auckland.dee.gradestyle.util.FileUtils;

public class RepoValidator {

  public static final int maxJavaFiles = 1000;
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

      if (!hasSingleRootBuildFile(moduleDir))
        return false;
      if (!hasSingleValidSrcMainJava(moduleDir))
        return false;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  private boolean hasSingleRootBuildFile(Path projectDir) {
    try (Stream<Path> paths = Files.walk(projectDir, 1)) {
      long buildFileCount = paths
          .filter(Files::isRegularFile)
          .filter(this::isBuildFile)
          .count();

      if (buildFileCount != 1) {
        return false;
      }
    } catch (IOException e) {
      System.err.println("Error checking build file: " + e.getMessage());
      return false;
    }

    return true;
  }

  private boolean hasSingleValidSrcMainJava(Path projectDir) {
    try (Stream<Path> paths = Files.walk(projectDir)) {
      List<Path> srcMainJavaDirs = paths
          .filter(Files::isDirectory)
          .filter(path -> path.endsWith("src/main/java"))
          .collect(Collectors.toList());

      if (srcMainJavaDirs.size() != 1) {
        return false;
      }

      // Check Java file count in the single src/main/java
      Path srcDir = srcMainJavaDirs.get(0);
      try (Stream<Path> javaFiles = Files.walk(srcDir)) {
        long javaFileCount = javaFiles
            .filter(Files::isRegularFile)
            .filter(p -> p.toString().endsWith(".java"))
            .count();

        if (javaFileCount < minJavaFiles || javaFileCount > maxJavaFiles) {
          return false;
        }
      }
    } catch (IOException e) {
      System.err.println("Error checking src/main/java: " + e.getMessage());
      return false;
    }

    return true;
  }

  private boolean isBuildFile(Path path) {
    return path.getFileName().toString().equals("pom.xml") || path.getFileName().toString().equals("build.gradle")
        || path.getFileName().toString().equals("build.gradle.kts");

  }
}
