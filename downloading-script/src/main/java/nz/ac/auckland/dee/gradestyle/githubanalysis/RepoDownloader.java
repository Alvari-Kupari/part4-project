package nz.ac.auckland.dee.gradestyle.githubanalysis;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.kohsuke.github.GHRepository;

public class RepodownLoader {
    // limit to
    private static final int maxRepoSize = 85000; // KB

    public static final int maxSubmodules = 31;
    private static final int lastCommitThreshold = 31 * 6; // in days

    private GHRepository repo;
    private RepoValidator validator;
    private FilterTracker tracker;

    public RepodownLoader(GHRepository repo, FilterTracker tracker) {
        this.repo = repo;
        this.tracker = tracker;
    }

    public boolean downloadSingleRepo(File repoPath) {

        if (repoPath.exists()) {
            System.out.println("Skipping " + repo.getName() + " (already downloaded).");
            return false;
        }

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
                tracker.exceptionThrown++;
                return false;
            }

            // check if passes checks
            validator = new RepoValidator(repoPath);

            List<Path> validPaths = validator.getValidProjects();

            if (validPaths == null) {
                System.out.println("Failed a check during validation");
                tracker.exceptionThrown++;
                return false;
            }

            if (validPaths.isEmpty()) {
                System.out.println("Failed validation " + repo.getName() + " (no valid submodules).");
                tracker.noValidProjects++;
                return false;
            }

            if (validPaths.size() > maxSubmodules) {
                System.out.println("Failed validation " + repo.getName() + " (too many submodules).");
                tracker.tooManyProjects++;
                return false;
            }

            boolean preChecksPassed = validator.canLikelyRunDependencyTree();

            if (!preChecksPassed) {
                System.out.println("Tree command failure!");
                tracker.errorRunningTree++;
                return false;
            }

            boolean successRunningTree = validator.tryRunningTree();

            if (!successRunningTree) {
                System.out.println("Tree command failure!");
                tracker.errorRunningTree++;
                return false;
            }

            System.out.println("Succeeded for  " + repo.getName() + " (passed all checks).");
            tracker.totalKept++;
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
            tracker.tooLarge++;
            return false;
        }

        if (!CommitChecker.hasRecentCommit(repo, lastCommitThreshold)) {
            System.out.println("Skipping " + repo.getName() + " (no recent commits in last 6 months).");
            tracker.noRecentCommits++;
            return false;
        }

        return true;
    }

}
