package nz.ac.auckland.dee.gradestyle.githubanalysis;

import java.io.*;
import org.kohsuke.github.GHRepository;

public class RepoSizeLogger {
    private File logFile;
    private PrintWriter pw;

    public RepoSizeLogger(String filePath) throws IOException {
        logFile = new File(filePath);
        boolean exists = logFile.exists();

        pw = new PrintWriter(new FileWriter(logFile, true));
        if (!exists) {
            pw.println("Repo Name,Repo Size (KB),Repo Stars");
        }
    }

    public void log(GHRepository repo) {
        pw.println(repo.getFullName() + "," + repo.getSize() + "," + repo.getStargazersCount());
        pw.flush();
    }

    public void close() {
        pw.close();
    }
}
