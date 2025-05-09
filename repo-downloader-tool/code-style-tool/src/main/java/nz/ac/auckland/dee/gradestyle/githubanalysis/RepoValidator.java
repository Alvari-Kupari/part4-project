package nz.ac.auckland.dee.gradestyle.githubanalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import nz.ac.auckland.dee.gradestyle.util.FileUtils;

public class RepoValidator {

  public static final boolean checkNumberOfFiles = true;
  public static final int maxJavaFiles = 100000;
  public static final int minJavaFiles = 1;
  public static final int maxFileLength = 10000;


  private File repoDir;

  public RepoValidator(File repoDir) {
    this.repoDir = repoDir;
  }

  /**
   * Checks the repo for a few criteria. First, checks whether it has either too much or too few
   * java source files to analyse. Then, it checks whether there are any massive java files which
   * may crash the code style tools. Finally it tests whether the repo can be parsed by javaparser
   * to make sure the code analysis tools can actually process it.
   *
   * @return
   */
  public boolean validate() {
    try {
      List<Path> sourceFiles = FileUtils.getJavaSrcFiles(repoDir.toPath()).toList();

      int numJavaFiles = sourceFiles.size();

      if (numJavaFiles < minJavaFiles || numJavaFiles > maxJavaFiles) {
        System.out.println("Invalid number of java files. Number of files: " + numJavaFiles);
        return false;
      }

      // check if pom.xml exists
      if (!pomExists(repoDir)) {
        return false;
      }



    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  private boolean srcMainJava() {
     if (!Files.exists(new File(repoDir, "src/main/java").toPath())) {
      System.out.println("SRC FOLDER NOT FOUND");
      return false;
    }

    return true;
  }

  private boolean pomExists(File repoDir) {
    try (Stream<Path> paths = Files.walk(repoDir.toPath(), 1)) { // Only search in the root directory
        long pomCount = paths
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


  public long getMaxLinesInJavaFiles(Stream<Path> paths) {
    return paths
        .filter(Files::isRegularFile) // Ensure it's a regular file
        .filter(path -> path.toString().endsWith(".java")) // Only Java files
        .mapToLong(
            path -> {
              try {
                return Files.lines(path).count(); // Count lines in the file
              } catch (IOException e) {
                System.err.println("Error reading file: " + path);
                return 0L; // If an error occurs, treat it as 0 lines
              }
            })
        .max() // Find the maximum number of lines
        .orElse(0L); // Return 0 if no files are processed
  }

  public long getTotalLinesInJavaFiles() {
    try {
      // Get all Java source files in the repository
      List<Path> sourceFiles = FileUtils.getJavaSrcFiles(repoDir.toPath()).toList();

      // Calculate the total lines across all files
      return sourceFiles.stream()
          .filter(Files::isRegularFile) // Ensure it's a regular file
          .filter(path -> path.toString().endsWith(".java")) // Only Java files
          .mapToLong(
              path -> {
                try {
                  return Files.lines(path).count(); // Count lines in the file
                } catch (IOException e) {
                  System.err.println("Error reading file: " + path);
                  return 0L; // Treat unreadable files as 0 lines
                }
              })
          .sum(); // Sum the lines of all files
    } catch (IOException e) {
      System.err.println("Error retrieving Java source files: " + e.getMessage());
      return 0L; // Return 0 if an error occurs
    }
  }
}
