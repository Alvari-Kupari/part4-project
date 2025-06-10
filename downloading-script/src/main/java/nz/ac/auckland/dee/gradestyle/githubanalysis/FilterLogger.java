package nz.ac.auckland.dee.gradestyle.githubanalysis;

import java.io.*;
import org.kohsuke.github.GHRepository;

public class FilterLogger {
    private File logFile;
    private PrintWriter pw;

    public FilterLogger(String filePath) throws IOException {
        logFile = new File(filePath);
        boolean exists = logFile.exists();

        pw = new PrintWriter(new FileWriter(logFile, true));
        if (!exists) {
            pw.println("Repo Name,Reason For Rejection");
        }
    }

    public void log(String repoName, FilterReason reason) {
        pw.println(repoName + "," + reason);
        pw.flush();
    }

    public void close() {
        pw.close();
    }

    public enum FilterReason {
        NO_RECENT_COMMITS,
        NO_VALID_PROJECTS,
        TOO_LARGE,
        TOO_MANY_SUBMODULES,
        ERROR_RUNNING_TREE,
        EXCEPTION_THROWN,
        PASSED_ALL_CHECKS,
        ALREADY_DOWNLOADED,
    }

}
