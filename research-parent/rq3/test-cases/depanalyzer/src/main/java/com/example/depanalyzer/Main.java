package com.example.depanalyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.lang3.StringUtils;

import junit.runner.BaseTestRunner;

/** Hello world! */
public class Main {
  private static final String laptopRepoPath = "C:\\Users\\Poika\\OneDrive\\Documents\\UNI\\archive\\SOFTENG_206\\r"
      + "epos\\escaipe-room-beta-and-final-team-27";
  private static final String pcRepoPath = "C:\\Users\\Alvari\\Documents\\UNI\\archive\\SOFTENG_206\\r"
      + "epos\\escaipe-room-beta-and-final-team-27";

  private static final String otherPath = "C:\\Users\\Alvari\\Documents\\UNI\\softeng_700\\part4-project\\depanalyzer";

  public static void main(String[] args) throws Exception {

    String repoPath = otherPath;

    // for jar running
    for (String arg : args) {
      if (arg.startsWith("--project=")) {
        repoPath = arg.substring("--project=".length());
      }
    }

    System.out.println("Analyzing project at: " + repoPath);

    Path pomFile = Path.of(repoPath, "pom.xml");

    if (!Files.exists(pomFile)) {
      throw new IOException("Pom file not found at: " + pomFile);
    }
  }

  public void doSomething(String input) {
    BaseTestRunner a = null;
    a.setPreference(input, input);
  }
}
