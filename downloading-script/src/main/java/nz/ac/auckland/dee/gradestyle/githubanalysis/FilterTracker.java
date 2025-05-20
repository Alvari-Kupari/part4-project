package nz.ac.auckland.dee.gradestyle.githubanalysis;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FilterTracker {
  public int noRecentCommits = 0;
  public int noValidProjects = 0;
  public int exceptionThrown = 0;
  public int totalConsidered = 0;
  public int totalKept = 0;
  public int tooLarge = 0;
  public int tooManyProjects = 0;

  public void logToFile(List<String> keptRepoLogs) {
    String timestamp = java.time.LocalDateTime.now().toString().replace(":", "-").replace("T", "_");
    File logFile = new File("D:/repo-logs/filter-log-" + timestamp + ".txt");
    try (PrintWriter out = new PrintWriter(logFile)) {
      out.println("--- Repo Filtering Summary ---");
      out.println("Total considered: " + totalConsidered);
      out.println("Total kept: " + totalKept);
      out.println("Total rejected: " + (totalConsidered - totalKept));
      out.println();
      out.println("Rejection reasons:");
      out.println("- No recent commits: " + noRecentCommits);
      out.println("- No valid projects: " + noValidProjects);
      out.println("- Too large size: " + tooLarge);
      out.println("- Too many submodules: " + tooManyProjects);
      out.println("- Exception thrown: " + exceptionThrown);
      out.println();
      out.println("Accepted repos (with valid project counts):");
      for (String line : keptRepoLogs) {
        out.println("- " + line);
      }
    } catch (IOException e) {
      System.err.println("Failed to write filter log: " + e.getMessage());
    }
  }
}
