package com.example.normalisation;

import com.example.Repo;
import com.example.SubModule;
import com.example.csv.ClientSymbolCsvWriter;
import com.example.pom.PomException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Script {
  public static final Path csvFolder =
      Paths.get("C:\\Users\\Alvari\\Documents\\UNI\\softeng_700\\test-results");

  private static final Path reposFolder =
      Paths.get("C:\\Users\\Alvari\\Documents\\UNI\\softeng_700\\test-repos");

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
        LOGGER.severe("No submodules found for repo: " + repo.getName());
        continue;
      }

      inner:
      for (SubModule submodule : subModules) {

        if (!resultExists(submodule)) {
          continue inner;
        }

        // delete old csv ONLY for the symbol uses csv.
        deleteOldCsv(submodule);

        LOGGER.info(
            "\n=== Starting Analysis  for submodule '"
                + submodule.getName()
                + "'\n at "
                + submodule.getDir().toAbsolutePath());
        try {

          performSubmoduleAnalysis(submodule);
          LOGGER.info("\n=== Analysis Complete for submodule '" + submodule.getName() + "' ===\n");
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

    LOGGER.info("\n\n========== ALL ANALYSIS COMPLETE ==========");
    LOGGER.info("Check output folders in: " + csvFolder.toAbsolutePath());
    LOGGER.info("============================================\n");
  }

  private static void deleteOldCsv(SubModule submodule) throws IOException {
    Path clientSymbolCsv = getClientSymbolPath(submodule);

    Files.delete(clientSymbolCsv);
  }

  private static boolean resultExists(SubModule submodule) {
    Path clientSymbolCsv = getClientSymbolPath(submodule);

    return Files.exists(clientSymbolCsv);
  }

  private static Path getClientSymbolPath(SubModule submodule) {
    String submoduleFullName = submodule.getRepo().getName() + "_" + submodule.getName();
    Path csv = Path.of(submoduleFullName).resolve(submoduleFullName + "-client-symbol-uses.csv");
    Path clientSymbolCsv = csvFolder.resolve(csv);

    return clientSymbolCsv;
  }

  private static void performSubmoduleAnalysis(SubModule submodule)
      throws IOException, PomException {

    // Create dedicated output folder for this submodule
    String submoduleFolderName = submodule.getRepo().getName() + "_" + submodule.getName();
    Path submoduleOutputFolder = csvFolder.resolve(submoduleFolderName);
    Files.createDirectories(submoduleOutputFolder);

    ClientSymbolCsvWriter symbolWriter =
        new ClientSymbolCsvWriter(submodule, submoduleOutputFolder, "-client-symbol-uses");

    // find all the symbols for later normalisation
    NormalisationAnalysis normalisationAnalysis = new NormalisationAnalysis(submodule);

    Set<Symbol> clientSymbols = normalisationAnalysis.getAllDependencySymbolUses();

    symbolWriter.writeAllSymbols(clientSymbols);

    symbolWriter.close();
  }
}
