package nz.ac.auckland.dee.gradestyle;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;

public class Repo {
 

  private Path dir;

  private String org;

  private String name;

  private String commit;

  public Repo(Path dir, String org, String name, String commit) {
    this.dir = dir;
    this.org = org;
    this.name = name;
    this.commit = commit;
  }

  public Path getDir() {
    return dir;
  }

  public String getOrg() {
    return org;
  }

  public String getName() {
    return name;
  }

  public String getCommit() {
    return commit;
  }

  public String getRepoUrl() {
    if (org == null || name == null) {
      return null;
    }

    return "https://github.com/" + org + "/" + name;
  }

  public String getCommitUrl() {
    if (getRepoUrl() == null) {
      return null;
    }

    return getRepoUrl() + "/commit/" + commit;
  }

  public String getFileUrl(Path file) {
    if (getRepoUrl() == null) {
      return null;
    }

    return getRepoUrl() + "/blob/" + commit + "/" + file;
  }

  public String getFileLineUrl(Path file, int line) {
    if (getFileUrl(file) == null) {
      return null;
    }

    return getFileUrl(file) + "#L" + line;
  }

  public String getFileRangeUrl(Path file, int begin, int end) {
    if (getFileLineUrl(file, begin) == null) {
      return null;
    }

    return getFileLineUrl(file, begin) + "-L" + end;
  }
}
