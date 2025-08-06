package com.example.stage1.breakingchange;

import com.example.depanalyzer.analyzer.dependencycollection.Repositories;
import japicmp.cmp.JApiCmpArchive;
import japicmp.cmp.JarArchiveComparator;
import japicmp.cmp.JarArchiveComparatorOptions;
import japicmp.model.JApiClass;
import japicmp.model.JApiCompatibilityChange;
import japicmp.model.JApiConstructor;
import japicmp.model.JApiField;
import japicmp.model.JApiMethod;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator;

/** Analyzes breaking changes between two versions of a dependency using JAPICMP. */
public class BreakingChangeAnalyzer {

  private static final Logger LOGGER = Logger.getLogger(BreakingChangeAnalyzer.class.getName());

  private final RepositorySystem repositorySystem;
  private final RepositorySystemSession session;
  private final List<RemoteRepository> repositories;

  public BreakingChangeAnalyzer(
      RepositorySystem repositorySystem, RepositorySystemSession session) {
    this.repositorySystem = repositorySystem;
    this.session = session;
    this.repositories = Repositories.repositories;
  }

  /**
   * Analyzes breaking changes between old and new versions of a dependency.
   *
   * @param oldDependency The old version of the dependency
   * @param newDependency The new version of the dependency
   * @return List of breaking changes detected
   * @throws Exception if analysis fails
   */
  public List<BreakingChange> analyzeBreakingChanges(
      Dependency oldDependency, Dependency newDependency) {

    // Resolve transitive dependencies for both versions
    List<File> oldJars = resolveTransitiveDependencies(oldDependency);
    List<File> newJars = resolveTransitiveDependencies(newDependency);

    if (oldJars.isEmpty() || newJars.isEmpty()) {
      LOGGER.warning("Failed to resolve dependencies for comparison");
      return new ArrayList<>();
    }

    return compareJarFiles(oldJars, newJars, oldDependency, newDependency);
  }

  /** Downloads the JAR file for a given dependency. */
  private File downloadJarFile(Dependency dependency) {
    try {
      Artifact artifact = dependency.getArtifact();
      ArtifactRequest request = new ArtifactRequest();
      request.setArtifact(artifact);
      request.setRepositories(repositories);

      ArtifactResult result = repositorySystem.resolveArtifact(session, request);
      File jarFile = result.getArtifact().getFile();

      if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
        LOGGER.info(String.format("Downloaded: %s -> %s", artifact, jarFile.getAbsolutePath()));
      }
      return jarFile;

    } catch (ArtifactResolutionException e) {
      LOGGER.severe("Failed to download dependency: " + dependency + ". Error: " + e.getMessage());
      return null;
    }
  }

  /**
   * Resolves and downloads all transitive dependencies for a given dependency.
   *
   * @param dependency The dependency to resolve transitively
   * @return List of jar files for the dependency and all its transitive dependencies
   */
  private List<File> resolveTransitiveDependencies(Dependency dependency) {
    List<File> jarFiles = new ArrayList<>();

    try {
      // Create a dependency request to resolve transitively
      org.eclipse.aether.collection.CollectRequest collectRequest =
          new org.eclipse.aether.collection.CollectRequest();
      collectRequest.setRoot(dependency);
      collectRequest.setRepositories(repositories);

      DependencyRequest dependencyRequest = new DependencyRequest();
      dependencyRequest.setCollectRequest(collectRequest);

      // Resolve all transitive dependencies
      DependencyResult dependencyResult =
          repositorySystem.resolveDependencies(session, dependencyRequest);

      // Use PreorderNodeListGenerator to collect all dependencies
      PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
      dependencyResult.getRoot().accept(nlg);

      // Download all dependencies
      for (Artifact artifact : nlg.getArtifacts(false)) {
        // Skip non-jar artifacts
        if ("jar".equals(artifact.getExtension())) {
          File jarFile = artifact.getFile();
          if (jarFile != null && jarFile.exists()) {
            jarFiles.add(jarFile);
            if (LOGGER.isLoggable(java.util.logging.Level.FINE)) {
              LOGGER.fine("Added transitive dependency: " + artifact);
            }
          }
        }
      }

      // Additionally try to resolve kotlin standard library if needed
      tryAddKotlinStandardLibrary(jarFiles);

      if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
        LOGGER.info(
            String.format(
                "Resolved %d transitive dependencies for %s",
                jarFiles.size(), dependency.getArtifact()));
      }

    } catch (DependencyResolutionException e) {
      LOGGER.warning(
          "Failed to resolve transitive dependencies for "
              + dependency
              + ". Falling back to single JAR analysis. Error: "
              + e.getMessage());

      // Fallback: just add the main JAR file
      File mainJar = downloadJarFile(dependency);
      if (mainJar != null) {
        jarFiles.add(mainJar);
      }
    }

    return jarFiles;
  }

  /**
   * Attempts to add Kotlin standard library to the classpath if it's not already present. This is
   * needed because some libraries (like JUnit platform commons) use Kotlin internally.
   */
  private void tryAddKotlinStandardLibrary(List<File> jarFiles) {
    // Check if kotlin stdlib is already in the list
    boolean hasKotlinStdlib =
        jarFiles.stream().anyMatch(jar -> jar.getName().contains("kotlin-stdlib"));

    if (!hasKotlinStdlib) {
      try {
        // Try to resolve kotlin-stdlib
        org.eclipse.aether.artifact.DefaultArtifact kotlinStdlib =
            new org.eclipse.aether.artifact.DefaultArtifact(
                "org.jetbrains.kotlin", "kotlin-stdlib", "jar", "1.9.10");

        ArtifactRequest request = new ArtifactRequest();
        request.setArtifact(kotlinStdlib);
        request.setRepositories(repositories);

        ArtifactResult result = repositorySystem.resolveArtifact(session, request);
        File kotlinJar = result.getArtifact().getFile();

        if (kotlinJar != null && kotlinJar.exists()) {
          jarFiles.add(kotlinJar);
          LOGGER.info("Added Kotlin standard library to classpath: " + kotlinJar.getAbsolutePath());
        }

      } catch (ArtifactResolutionException e) {
        LOGGER.fine("Could not resolve Kotlin standard library: " + e.getMessage());
      }
    }

    // Also try to add common optional dependencies
    tryAddOptionalDependency(jarFiles, "org.osgi", "org.osgi.framework", "1.10.0");
  }

  /** Attempts to add an optional dependency to avoid missing class errors. */
  private void tryAddOptionalDependency(
      List<File> jarFiles, String groupId, String artifactId, String version) {
    // Check if the dependency is already present
    boolean hasLibrary = jarFiles.stream().anyMatch(jar -> jar.getName().contains(artifactId));

    if (!hasLibrary) {
      try {
        org.eclipse.aether.artifact.DefaultArtifact artifact =
            new org.eclipse.aether.artifact.DefaultArtifact(groupId, artifactId, "jar", version);

        ArtifactRequest request = new ArtifactRequest();
        request.setArtifact(artifact);
        request.setRepositories(repositories);

        ArtifactResult result = repositorySystem.resolveArtifact(session, request);
        File jarFile = result.getArtifact().getFile();

        if (jarFile != null && jarFile.exists()) {
          jarFiles.add(jarFile);
          LOGGER.fine("Added optional dependency to classpath: " + jarFile.getAbsolutePath());
        }

      } catch (ArtifactResolutionException e) {
        LOGGER.fine(
            "Could not resolve optional dependency "
                + groupId
                + ":"
                + artifactId
                + ": "
                + e.getMessage());
      }
    }
  }

  /** Compares two sets of JAR files using JAPICMP and extracts breaking changes. */
  private List<BreakingChange> compareJarFiles(
      List<File> oldJars, List<File> newJars, Dependency oldDependency, Dependency newDependency) {

    List<BreakingChange> breakingChanges = new ArrayList<>();

    try {
      // Set up JAPICMP archives
      List<JApiCmpArchive> oldArchives = new ArrayList<>();
      List<JApiCmpArchive> newArchives = new ArrayList<>();

      // Add main JAR files - these are the primary artifacts being compared
      File oldMainJar = findMainJar(oldJars, oldDependency);
      File newMainJar = findMainJar(newJars, newDependency);

      if (oldMainJar == null || newMainJar == null) {
        LOGGER.warning("Could not find main JAR files for comparison");
        return breakingChanges;
      }

      oldArchives.add(new JApiCmpArchive(oldMainJar, oldDependency.getArtifact().getVersion()));
      newArchives.add(new JApiCmpArchive(newMainJar, newDependency.getArtifact().getVersion()));

      // Try the comparison with full classpath first
      breakingChanges =
          attemptComparison(
              oldArchives, newArchives, oldJars, newJars, oldDependency, newDependency, false);

      // If that fails with missing classes, try again with ignore missing classes
      if (breakingChanges.isEmpty()) {
        LOGGER.info("Retrying comparison with ignore missing classes option");
        breakingChanges =
            attemptComparison(
                oldArchives, newArchives, oldJars, newJars, oldDependency, newDependency, true);
      }

    } catch (Exception e) {
      LOGGER.severe("Error comparing JAR files: " + e.getMessage());
      e.printStackTrace();
    }

    return breakingChanges;
  }

  /** Attempts to run japicmp comparison with the given configuration. */
  private List<BreakingChange> attemptComparison(
      List<JApiCmpArchive> oldArchives,
      List<JApiCmpArchive> newArchives,
      List<File> oldJars,
      List<File> newJars,
      Dependency oldDependency,
      Dependency newDependency,
      boolean ignoreMissingClasses) {
    List<BreakingChange> breakingChanges = new ArrayList<>();

    try {
      // Configure JAPICMP options
      JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();

      // Add all transitive dependencies to the classpath for both versions
      File oldMainJar = findMainJar(oldJars, oldDependency);
      File newMainJar = findMainJar(newJars, newDependency);

      for (File jar : oldJars) {
        if (!jar.equals(oldMainJar)) {
          options.getClassPathEntries().add(jar.getAbsolutePath());
        }
      }
      for (File jar : newJars) {
        if (!jar.equals(newMainJar)) {
          options.getClassPathEntries().add(jar.getAbsolutePath());
        }
      }

      // Set ignore missing classes option if requested
      if (ignoreMissingClasses) {
        // Try to set the ignore missing classes option via reflection
        try {
          java.lang.reflect.Method method =
              options.getClass().getMethod("setIgnoreMissingClasses", Object.class);
          // Try different enum values that might exist
          Class<?> enumClass = Class.forName("japicmp.config.Options$IgnoreMissingClasses");
          Object enumValue =
              java.lang.reflect.Array.get(enumClass.getEnumConstants(), 0); // Get first enum value
          method.invoke(options, enumValue);
          LOGGER.info("Successfully set ignore missing classes option");
        } catch (Exception e) {
          LOGGER.fine("Could not set ignore missing classes option: " + e.getMessage());
        }
      }

      if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
        LOGGER.info(
            String.format(
                "Using classpath with %d entries for JAR comparison (ignore missing: %s)",
                options.getClassPathEntries().size(), ignoreMissingClasses));
      }

      // Create comparator and run comparison
      JarArchiveComparator comparator = new JarArchiveComparator(options);
      List<JApiClass> jApiClasses = comparator.compare(oldArchives, newArchives);

      // Extract breaking changes from results
      String libraryName = getLibraryName(oldDependency);
      String oldVersion = oldDependency.getArtifact().getVersion();
      String newVersion = newDependency.getArtifact().getVersion();

      for (JApiClass jApiClass : jApiClasses) {
        breakingChanges.addAll(
            extractBreakingChangesFromClass(jApiClass, libraryName, oldVersion, newVersion));
      }

      if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
        LOGGER.info(
            String.format(
                "Found %d breaking changes between %s and %s of %s",
                breakingChanges.size(), oldVersion, newVersion, libraryName));
      }

    } catch (Exception e) {
      if (!ignoreMissingClasses
          && e.getMessage() != null
          && e.getMessage().contains("Could not load")) {
        // This is likely a missing class error, let the caller retry with ignore missing classes
        LOGGER.warning("Comparison failed due to missing classes: " + e.getMessage());
        return new ArrayList<>(); // Return empty list to signal retry needed
      } else {
        // Re-throw for other errors or if we're already ignoring missing classes
        throw new RuntimeException(e);
      }
    }

    return breakingChanges;
  }

  /** Finds the main JAR file from a list of JAR files based on the dependency artifact. */
  private File findMainJar(List<File> jars, Dependency dependency) {
    String expectedName =
        dependency.getArtifact().getArtifactId()
            + "-"
            + dependency.getArtifact().getVersion()
            + ".jar";

    for (File jar : jars) {
      if (jar.getName().equals(expectedName)) {
        return jar;
      }
    }

    // Fallback: if exact match not found, look for a JAR with the artifact ID
    String artifactId = dependency.getArtifact().getArtifactId();
    for (File jar : jars) {
      if (jar.getName().startsWith(artifactId + "-")) {
        return jar;
      }
    }

    // Last resort: return the first JAR if available
    return jars.isEmpty() ? null : jars.get(0);
  }

  /** Extracts breaking changes from a JApiClass. */
  private List<BreakingChange> extractBreakingChangesFromClass(
      JApiClass jApiClass, String libraryName, String oldVersion, String newVersion) {
    List<BreakingChange> changes = new ArrayList<>();

    // Check class-level changes
    if (hasBreakingChanges(jApiClass.getCompatibilityChanges())
        && isActuallyBreaking(jApiClass.isBinaryCompatible(), jApiClass.isSourceCompatible())) {
      changes.add(
          BreakingChange.builder()
              .className(jApiClass.getFullyQualifiedName())
              .memberName("<class>")
              .changeType("CLASS_CHANGE")
              .description(getChangeDescription(jApiClass.getCompatibilityChanges()))
              .libraryName(libraryName)
              .oldVersion(oldVersion)
              .newVersion(newVersion)
              .isBinaryCompatible(jApiClass.isBinaryCompatible())
              .isSourceCompatible(jApiClass.isSourceCompatible())
              .build());
    }

    // Check method changes
    for (JApiMethod method : jApiClass.getMethods()) {
      if (hasBreakingChanges(method.getCompatibilityChanges())
          && isActuallyBreaking(method.isBinaryCompatible(), method.isSourceCompatible())) {
        changes.add(
            BreakingChange.builder()
                .className(jApiClass.getFullyQualifiedName())
                .memberName(method.getName())
                .changeType("METHOD_CHANGE")
                .description(getChangeDescription(method.getCompatibilityChanges()))
                .libraryName(libraryName)
                .oldVersion(oldVersion)
                .newVersion(newVersion)
                .isBinaryCompatible(method.isBinaryCompatible())
                .isSourceCompatible(method.isSourceCompatible())
                .build());
      }
    }

    // Check field changes
    for (JApiField field : jApiClass.getFields()) {
      if (hasBreakingChanges(field.getCompatibilityChanges())
          && isActuallyBreaking(field.isBinaryCompatible(), field.isSourceCompatible())) {
        changes.add(
            BreakingChange.builder()
                .className(jApiClass.getFullyQualifiedName())
                .memberName(field.getName())
                .changeType("FIELD_CHANGE")
                .description(getChangeDescription(field.getCompatibilityChanges()))
                .libraryName(libraryName)
                .oldVersion(oldVersion)
                .newVersion(newVersion)
                .isBinaryCompatible(field.isBinaryCompatible())
                .isSourceCompatible(field.isSourceCompatible())
                .build());
      }
    }

    // Check constructor changes
    for (JApiConstructor constructor : jApiClass.getConstructors()) {
      if (hasBreakingChanges(constructor.getCompatibilityChanges())
          && isActuallyBreaking(
              constructor.isBinaryCompatible(), constructor.isSourceCompatible())) {
        changes.add(
            BreakingChange.builder()
                .className(jApiClass.getFullyQualifiedName())
                .memberName("<constructor>")
                .changeType("CONSTRUCTOR_CHANGE")
                .description(getChangeDescription(constructor.getCompatibilityChanges()))
                .libraryName(libraryName)
                .oldVersion(oldVersion)
                .newVersion(newVersion)
                .isBinaryCompatible(constructor.isBinaryCompatible())
                .isSourceCompatible(constructor.isSourceCompatible())
                .build());
      }
    }

    return changes;
  }

  /**
   * Checks if there are any breaking compatibility changes. Only returns true for changes that
   * actually break binary OR source compatibility.
   */
  private boolean hasBreakingChanges(List<JApiCompatibilityChange> changes) {
    // Only consider changes that break binary or source compatibility
    return changes.stream()
        .anyMatch(change -> !change.isBinaryCompatible() || !change.isSourceCompatible());
  }

  /** Additional check to ensure we only capture actual breaking changes at the element level. */
  private boolean isActuallyBreaking(boolean isBinaryCompatible, boolean isSourceCompatible) {
    return !isBinaryCompatible || !isSourceCompatible;
  }

  /** Creates a description string from compatibility changes. */
  private String getChangeDescription(List<JApiCompatibilityChange> changes) {
    StringBuilder desc = new StringBuilder();
    for (JApiCompatibilityChange change : changes) {
      if (desc.length() > 0) desc.append("; ");
      desc.append(change.getType().name());
    }
    return desc.toString();
  }

  /** Gets the library name in groupId:artifactId format. */
  private String getLibraryName(Dependency dependency) {
    Artifact artifact = dependency.getArtifact();
    return artifact.getGroupId() + ":" + artifact.getArtifactId();
  }
}
