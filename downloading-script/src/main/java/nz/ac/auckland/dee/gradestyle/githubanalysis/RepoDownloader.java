package nz.ac.auckland.dee.gradestyle.githubanalysis;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.DirectoryStream.Filter;
import java.util.List;

import org.kohsuke.github.GHRepository;

public class RepodownLoader {
    // limit to
    private static final int maxRepoSize = 85000; // KB

    public static final int maxSubmodules = 31;
    private static final int lastCommitThreshold = 31 * 6; // in days

    private GHRepository repo;
    private RepoValidator validator;
    private FilterLogger filterLogger;

    public RepodownLoader(GHRepository repo, FilterLogger filterLogger) {
        this.repo = repo;
        this.filterLogger = filterLogger;
    }

    public boolean downloadSingleRepo(File repoPath) {

        // Repository's clone URL
        String cloneUrl = repo.getHttpTransportUrl();

        try {
            System.out.println("Cloning " + repo.getName() + " into " + repoPath.getAbsolutePath());

            // Use Git CLI for shallow clone
            ProcessBuilder builder = new ProcessBuilder(
                    "git",
                    "clone",
                    "--depth=1",
                    "--config",
                    "core.longpaths=true",
                    cloneUrl,
                    repoPath.getAbsolutePath());

            builder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
            builder.redirectError(ProcessBuilder.Redirect.DISCARD);
            Process process = builder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                System.err.println("Failed to clone repository: " + repo.getName());
                filterLogger.log(repo.getName(), FilterLogger.FilterReason.EXCEPTION_THROWN);
                return false;
            }

            // check if passes checks
            validator = new RepoValidator(repoPath);

            List<Path> validPaths = validator.getValidProjects();

            if (validPaths == null) {
                System.out.println("Failed a check during validation");
                filterLogger.log(repo.getName(), FilterLogger.FilterReason.EXCEPTION_THROWN);
                return false;
            }

            if (validPaths.isEmpty()) {
                System.out.println("Failed validation " + repo.getName() + " (no valid submodules).");
                filterLogger.log(repo.getName(), FilterLogger.FilterReason.NO_VALID_PROJECTS);
                return false;
            }

            if (validPaths.size() > maxSubmodules) {
                System.out.println("Failed validation " + repo.getName() + " (too many submodules).");
                filterLogger.log(repo.getName(), FilterLogger.FilterReason.TOO_MANY_SUBMODULES);
                return false;
            }

            boolean preChecksPassed = validator.canLikelyRunDependencyTree();

            if (!preChecksPassed) {
                System.out.println("Tree command failure!");
                filterLogger.log(repo.getName(), FilterLogger.FilterReason.ERROR_RUNNING_TREE);
                return false;
            }

            boolean successRunningTree = validator.tryRunningTree();

            if (!successRunningTree) {
                System.out.println("Tree command failure!");
                filterLogger.log(repo.getName(), FilterLogger.FilterReason.ERROR_RUNNING_TREE);
                return false;
            }

            System.out.println("Succeeded for  " + repo.getName() + " (passed all checks).");
            filterLogger.log(repo.getName(), FilterLogger.FilterReason.PASSED_ALL_CHECKS);
            return true;

        } catch (Exception e) {
            System.err.println(
                    "Error processing repository: " + repo.getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    public boolean preCheck() {
        System.out.println(
                "Inspecting repo: " + repo.getName() + " with stars: " + repo.getStargazersCount());

        if (repo.getSize() > maxRepoSize) {
            System.out.println("Skipping " + repo.getName() + " (too large size).");
            filterLogger.log(repo.getName(), FilterLogger.FilterReason.TOO_LARGE);
            return false;
        }

        if (!CommitChecker.hasRecentCommit(repo, lastCommitThreshold)) {
            System.out.println("Skipping " + repo.getName() + " (no recent commits in last 6 months).");
            filterLogger.log(repo.getName(), FilterLogger.FilterReason.NO_RECENT_COMMITS);
            return false;
        }

        return true;
    }

}
