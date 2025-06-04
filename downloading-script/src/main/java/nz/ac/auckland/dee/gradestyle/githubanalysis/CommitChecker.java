package nz.ac.auckland.dee.gradestyle.githubanalysis;

import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class CommitChecker {

    private static final long DAYS_1_MS = 24L * 60 * 60 * 1000;

    public static boolean hasRecentCommit(GHRepository repo, int numDays) {
        try {
            Date threshold = new Date(System.currentTimeMillis() - DAYS_1_MS * numDays);

            Map<String, GHBranch> branches = repo.getBranches();
            for (GHBranch branch : branches.values()) {
                try {
                    GHCommit commit = repo.getCommit(branch.getSHA1());
                    if (commit.getCommitDate().after(threshold)) {
                        return true;
                    }
                } catch (IOException e) {
                    System.err.println("Could not get commit for branch " + branch.getName() + ": " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Failed to list branches for: " + repo.getFullName() + ": " + e.getMessage());
        }

        return false;
    }
}
