package nz.ac.auckland.dee.gradestyle.githubanalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
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

public class DownloadScript {

  private static final String REPO_SIZES_LOG = "C:\\Users\\akup390\\Documents\\logs\\repo-sizes.csv";
  private static final String REPO_FILTER_LOG = "C:\\Users\\akup390\\Documents\\logs\\repo-filter.csv";

  // private static final String REPO_SIZES_LOG = "D:\\logs\\repo-sizes.csv";
  // private static final String REPO_FILTER_LOG = "D:\\logs\\repo-filter.txt";

  private GitHub github;
  private Config config;

  public DownloadScript(Config config) throws IOException {
    this.config = config;
    this.github = new GitHubBuilder().withOAuthToken(config.getGithubToken()).build();
  }

  public void download(int n, List<Sort> sortCriteria) throws Exception {
    FilterLogger filterLogger = new FilterLogger(REPO_FILTER_LOG);
    RepoSizeLogger sizeLogger = new RepoSizeLogger(REPO_SIZES_LOG);

    int count = 0;

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

    // Track the last star count to continue search from
    int lastStarCount = Integer.MAX_VALUE;

    while (count < needed) {

      // Build search query with star count filter
      String query = "language:java";
      if (lastStarCount < Integer.MAX_VALUE) {
        query += " stars:<" + lastStarCount;
      }

      GHRepositorySearchBuilder searchBuilder = github.searchRepositories().q(query).order(GHDirection.DESC);

      for (Sort sort : sortCriteria) {
        searchBuilder.sort(sort);
      }

      // Optimize API usage: set page size to 100
      PagedIterator<GHRepository> iterator = searchBuilder.list().withPageSize(100).iterator();

      boolean foundResults = false;
      int resultsProcessed = 0;

      while (iterator.hasNext() && count < needed) {
        foundResults = true;
        resultsProcessed++;

        // Check and handle rate limits
        GHRateLimit rateLimit = github.getRateLimit();
        if (rateLimit.getRemaining() < 10) {
          System.out.println("Rate limit approaching. Pausing until reset...");
          Thread.sleep((rateLimit.getResetDate().getTime() - System.currentTimeMillis()) + 1000);
        }

        System.out.println("Count: " + count);
        GHRepository repo = iterator.next();

        sizeLogger.log(repo);

        // Update the last star count for the next iteration
        lastStarCount = repo.getStargazersCount();

        // Determine the target directory for this repository
        File repoPath = new File(repoDir, repo.getOwnerName() + "__" + repo.getName());

        if (repoPath.exists()) {
          System.out.println("Skipping " + repo.getName() + " (already downloaded).");
          filterLogger.log(repo.getName(), FilterLogger.FilterReason.ALREADY_DOWNLOADED);
          continue;
        }

        RepodownLoader downloader = new RepodownLoader(repo, filterLogger);

        boolean preCheck = downloader.preCheck();

        if (!preCheck) {
          deleteDirectory(repoPath);
          continue;
        }

        boolean success = downloader.downloadSingleRepo(repoPath);

        if (!success) {
          deleteDirectory(repoPath);
          continue;
        }

        // all checks passed
        count++;
      }

      // If no results were found or processed, break to avoid infinite loop
      if (!foundResults || resultsProcessed == 0) {
        System.out.println("No more repositories found. Search exhausted.");
        break;
      }

      System.out
          .println("Processed " + resultsProcessed + " results. Continuing search from star count: " + lastStarCount);
    }

    System.out.println(
        "Downloaded "
            + count
            + " repositories. Total repositories: "
            + (alreadyDownloaded + count));

    sizeLogger.close();
  }

  private void deleteDirectory(File directory) {
    // Delete if repo wasn't successfully validated or an error occurred.
    if (directory.isDirectory()) {
      for (File file : directory.listFiles()) {
        deleteDirectory(file);
      }
    }
    directory.delete();
  }
}
