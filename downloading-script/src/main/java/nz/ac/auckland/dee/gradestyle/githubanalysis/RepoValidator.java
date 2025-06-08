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

  public boolean tryRunningTree() {
    Path repoRoot = repoDir.toPath();
    try {
      Path rootPomDir = findTopMostPomDir(repoRoot);
      if (rootPomDir == null) {
        System.out.println("No root pom.xml found.");
        return false;
      }

      ProcessBuilder builder = new ProcessBuilder(
          "cmd.exe", "/c", "mvn dependency:tree -DoutputType=dot");
      builder.directory(rootPomDir.toFile());
      builder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
      builder.redirectError(ProcessBuilder.Redirect.DISCARD);

      Process process = builder.start();
      int exitCode = process.waitFor();

      if (exitCode == 0) {

        return true;
      }

      return false;

    } catch (IOException | InterruptedException e) {
      System.err.println("Failed to run mvn dependency:tree: " + e.getMessage());
      return false;
    }
  }

  private Path findTopMostPomDir(Path root) throws IOException {
    try (Stream<Path> paths = Files.walk(root, 2)) {
      return paths
          .filter(p -> Files.isRegularFile(p) && p.getFileName().toString().equals("pom.xml"))
          .map(Path::getParent)
          .sorted()
          .findFirst()
          .orElse(null);
    }
  }

  public boolean canLikelyRunDependencyTree() {
    Path repoRoot = repoDir.toPath();
    try {
      Path rootPomDir = findTopMostPomDir(repoRoot);
      if (rootPomDir == null) {
        return false;
      }

      ProcessBuilder builder = new ProcessBuilder(
          "cmd.exe", "/c", "mvn validate -q");
      builder.directory(rootPomDir.toFile());
      builder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
      builder.redirectError(ProcessBuilder.Redirect.DISCARD);

      Process process = builder.start();
      int exitCode = process.waitFor();
      return exitCode == 0;

    } catch (IOException | InterruptedException e) {
      return false;
    }
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
