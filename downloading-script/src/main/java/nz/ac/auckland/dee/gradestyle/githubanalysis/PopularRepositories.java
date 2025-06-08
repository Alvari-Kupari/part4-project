package nz.ac.auckland.dee.gradestyle.githubanalysis;

import java.io.IOException;
import java.util.List;
import nz.ac.auckland.dee.gradestyle.config.Config;
import org.kohsuke.github.*;
import org.kohsuke.github.GHRepositorySearchBuilder.Sort;

public class PopularRepositories {

  public static void main(String[] args) {
    try {
      Config config = Config.parse(args);
      DownloadScript downloader = new DownloadScript(config);

      List<Sort> criteria = List.of(Sort.STARS);

      downloader.download(10, criteria);

    } catch (IOException e) {
      System.err.println("Error fetching repositories: " + e.getMessage() + e.getCause());
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Error processing repositories: " + e.getMessage() + e.getCause());
    }
  }
}
