package nz.ac.auckland.dee.gradestyle.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.sourceforge.pmd.PMDConfiguration;
import nz.ac.auckland.dee.gradestyle.Repo;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.*;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.*;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;

public class MavenReader {

  private static final String CENTRAL_REPO_URL = "https://repo.maven.apache.org/maven2";

  private static final RepositorySystem system = newRepositorySystem();
  private static final RepositorySystemSession session = newRepositorySystemSession(system);

  private static Set<String> cachedJars = new HashSet<>();
  private static URLClassLoader urlClassLoader;

  public static void configureMavenDependencies(Repo repo, PMDConfiguration configuration)
      throws Exception {

    List<Dependency> dependencies = getDependenciesFromPom(FileUtils.getPomFilePath(repo));
    List<File> resolvedFiles = resolveDependencies(system, session, dependencies);

    ClassLoader classLoader = createClassLoader(resolvedFiles);
    configuration.setClassLoader(classLoader);
  }

  private static RepositorySystem newRepositorySystem() {
    DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
    locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
    locator.addService(
        org.eclipse.aether.spi.connector.transport.TransporterFactory.class,
        FileTransporterFactory.class);
    locator.addService(
        org.eclipse.aether.spi.connector.transport.TransporterFactory.class,
        HttpTransporterFactory.class);
    return locator.getService(RepositorySystem.class);
  }

  private static RepositorySystemSession newRepositorySystemSession(RepositorySystem system) {
    DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

    // Use a custom local repository path inside the tool's working directory
    String localRepoPath = System.getProperty("user.dir") + "/dependencies";
    File localRepoDir = new File(localRepoPath);

    if (!localRepoDir.exists()) {
      boolean created = localRepoDir.mkdirs();
      if (!created) {
        throw new IllegalStateException(
            "Failed to create local repository directory: " + localRepoPath);
      }
    }

    LocalRepository localRepo = new LocalRepository(localRepoPath);
    session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
    return session;
  }

  private static List<Dependency> getDependenciesFromPom(String pomPath) throws Exception {
    MavenXpp3Reader reader = new MavenXpp3Reader();
    try (FileReader fileReader = new FileReader(pomPath)) {
      Model model = reader.read(fileReader);
      return model.getDependencies().stream()
          .map(
              dep ->
                  new Dependency(
                      new DefaultArtifact(
                          dep.getGroupId(),
                          dep.getArtifactId(),
                          dep.getClassifier(),
                          dep.getType(),
                          dep.getVersion()),
                      dep.getScope()))
          .collect(Collectors.toList());
    }
  }

  private static List<File> resolveDependencies(
      RepositorySystem system, RepositorySystemSession session, List<Dependency> dependencies)
      throws DependencyResolutionException {
    List<File> resolvedFiles = new ArrayList<>();
    for (Dependency dependency : dependencies) {

      if (cachedJars.contains(dependency.getArtifact().toString())) {
        System.out.println("Cached depedency found: " + dependency.getArtifact().toString());
        continue;
      }
      try {
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(dependency);
        collectRequest.addRepository(
            new RemoteRepository.Builder("central", "default", CENTRAL_REPO_URL).build());

        DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, null);
        DependencyResult dependencyResult = system.resolveDependencies(session, dependencyRequest);

        resolvedFiles.addAll(
            dependencyResult.getArtifactResults().stream()
                .map(artifactResult -> artifactResult.getArtifact().getFile())
                .collect(Collectors.toList()));

        // cache the depedency so it doesn't get downloaded again during this run
        cachedJars.add(dependency.getArtifact().toString());
      } catch (DependencyResolutionException e) {
        continue;
      }
    }
    return resolvedFiles;
  }

  private static ClassLoader createClassLoader(List<File> files) throws Exception {
    URL[] urls =
        files.stream()
            .map(
                file -> {
                  try {
                    return file.toURI().toURL();
                  } catch (Exception e) {
                    throw new RuntimeException("Failed to convert file to URL: " + file, e);
                  }
                })
            .toArray(URL[]::new);
    urlClassLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
    return urlClassLoader;
  }

  public static void clearDependencies() {
    // Use a custom local repository path inside the tool's working directory
    String localRepoPath = System.getProperty("user.dir") + "/dependencies";
    File localRepoDir = new File(localRepoPath);

    if (localRepoDir.exists()) {
      try {
        deleteDirectoryRecursively(localRepoDir);
        System.out.println("Dependencies cleared successfully.");
      } catch (IOException e) {
        System.err.println("Failed to clear dependencies: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  private static void deleteDirectoryRecursively(File directory) throws IOException {
    if (directory.isDirectory()) {
      // Recursively delete all files and subdirectories
      for (File file : directory.listFiles()) {
        deleteDirectoryRecursively(file);
      }
    }
    // Delete the directory or file itself
    Files.delete(directory.toPath());
  }
}
