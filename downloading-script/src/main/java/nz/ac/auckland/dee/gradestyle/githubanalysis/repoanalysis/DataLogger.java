package nz.ac.auckland.dee.gradestyle.githubanalysis.repoanalysis;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.kohsuke.github.GHRepository;

public class DataLogger {
  private PrintWriter pw;

  public DataLogger(String outputLocation) throws IOException {
    pw = new PrintWriter(new FileWriter(outputLocation, true));
  }

  public void log(GHRepository repo) {
    // String repoName = repo.getFullName();
    // int repoSize = repo.getSize();
    // int[] days = new int[] {30, 60, 90, 180, 365};

    // boolean[] commitResult;
    // try {
    //   commitResult = CommitChecker.hasRecentCommit(repo, days);
    // } catch (IOException e) {
    //   System.err.println("Failed to check commits for " + repoName + ": " + e.getMessage());
    //   return;
    // }

    // String line =
    //     String.join(
    //         ",",
    //         repoName,
    //         String.valueOf(repoSize),
    //         String.valueOf(commitResult[0]),
    //         String.valueOf(commitResult[1]),
    //         String.valueOf(commitResult[2]),
    //         String.valueOf(commitResult[3]),
    //         String.valueOf(commitResult[4]));

    // pw.println(line);
    // pw.flush();
  }

  public void close() {
    pw.close();
  }
}
