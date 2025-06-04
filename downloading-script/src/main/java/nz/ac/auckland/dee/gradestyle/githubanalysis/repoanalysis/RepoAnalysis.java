package nz.ac.auckland.dee.gradestyle.githubanalysis.repoanalysis;

import java.io.IOException;
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

public class RepoAnalysis {

  private GitHub github;
  private DataLogger logger;

  public RepoAnalysis(Config config) throws IOException {
    this.github = new GitHubBuilder().withOAuthToken(config.getGithubToken()).build();
    this.logger = new DataLogger("D:/repo-logs/logs-1000-repos.csv");
  }

  public void performAnalysis(int n, List<Sort> sortCriteria) throws Exception {

    // Search repositories and sort by the specified criteria
    GHRepositorySearchBuilder searchBuilder =
        github.searchRepositories().q("language:java stars:<4329").order(GHDirection.DESC);

    for (Sort sort : sortCriteria) {
      searchBuilder.sort(sort);
    }

    // Optimize API usage: set page size to 100
    PagedIterator<GHRepository> iterator = searchBuilder.list().withPageSize(100).iterator();
    int count = 0;

    while (iterator.hasNext() && count < n) {

      // Check and handle rate limits
      GHRateLimit rateLimit = github.getRateLimit();
      if (rateLimit.getRemaining() < 10) {
        System.out.println("Rate limit approaching. Pausing until reset...");
        Thread.sleep((rateLimit.getResetDate().getTime() - System.currentTimeMillis()) + 1000);
      }

      System.out.println("Count: " + count);
      GHRepository repo = iterator.next();

      System.out.println(
          "Inspecting repo: " + repo.getName() + " with stars: " + repo.getStargazersCount());

      logger.log(repo);
      count++;
    }

    System.out.println("Inspected " + count + " repos");
    logger.close();
  }
}
