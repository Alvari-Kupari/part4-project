package com.example.breakingchange;

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
   * @throws BreakingChangeAnalysisException if analysis fails
   */
  public List<BreakingChange> analyzeBreakingChanges(
      Dependency oldDependency, Dependency newDependency) throws BreakingChangeAnalysisException {

    try {
      // Validate inputs
      validateDependencies(oldDependency, newDependency);

      // Resolve transitive dependencies for both versions
      List<File> oldJars = resolveTransitiveDependencies(oldDependency);
      List<File> newJars = resolveTransitiveDependencies(newDependency);

      // Check if we have any JARs to compare
      // Note: Empty lists could be valid if dependency has no JAR artifacts (e.g., POM-only dependencies)
      // We should only fail if we absolutely cannot find the main JAR files
      validateResolvedJars(oldJars, newJars, oldDependency, newDependency);

      return compareJarFiles(oldJars, newJars, oldDependency, newDependency);

    } catch (BreakingChangeAnalysisException e) {
      // Re-throw our custom exceptions as-is
      throw e;
    } catch (Exception e) {
      // Wrap any unexpected exceptions
      throw new BreakingChangeAnalysisException(
          String.format("Unexpected error analyzing breaking changes between %s and %s", 
              formatDependency(oldDependency), formatDependency(newDependency)), e);
    }
  }

  /** Validates that the input dependencies are not null and have valid artifacts. */
  private void validateDependencies(Dependency oldDependency, Dependency newDependency) 
      throws BreakingChangeAnalysisException {
    if (oldDependency == null) {
      throw new BreakingChangeAnalysisException("Old dependency cannot be null");
    }
    if (newDependency == null) {
      throw new BreakingChangeAnalysisException("New dependency cannot be null");
    }
    if (oldDependency.getArtifact() == null) {
      throw new BreakingChangeAnalysisException("Old dependency artifact cannot be null");
    }
    if (newDependency.getArtifact() == null) {
      throw new BreakingChangeAnalysisException("New dependency artifact cannot be null");
    }
    
    // Verify they are the same library (groupId:artifactId)
    String oldLibrary = getLibraryName(oldDependency);
    String newLibrary = getLibraryName(newDependency);
    if (!oldLibrary.equals(newLibrary)) {
      throw new BreakingChangeAnalysisException(
          String.format("Cannot compare different libraries: %s vs %s", oldLibrary, newLibrary));
    }
  }

  /** 
   * Validates that we have sufficient JAR files to perform a meaningful comparison.
   * This method handles edge cases where dependencies might not have JAR artifacts.
   */
  private void validateResolvedJars(List<File> oldJars, List<File> newJars, 
      Dependency oldDependency, Dependency newDependency) throws BreakingChangeAnalysisException {
    
    // First check if both lists are empty - this could be valid for POM-only dependencies
    if (oldJars.isEmpty() && newJars.isEmpty()) {
      LOGGER.info(String.format("Both versions have no JAR artifacts for %s - skipping analysis", 
          getLibraryName(oldDependency)));
      return; // This is not an error - some dependencies are POM-only
    }
    
    // If only one version has JARs, this indicates an issue
    if (oldJars.isEmpty() && !newJars.isEmpty()) {
      throw new BreakingChangeAnalysisException(
          String.format("Failed to resolve any JAR files for old dependency: %s:%s (new version resolved %d JARs)",
              getLibraryName(oldDependency), oldDependency.getArtifact().getVersion(), newJars.size()));
    }
    
    if (!oldJars.isEmpty() && newJars.isEmpty()) {
      throw new BreakingChangeAnalysisException(
          String.format("Failed to resolve any JAR files for new dependency: %s:%s (old version resolved %d JARs)",
              getLibraryName(newDependency), newDependency.getArtifact().getVersion(), oldJars.size()));
    }
    
    // At this point, both have JARs - verify we can find the main JARs
    File oldMainJar = findMainJar(oldJars, oldDependency);
    File newMainJar = findMainJar(newJars, newDependency);
    
    if (oldMainJar == null) {
      throw new BreakingChangeAnalysisException(
          String.format("Could not find main JAR file for old dependency: %s:%s (resolved %d JARs)",
              getLibraryName(oldDependency), oldDependency.getArtifact().getVersion(), oldJars.size()));
    }
    
    if (newMainJar == null) {
      throw new BreakingChangeAnalysisException(
          String.format("Could not find main JAR file for new dependency: %s:%s (resolved %d JARs)",
              getLibraryName(newDependency), newDependency.getArtifact().getVersion(), newJars.size()));
    }
  }

  /** 
   * Downloads the JAR file for a given dependency.
   * @throws BreakingChangeAnalysisException if download fails
   */
  /** 
   * Downloads the JAR file for a given dependency.
   * @throws BreakingChangeAnalysisException if download fails
   */
  private File downloadJarFile(Dependency dependency) throws BreakingChangeAnalysisException {
    try {
      Artifact artifact = dependency.getArtifact();
      ArtifactRequest request = new ArtifactRequest();
      request.setArtifact(artifact);
      request.setRepositories(repositories);

      ArtifactResult result = repositorySystem.resolveArtifact(session, request);
      File jarFile = result.getArtifact().getFile();

      if (jarFile == null || !jarFile.exists()) {
        throw new BreakingChangeAnalysisException("Downloaded JAR file does not exist: " + artifact);
      }

      if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
        LOGGER.info(String.format("Downloaded: %s -> %s", artifact, jarFile.getAbsolutePath()));
      }
      return jarFile;

    } catch (ArtifactResolutionException e) {
      throw new BreakingChangeAnalysisException("Failed to download dependency: " + dependency + ". Error: " + e.getMessage(), e);
    }
  }

  /**
   * Resolves and downloads all transitive dependencies for a given dependency.
   *
   * @param dependency The dependency to resolve transitively
   * @return List of jar files for the dependency and all its transitive dependencies (may be empty for POM-only dependencies)
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
          } else {
            LOGGER.warning("JAR file not found for artifact: " + artifact);
          }
        }
      }

      if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
        LOGGER.info(
            String.format(
                "Resolved %d transitive dependencies for %s",
                jarFiles.size(), dependency.getArtifact()));
      }

      // If we couldn't resolve any transitive dependencies, try to at least get the main JAR
      if (jarFiles.isEmpty()) {
        LOGGER.info("No transitive dependencies resolved, attempting to download main JAR only for: " + dependency);
        try {
          File mainJar = downloadJarFile(dependency);
          jarFiles.add(mainJar);
          LOGGER.info("Successfully downloaded main JAR as fallback: " + mainJar.getName());
        } catch (BreakingChangeAnalysisException e) {
          // Log the warning but don't fail - this could be a POM-only dependency
          LOGGER.warning("Failed to download main JAR for " + dependency + ": " + e.getMessage());
          // Return empty list - will be handled by validateResolvedJars
        }
      }

    } catch (DependencyResolutionException e) {
      // Try fallback to main JAR only if transitive resolution fails
      LOGGER.warning(
          "Failed to resolve transitive dependencies for "
              + dependency
              + ". Attempting to download main JAR only. Error: "
              + e.getMessage());

      try {
        File mainJar = downloadJarFile(dependency);
        jarFiles.add(mainJar);
        LOGGER.info("Successfully downloaded main JAR as fallback: " + mainJar.getName());
      } catch (BreakingChangeAnalysisException fallbackException) {
        // Log the warning but don't fail here - let validateResolvedJars handle it
        LOGGER.warning("Failed to resolve transitive dependencies and main JAR for " + dependency + 
            ". Original error: " + e.getMessage() + 
            ". Fallback error: " + fallbackException.getMessage());
        // Return empty list - will be handled by validateResolvedJars
      }
    }

    return jarFiles;
  }

  /** 
   * Compares two sets of JAR files using JAPICMP and extracts breaking changes. 
   * @throws BreakingChangeAnalysisException if comparison fails
   */
  private List<BreakingChange> compareJarFiles(
      List<File> oldJars, List<File> newJars, Dependency oldDependency, Dependency newDependency) 
      throws BreakingChangeAnalysisException {

    // Handle edge case where both versions have no JARs (POM-only dependencies)
    if (oldJars.isEmpty() && newJars.isEmpty()) {
      LOGGER.info(String.format("Both versions are POM-only for %s - no breaking changes to analyze", 
          getLibraryName(oldDependency)));
      return new ArrayList<>();
    }

    try {
      // Set up JAPICMP archives
      List<JApiCmpArchive> oldArchives = new ArrayList<>();
      List<JApiCmpArchive> newArchives = new ArrayList<>();

      // Find main JAR files - validation already ensured these exist
      File oldMainJar = findMainJar(oldJars, oldDependency);
      File newMainJar = findMainJar(newJars, newDependency);

      oldArchives.add(new JApiCmpArchive(oldMainJar, oldDependency.getArtifact().getVersion()));
      newArchives.add(new JApiCmpArchive(newMainJar, newDependency.getArtifact().getVersion()));

      // Perform the comparison
      return performComparison(
          oldArchives, newArchives, oldJars, newJars, oldDependency, newDependency);

    } catch (Exception e) {
      LOGGER.severe("Error comparing JAR files: " + e.getMessage());
      throw new BreakingChangeAnalysisException("Failed to compare JAR files: " + e.getMessage(), e);
    }
  }

  /** 
   * Performs japicmp comparison between two sets of archives. 
   * @throws BreakingChangeAnalysisException if comparison fails
   */
  private List<BreakingChange> performComparison(
      List<JApiCmpArchive> oldArchives,
      List<JApiCmpArchive> newArchives,
      List<File> oldJars,
      List<File> newJars,
      Dependency oldDependency,
      Dependency newDependency) throws BreakingChangeAnalysisException {
    
    try {
      List<BreakingChange> breakingChanges = new ArrayList<>();

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

      if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
        LOGGER.info(
            String.format(
                "Using classpath with %d entries for JAR comparison",
                options.getClassPathEntries().size()));
      }

      // Create comparator and run comparison
      JarArchiveComparator comparator = new JarArchiveComparator(options);
      List<JApiClass> jApiClasses;
      
      try {
        jApiClasses = comparator.compare(oldArchives, newArchives);
      } catch (Exception e) {
        throw new BreakingChangeAnalysisException(
            String.format("JAPICMP comparison failed for %s", getLibraryName(oldDependency)), e);
      }

      // Extract breaking changes from results
      String libraryName = getLibraryName(oldDependency);
      String oldVersion = oldDependency.getArtifact().getVersion();
      String newVersion = newDependency.getArtifact().getVersion();

      for (JApiClass jApiClass : jApiClasses) {
        breakingChanges.addAll(
            extractBreakingChangesFromClass(jApiClass, oldDependency, newDependency));
      }

      if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
        LOGGER.info(
            String.format(
                "Found %d breaking changes between %s and %s of %s",
                breakingChanges.size(), oldVersion, newVersion, libraryName));
      }

      return breakingChanges;
      
    } catch (BreakingChangeAnalysisException e) {
      // Re-throw our custom exceptions
      throw e;
    } catch (Exception e) {
      // Wrap any unexpected exceptions
      throw new BreakingChangeAnalysisException(
          String.format("Unexpected error during JAR comparison for %s", getLibraryName(oldDependency)), e);
    }
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
      JApiClass jApiClass, Dependency 
      oldDependency, Dependency newDependency) {
    List<BreakingChange> changes = new ArrayList<>();

    String libraryName = getLibraryName(oldDependency);
    String oldVersion = oldDependency.getArtifact().getVersion();
    String newVersion = newDependency.getArtifact().getVersion();

    // Check class-level changes
    if (hasBreakingChanges(jApiClass.getCompatibilityChanges())
        && isActuallyBreaking(jApiClass.isBinaryCompatible(), jApiClass.isSourceCompatible())) {
      changes.add(
          BreakingChange.builder()
              .className(jApiClass.getFullyQualifiedName())
              .memberName(jApiClass.getFullyQualifiedName())
              .changeType("CLASS_CHANGE")
              .description(getChangeDescription(jApiClass.getCompatibilityChanges()))
              .oldDependency(oldDependency)
              .newDependency(newDependency)
              .isBinaryCompatible(jApiClass.isBinaryCompatible())
              .isSourceCompatible(jApiClass.isSourceCompatible())
              .isTransitive(false)
              .depth(1)
              .directParentDependency(oldDependency)
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
                .oldDependency(oldDependency)
                .newDependency(newDependency)
                .isBinaryCompatible(method.isBinaryCompatible())
                .isSourceCompatible(method.isSourceCompatible())
                .isTransitive(false)
                .depth(1)
                .directParentDependency(oldDependency)
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
                .oldDependency(oldDependency)
                .newDependency(newDependency)
                .isBinaryCompatible(field.isBinaryCompatible())
                .isSourceCompatible(field.isSourceCompatible())
                .isTransitive(false)
                .depth(1)
                .directParentDependency(oldDependency)
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
                .memberName(constructor.getName())
                .changeType("CONSTRUCTOR_CHANGE")
                .description(getChangeDescription(constructor.getCompatibilityChanges()))
                .oldDependency(oldDependency)
                .newDependency(newDependency)
                .isBinaryCompatible(constructor.isBinaryCompatible())
                .isSourceCompatible(constructor.isSourceCompatible())
                .isTransitive(false)
                .depth(1)
                .directParentDependency(oldDependency)
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

  /** Formats a dependency as groupId:artifactId:version for logging. */
  private String formatDependency(Dependency dependency) {
    if (dependency == null || dependency.getArtifact() == null) {
      return "null";
    }
    Artifact artifact = dependency.getArtifact();
    return artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion();
  }
}
