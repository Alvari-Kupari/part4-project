package nz.ac.auckland.dee.gradestyle.config;

import java.nio.file.Path;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.PropertiesBuilderParameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class Config {
  public static Config parse(String[] args) {
    if (args.length != 1) {
      System.err.println("Missing config file argument.");
      return null;
    }

    try {
      return createConfig(args[0]);
    } catch (ConfigurationException e) {
      System.err.println("Invalid config file: " + args[0]);
    }

    return null;
  }

  private static Config createConfig(String filename) throws ConfigurationException {

    PropertiesBuilderParameters params =
        new Parameters()
            .properties()
            .setFileName(filename)
            .setListDelimiterHandler(new DefaultListDelimiterHandler(','));

    Configuration config =
        new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
            .configure(params)
            .getConfiguration();

    Path parent = Path.of(filename).toAbsolutePath().getParent();
    Path repos = resolveOptionalPath(parent, config.getString("repos"), null);
    String packageString = config.getString("package");

    Path parentTemplate = repos != null ? repos : parent;
    Path defaultTemplate = repos != null ? repos.resolve(repos.getFileName()) : null;
    Path template =
        resolveOptionalPath(parentTemplate, config.getString("template"), defaultTemplate);

    boolean templateIgnoreViolations = config.getBoolean("template.ignoreViolations", false);


    boolean github = config.getBoolean("github", false);
    String githubToken = config.getString("github.token");
    String githubClassroom = config.getString("github.classroom");
    String githubAssignment = config.getString("github.assignment");
    boolean githubFeedback = config.getBoolean("github.feedback", false);



    return new Config(
        repos,
        packageString,
        template,
        templateIgnoreViolations,
        github,
        githubToken,
        githubClassroom,
        githubAssignment,
        github
);
  }

  private static Path resolveOptionalPath(Path path, String other, Path fallback) {
    if (other == null) {
      return fallback;
    }

    if (Path.of(other).isAbsolute()) {
      return Path.of(other);
    }

    return path.resolve(other);
  }



  

  private Path repos;

  private String packageString;

  private Path templateRepo;

  private boolean templateIgnoreViolations;

  private boolean github;

  private String githubToken;

  private String githubClassroom;

  private String githubAssignment;

  private boolean githubFeedback;


  private Config(
      Path repos,
      String packageString,
      Path templateRepo,
      boolean templateIgnoreViolations,
      boolean github,
      String githubToken,
      String githubClassroom,
      String githubAssignment,
      boolean githubFeedback
) {
    this.repos = repos;
    this.packageString = packageString;
    this.templateRepo = templateRepo;
    this.templateIgnoreViolations = templateIgnoreViolations;
    this.github = github;
    this.githubToken = githubToken;
    this.githubClassroom = githubClassroom;
    this.githubAssignment = githubAssignment;
    this.githubFeedback = githubFeedback;
  }

  public Path getRepos() {
    return repos;
  }

  public String getPackage() {
    return packageString;
  }

  public Path getTemplateRepo() {
    return templateRepo;
  }

  public boolean getTemplateIgnoreViolations() {
    return templateIgnoreViolations;
  }


  public boolean getGithub() {
    return github;
  }

  public String getGithubToken() {
    return githubToken;
  }

  public String getGithubClassroom() {
    return githubClassroom;
  }

  public String getGithubAssignment() {
    return githubAssignment;
  }

  public boolean getGithubFeedback() {
    return githubFeedback;
  }



  

}
