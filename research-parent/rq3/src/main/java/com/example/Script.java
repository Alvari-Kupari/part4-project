package com.example;

import com.example.breakingchange.BreakingChange;
import com.example.breakingchange.BreakingChangeAnalyzer;
import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;
import com.example.dependencyupdate.DependencyUpdate;
import com.example.dependencyupdate.NoDependencyUpdateException;
import com.example.pom.PomException;
import com.example.pom.PomFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.version.InvalidVersionSpecificationException;

public class Script {
  private static final Path csvFolder =
      Paths.get(
          "C:\\Users\\Alvari\\Documents\\UNI\\softeng_700\\part4-project\\r"
              + "esearch-parent\\rq3\\data");

  private static final Path reposFolder = Paths.get("D:/repos");
  private static final RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
  private static final RepositorySystemSession session = RepositorySystemFactory.newSession(system);
  private static final BreakingChangeAnalyzer breakingChangeAnalyzer =
      new BreakingChangeAnalyzer(system, session);
  private static final Logger LOGGER = Logger.getLogger(Script.class.getName());

  public static void main(String[] args) throws IOException {

    if (!Files.isDirectory(reposFolder)) {
      throw new IOException("Repos folder not found at: " + reposFolder);
    }

    Files.createDirectories(csvFolder);

    List<Repo> repos = Repo.getRepos(reposFolder);

    for (Repo repo : repos) {
      LOGGER.info(
          "\n\n========== STARTING ANALYSIS FOR REPOSITORY: " + repo.getName() + " ==========\n");

      List<SubModule> subModules = repo.getSubModules();

      if (subModules.isEmpty()) {
        throw new RuntimeException("No submodules found for repo: " + repo.getName());
      }

      inner:
      for (SubModule submodule : subModules) {
        try {
          performSubmoduleAnalysis(submodule);
        } catch (PomException e) {
          LOGGER.log(
              Level.SEVERE,
              "Failed to parse POM for submodule '"
                  + submodule.getName()
                  + "' at "
                  + submodule.getDir()
                  + ". Skipping this submodule.",
              e);
          continue inner;
        }
      }
    }
  }

  private static void performSubmoduleAnalysis(SubModule submodule)
      throws IOException, PomException {

    LOGGER.info(
        "=== Starting Analysis  for submodule '"
            + submodule.getName()
            + "'\n at "
            + submodule.getDir().toAbsolutePath());

    String csvFileName = submodule.getRepo().getName() + "_" + submodule.getName() + ".csv";
    Path csvPath = csvFolder.resolve(csvFileName);
    Csv csv = new Csv(csvPath);
    Path pomFile = submodule.getPom();
    PomFile pom = new PomFile(pomFile, system, session);
    List<Dependency> deps = pom.getDependencies();
    System.out.println(deps);

    int totalBreakingChanges = 0;

    outer:
    for (Dependency dep : deps) {

      DependencyUpdate update = new DependencyUpdate(dep, system, session);

      try {
        // PART 1: Get the latest minor version
        Dependency latestMinor = update.getLatestMinorVersion();
        LOGGER.info("Latest minor version: " + latestMinor);

        // PART 2: Analyze breaking changes between current and latest minor version
        LOGGER.info("Analyzing breaking changes between versions...");

        List<BreakingChange> breakingChanges =
            breakingChangeAnalyzer.analyzeBreakingChanges(dep, latestMinor);

        if (breakingChanges.isEmpty()) {
          LOGGER.info("No breaking changes detected.");
          continue outer;
        }

        LOGGER.info("Found " + breakingChanges.size() + " breaking changes:");

        for (BreakingChange change : breakingChanges) {
          csv.writeBreakingChange(dep, latestMinor, change);
          totalBreakingChanges++;
        }

      } catch (VersionRangeResolutionException e) {
        LOGGER.log(Level.SEVERE, "Version range resolution failed", e);

      } catch (InvalidVersionSpecificationException e) {
        LOGGER.log(Level.SEVERE, "Invalid version specification", e);

      } catch (NoDependencyUpdateException e) {
        LOGGER.warning("No update available for dependency: " + dep);
      }
    }

    LOGGER.info(
        "=== Analysis Complete for submodule '"
            + "' ===\n"
            + "Total breaking changes found: "
            + totalBreakingChanges
            + "\n"
            + "CSV report saved to: "
            + csv.getPath().toAbsolutePath());
  }
}
