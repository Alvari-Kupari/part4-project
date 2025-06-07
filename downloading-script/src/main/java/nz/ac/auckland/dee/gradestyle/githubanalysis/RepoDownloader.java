package nz.ac.auckland.dee.gradestyle.githubanalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import nz.ac.auckland.dee.gradestyle.config.Config;
import org.kohsuke.github.GHDirection;
import org.kohsuke.github.GHRateLimit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHRepositorySearchBuilder;
import org.kohsuke.github.GHRepositorySearchBuilder.Sort;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.PagedIterator;

public class RepoDownloader {
  // limit to
  private static final int maxRepoSize = 85000; // KB

  public static final int maxSubmodules = 31;
  private static final int lastCommitThreshold = 31 * 6; // in days
  // private static final String REPO_SIZES_LOG =
  // "C:\\Users\\akup390\\Documents\\logs\\repo-sizes.csv";
  // private static final String REPO_FILTER_LOG =
  // "C:\\Users\\akup390\\Documents\\logs\\repo-filter.txt";

  private static final String REPO_SIZES_LOG = "D:\\logs\\repo-sizes.csv";
  private static final String REPO_FILTER_LOG = "D:\\logs\\repo-filter.txt";

  private GitHub github;
  private Config config;
  private RepoValidator validator;

  public RepoDownloader(Config config) throws IOException {
    this.config = config;
    this.github = new GitHubBuilder().withOAuthToken(config.getGithubToken()).build();
  }

  public void download(int n, List<Sort> sortCriteria) throws Exception {
    FilterTracker tracker = new FilterTracker(REPO_FILTER_LOG);

    RepoSizeLogger sizeLogger = new RepoSizeLogger(REPO_SIZES_LOG);

    // Determine how many repositories are already downloaded
    File repoDir = config.getRepos().toFile();
    if (!repoDir.exists()) {
      repoDir.mkdirs();
    }
    int alreadyDownloaded = repoDir.list() == null ? 0 : repoDir.list().length;
    int needed = Math.max(0, n - alreadyDownloaded);

    System.out.println("Already downloaded: " + alreadyDownloaded + " repositories.");
    System.out.println("Need to download: " + needed + " more repositories.");

    if (needed == 0) {
      System.out.println("Target already met. No additional downloads required.");
      return;
    }

    // Search repositories and sort by the specified criteria
    GHRepositorySearchBuilder searchBuilder =
        github.searchRepositories().q("language:java").order(GHDirection.DESC);

    for (Sort sort : sortCriteria) {
      searchBuilder.sort(sort);
    }

    // Optimize API usage: set page size to 100
    PagedIterator<GHRepository> iterator = searchBuilder.list().withPageSize(100).iterator();
    int count = 0;

    while (iterator.hasNext() && count < needed) {

      // Check and handle rate limits
      GHRateLimit rateLimit = github.getRateLimit();
      if (rateLimit.getRemaining() < 10) {
        System.out.println("Rate limit approaching. Pausing until reset...");
        Thread.sleep((rateLimit.getResetDate().getTime() - System.currentTimeMillis()) + 1000);
      }

      System.out.println("Count: " + count);
      GHRepository repo = iterator.next();

      tracker.totalConsidered++;
      sizeLogger.log(repo);

      System.out.println(
          "Inspecting repo: " + repo.getName() + " with stars: " + repo.getStargazersCount());

      if (repo.getSize() > maxRepoSize) {
        System.out.println("Skipping " + repo.getName() + " (too large size).");
        tracker.tooLarge++;
        continue;
      }

      if (!CommitChecker.hasRecentCommit(repo, lastCommitThreshold)) {
        System.out.println("Skipping " + repo.getName() + " (no recent commits in last 30 days).");
        tracker.noRecentCommits++;
        continue;
      }

      // Determine the target directory for this repository
      File repoPath = new File(repoDir, repo.getName());

      if (repoPath.exists()) {
        System.out.println("Skipping " + repo.getName() + " (already downloaded).");
        continue;
      }

      // Repository's clone URL
      String cloneUrl = repo.getHttpTransportUrl();

      try {
        System.out.println("Cloning " + repo.getName() + " into " + repoPath.getAbsolutePath());

        // Use Git CLI for shallow clone
        ProcessBuilder builder =
            new ProcessBuilder(
                "git",
                "clone",
                "--depth=1",
                "--config",
                "core.longpaths=true",
                cloneUrl,
                repoPath.getAbsolutePath());

        builder.inheritIO(); // Redirect output to console
        Process process = builder.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
          // success

          // check if passes checks
          validator = new RepoValidator(repoPath);

          List<Path> validPaths = validator.getValidProjects();

          if (validPaths != null && validPaths.size() > maxSubmodules) {
            System.out.println("Failed validation " + repo.getName() + " (too many submodules).");
            tracker.tooManyProjects++;
          } else if (validPaths != null && validPaths.size() > 0) {
            System.out.println("Succeeded for  " + repo.getName() + " (passed all checks).");
            tracker.totalKept++;
            count++;
            continue;
          } else if (validPaths != null) {
            System.out.println("Failed validation " + repo.getName() + " (no valid submodules).");
            tracker.noValidProjects++;
          } else {
            tracker.exceptionThrown++;
            System.out.println("Failed a check during validation");
          }

        } else {
          System.err.println("Failed to clone repository: " + repo.getName());
        }
      } catch (Exception e) {
        System.err.println(
            "Error processing repository: " + repo.getName() + ": " + e.getMessage());
        e.printStackTrace();
      }

      // Delete if repo wasn't successfully validated or an error occurred.
      System.out.println("Deleting: " + repo.getName());
      deleteDirectory(repoPath);
    }

    System.out.println(
        "Downloaded "
            + count
            + " repositories. Total repositories: "
            + (alreadyDownloaded + count));

    tracker.logToFile();

    sizeLogger.close();
  }

  private void deleteDirectory(File directory) {
    if (directory.isDirectory()) {
      for (File file : directory.listFiles()) {
        deleteDirectory(file);
      }
    }
    directory.delete();
  }
}
