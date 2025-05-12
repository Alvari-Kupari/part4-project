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

  public boolean validate() {
    try {
      List<Path> sourceFiles = FileUtils.getJavaSrcFiles(repoDir.toPath()).toList();
      int numJavaFiles = sourceFiles.size();

      if (numJavaFiles < minJavaFiles || numJavaFiles > maxJavaFiles) {
        System.out.println("Invalid number of java files. Number of files: " + numJavaFiles);
        return false;
      }

      if (!hasSingleRootPom(repoDir)) return false;
      if (!hasSingleSrcMainJava(repoDir)) return false;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  private boolean hasSingleRootPom(File repoDir) {
    try (Stream<Path> paths = Files.walk(repoDir.toPath(), 1)) {
      long pomCount =
          paths
              .filter(Files::isRegularFile)
              .filter(path -> path.getFileName().toString().equals("pom.xml"))
              .count();

      if (pomCount != 1) {
        System.out.println("POM FILE CHECK FAILED. Found " + pomCount + " pom.xml files in root.");
        return false;
      }
    } catch (IOException e) {
      System.err.println("Error checking pom.xml: " + e.getMessage());
      return false;
    }

    return true;
  }

  private boolean hasSingleSrcMainJava(File repoDir) {
    try (Stream<Path> paths = Files.walk(repoDir.toPath())) {
      List<Path> srcMainJavaDirs =
          paths
              .filter(Files::isDirectory)
              .filter(path -> path.endsWith("src/main/java"))
              .filter(
                  path -> {
                    try (Stream<Path> files = Files.walk(path)) {
                      return files.anyMatch(p -> p.toString().endsWith(".java"));
                    } catch (IOException e) {
                      return false;
                    }
                  })
              .collect(Collectors.toList());

      if (srcMainJavaDirs.size() != 1) {
        System.out.println(
            "SRC CHECK FAILED. Found "
                + srcMainJavaDirs.size()
                + " src/main/java directories with Java files.");
        return false;
      }

    } catch (IOException e) {
      System.err.println("Error checking src/main/java: " + e.getMessage());
      return false;
    }

    return true;
  }
}
