package com.example.breakingchange;

import japicmp.cmp.JApiCmpArchive;
import japicmp.cmp.JarArchiveComparator;
import japicmp.cmp.JarArchiveComparatorOptions;
import japicmp.model.JApiClass;
import japicmp.model.JApiCompatibilityChange;
import japicmp.model.JApiMethod;
import japicmp.model.JApiField;
import japicmp.model.JApiConstructor;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.repository.RemoteRepository;
import com.example.depanalyzer.analyzer.dependencycollection.Repositories;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Analyzes breaking changes between two versions of a dependency using JAPICMP.
 */
public class BreakingChangeAnalyzer {
    
    private static final Logger LOGGER = Logger.getLogger(BreakingChangeAnalyzer.class.getName());
    
    private final RepositorySystem repositorySystem;
    private final RepositorySystemSession session;
    private final List<RemoteRepository> repositories;

    public BreakingChangeAnalyzer(RepositorySystem repositorySystem, RepositorySystemSession session) {
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
    public List<BreakingChange> analyzeBreakingChanges(Dependency oldDependency, Dependency newDependency) {
        
        // Download JAR files for both versions
        File oldJar = downloadJarFile(oldDependency);
        File newJar = downloadJarFile(newDependency);
        
        if (oldJar == null || newJar == null) {
            LOGGER.warning("Failed to download JAR files for comparison");
            return new ArrayList<>();
        }

        return compareJarFiles(oldJar, newJar, oldDependency, newDependency);
    }

    /**
     * Downloads the JAR file for a given dependency.
     */
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
     * Compares two JAR files using JAPICMP and extracts breaking changes.
     */
    private List<BreakingChange> compareJarFiles(File oldJar, File newJar, 
                                               Dependency oldDependency, Dependency newDependency) {
        
        List<BreakingChange> breakingChanges = new ArrayList<>();
        
        try {
            // Set up JAPICMP archives
            List<JApiCmpArchive> oldArchives = new ArrayList<>();
            List<JApiCmpArchive> newArchives = new ArrayList<>();
            
            oldArchives.add(new JApiCmpArchive(oldJar, oldDependency.getArtifact().getVersion()));
            newArchives.add(new JApiCmpArchive(newJar, newDependency.getArtifact().getVersion()));

            // Configure JAPICMP options
            JarArchiveComparatorOptions options = new JarArchiveComparatorOptions();
            // Note: Ignoring missing classes to avoid transitive dependency issues
            
            // Create comparator and run comparison
            JarArchiveComparator comparator = new JarArchiveComparator(options);
            List<JApiClass> jApiClasses = comparator.compare(oldArchives, newArchives);
            
            // Extract breaking changes from results
            String libraryName = getLibraryName(oldDependency);
            String oldVersion = oldDependency.getArtifact().getVersion();
            String newVersion = newDependency.getArtifact().getVersion();
            
            for (JApiClass jApiClass : jApiClasses) {
                breakingChanges.addAll(extractBreakingChangesFromClass(jApiClass, libraryName, oldVersion, newVersion));
            }
            
            if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
                LOGGER.info(String.format("Found %d breaking changes between %s and %s of %s", 
                                 breakingChanges.size(), oldVersion, newVersion, libraryName));
            }
            
        } catch (Exception e) {
            LOGGER.severe("Error comparing JAR files: " + e.getMessage());
            e.printStackTrace();
        }
        
        return breakingChanges;
    }

    /**
     * Extracts breaking changes from a JApiClass.
     */
    private List<BreakingChange> extractBreakingChangesFromClass(JApiClass jApiClass, 
                                                               String libraryName, 
                                                               String oldVersion, 
                                                               String newVersion) {
        List<BreakingChange> changes = new ArrayList<>();
        
        // Check class-level changes
        if (hasBreakingChanges(jApiClass.getCompatibilityChanges())) {
            changes.add(BreakingChange.builder()
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
            if (hasBreakingChanges(method.getCompatibilityChanges())) {
                changes.add(BreakingChange.builder()
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
            if (hasBreakingChanges(field.getCompatibilityChanges())) {
                changes.add(BreakingChange.builder()
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
            if (hasBreakingChanges(constructor.getCompatibilityChanges())) {
                changes.add(BreakingChange.builder()
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
     * Checks if there are any breaking compatibility changes.
     */
    private boolean hasBreakingChanges(List<JApiCompatibilityChange> changes) {
        return !changes.isEmpty();
    }

    /**
     * Creates a description string from compatibility changes.
     */
    private String getChangeDescription(List<JApiCompatibilityChange> changes) {
        StringBuilder desc = new StringBuilder();
        for (JApiCompatibilityChange change : changes) {
            if (desc.length() > 0) desc.append("; ");
            desc.append(change.getType().name());
        }
        return desc.toString();
    }

    /**
     * Gets the library name in groupId:artifactId format.
     */
    private String getLibraryName(Dependency dependency) {
        Artifact artifact = dependency.getArtifact();
        return artifact.getGroupId() + ":" + artifact.getArtifactId();
    }
}
