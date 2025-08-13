package com.example;

import com.example.breakingchange.BreakingChange;
import com.example.breakingchange.BreakingChangeAnalyzer;
import com.example.breakingchange.BreakingChangeAnalysisException;
import com.example.breakingchange.TransitiveDependencyAnalyzer;
import com.example.breakingchange.TransitiveDependencyAnalyzer.TransitiveDependencyChange;
import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;
import com.example.dependencyupdate.DependencyUpdate;
import com.example.dependencyupdate.NoDependencyUpdateException;
import com.example.dependencyupdate.VersionResolutionException;

public class DependencyAnalysis {
  private Dependency dependency;
  private List<BreakingChange> directBreakingChanges;
  private List<BreakingChange> transitiveBreakingChanges;

  private final FailureTracker failureTracker;

  private static final Logger LOGGER = Logger.getLogger(DependencyAnalysis.class.getName());
  private static final RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
  private static final RepositorySystemSession session = RepositorySystemFactory.newSession(system);
  private static final BreakingChangeAnalyzer breakingChangeAnalyzer =
      new BreakingChangeAnalyzer(system, session);
  private static final TransitiveDependencyAnalyzer transitiveDependencyAnalyzer =
      new TransitiveDependencyAnalyzer(system, session, breakingChangeAnalyzer);

  public DependencyAnalysis(Dependency dep, FailureTracker failureTracker) {
    this.dependency = dep;
    this.failureTracker = failureTracker;
  }

  // Backwards-compat constructor (no tracking)
  public DependencyAnalysis(Dependency dep) {
    this(dep, null);
  }

  public void execute() throws VersionResolutionException, NoDependencyUpdateException {
    DependencyUpdate update = new DependencyUpdate(dependency, system, session);

    List<Dependency> updates = update.getMinorUpdates();

    Dependency current = dependency;
    for (Dependency nextVersion : updates) {
      LOGGER.info(
          "Analyzing breaking changes between dependencies " + current + " and " + nextVersion);

      try {
        // 1. Analyze direct dependency breaking changes
        this.directBreakingChanges =
            breakingChangeAnalyzer.analyzeBreakingChanges(current, nextVersion);
        if (failureTracker != null) failureTracker.recordSuccess();

        // 2. Find transitive dependency version changes
        List<TransitiveDependencyChange> transitiveChanges =
            transitiveDependencyAnalyzer.findTransitiveDependencyChanges(current, nextVersion);

        // 3. Analyze breaking changes in each transitive dependency update
        this.transitiveBreakingChanges = new ArrayList<>();
        for (TransitiveDependencyChange transitiveChange : transitiveChanges) {
          try {
            List<BreakingChange> changes =
                transitiveDependencyAnalyzer.analyzeTransitiveBreakingChanges(transitiveChange);
            this.transitiveBreakingChanges.addAll(changes);
            if (failureTracker != null) failureTracker.recordSuccess();
          } catch (Exception te) {
            // Log and record transitive failure with the specific pair
            try {
              org.eclipse.aether.artifact.Artifact oA =
                  new org.eclipse.aether.artifact.DefaultArtifact(
                      transitiveChange.getGroupId(),
                      transitiveChange.getArtifactId(),
                      "jar",
                      transitiveChange.getOldVersion());
              org.eclipse.aether.artifact.Artifact nA =
                  new org.eclipse.aether.artifact.DefaultArtifact(
                      transitiveChange.getGroupId(),
                      transitiveChange.getArtifactId(),
                      "jar",
                      transitiveChange.getNewVersion());
              org.eclipse.aether.graph.Dependency oldDep =
                  new org.eclipse.aether.graph.Dependency(oA, "compile");
              org.eclipse.aether.graph.Dependency newDep =
                  new org.eclipse.aether.graph.Dependency(nA, "compile");
              if (failureTracker != null) failureTracker.recordFailure(oldDep, newDep, te);
            } catch (Exception ignore) {
              // best-effort for failure tracking
            }
            LOGGER.log(
                Level.WARNING,
                "Failed to analyze transitive breaking changes for: " + transitiveChange + ": "
                    + te.getMessage(),
                te);
          }
        }

        current = nextVersion;
      } catch (BreakingChangeAnalysisException e) {
        LOGGER.log(
            Level.WARNING,
            "Breaking change analysis failed between "
                + current
                + " and "
                + nextVersion
                + ": "
                + e.getMessage(),
            e);
        if (failureTracker != null) failureTracker.recordFailure(current, nextVersion, e);
      } catch (Exception e) {
        LOGGER.log(
            Level.WARNING,
            "Unexpected error analyzing breaking changes between "
                + current
                + " and "
                + nextVersion
                + ": "
                + e.getMessage(),
            e);
        if (failureTracker != null) failureTracker.recordFailure(current, nextVersion, e);
      }
    }
  }

  public List<BreakingChange> getDirectBreakingChanges() {
    return this.directBreakingChanges;
  }

  public List<BreakingChange> getTransitiveBreakingChanges() {
    return this.transitiveBreakingChanges;
  }
}
