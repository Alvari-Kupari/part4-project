package com.example.normalisation;

import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;
import com.example.depanalyzer.analyzer.dependencycollection.Repositories;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.zip.ZipException;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator;

public class DependencyDatabase {

  private static final RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
  private static final RepositorySystemSession session = RepositorySystemFactory.newSession(system);
  private static final Logger LOGGER = Logger.getLogger(DependencyDatabase.class.getName());

  private final Map<String, Artifact> fqnToArtifactDirect = new HashMap<>();
  private final Map<String, Artifact> fqnToArtifactTransitive = new HashMap<>();
  private final String rootPackage;
  private List<RemoteRepository> repositories;

  /**
   * Constructs the database from a set of artifacts. Each artifact should have an associated JAR
   * file.
   *
   * @param artifacts Set of artifacts to index
   * @throws IOException if a JAR cannot be read
   */
  public DependencyDatabase(String rootPackage, Collection<Dependency> deps) throws IOException {
    this.rootPackage = rootPackage;
    this.repositories = Repositories.repositories;

    for (Dependency dependency : deps) {
      try {
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(dependency);
        collectRequest.setRepositories(repositories);

        DependencyRequest dependencyRequest = new DependencyRequest();
        dependencyRequest.setCollectRequest(collectRequest);

        DependencyResult dependencyResult = system.resolveDependencies(session, dependencyRequest);

        PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
        dependencyResult.getRoot().accept(nlg);

        // Skip the root dependency (which is the direct dependency itself)
        List<Dependency> dependencies = nlg.getDependencies(false);

        for (int i = 0; i < dependencies.size(); i++) {
          Dependency dep = dependencies.get(i);

          // direct dep
          if (i == 0) {
            indexDependency(dep, false);
          }
          // transitive
          else {
            indexDependency(dep, true);
          }
        }

      } catch (DependencyResolutionException e) {
        LOGGER.warning(
            String.format(
                "Failed to resolve transitive dependencies for %s: %s",
                dependency, e.getMessage()));
      }
    }
    // for (Artifact artifact : artifacts) {
    //   File jarFile = artifact.getFile();
    //   if (jarFile != null && jarFile.exists()) {
    //     indexJar(jarFile, artifact);
    //   }
    // }
  }

  /**
   * Returns the Artifact for a given fully qualified class name.
   *
   * @param fullyQualifiedName fully qualified name (e.g., "com.example.MyClass")
   * @return Artifact, or null if not found
   */
  public ArtifactWrapper getArtifact(String fqn) {

    Artifact artifact = fqnToArtifactDirect.get(fqn);

    if (artifact != null) {
      return new ArtifactWrapper(artifact, false);
    } else {
      artifact = fqnToArtifactTransitive.get(fqn);

      if (artifact != null) {
        return new ArtifactWrapper(artifact, true);
      }
    }

    // JDK internal
    if (fqn.startsWith("java.") || fqn.startsWith("javax.") || fqn.startsWith("jdk.")) {
      return new ArtifactWrapper(new DefaultArtifact("JDK", "java.base", "N/A", "N/A"), false);
    }

    // Client internal
    if (rootPackage != null && fqn.startsWith(rootPackage + ".")) {
      return new ArtifactWrapper(new DefaultArtifact("CLIENT", "client-code", "N/A", "N/A"), false);
    }

    return new ArtifactWrapper(null, false);
  }

  private void indexDependency(Dependency dep, boolean isTransitive) throws IOException {
    Artifact artifact = dep.getArtifact();

    // Only include jar artifacts
    // if ("jar".equals(artifact.getExtension())) {
    File jarFile = artifact.getFile();
    if (jarFile != null && jarFile.exists()) {
      indexJar(jarFile, artifact, isTransitive);
    }
    // }
  }

  private void indexJar(File jarFile, Artifact artifact, boolean isTransitive) throws IOException {
    try (JarFile jar = new JarFile(jarFile)) {
      Enumeration<JarEntry> entries = jar.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        if (entry.getName().endsWith(".class") && !entry.isDirectory()) {
          String classFqn =
              entry.getName().replace('/', '.').replace('\\', '.').replaceAll("\\.class$", "");

          // Collapse inner classes to their outer class
          int dollarIndex = classFqn.indexOf('$');
          if (dollarIndex != -1) {
            classFqn = classFqn.substring(0, dollarIndex);
          }

          if (isTransitive) {
            fqnToArtifactTransitive.put(classFqn, artifact);
          } else {
            fqnToArtifactDirect.put(classFqn, artifact);
          }
        }
      }
    } catch (ZipException e) {
      System.err.println("SEVERE: Failed to read JAR file: " + jarFile.getAbsolutePath());
    }
  }
}
