package com.example;

import com.example.breakingchange.BreakingChange;
import com.example.breakingchange.BreakingChangeAnalyzer;
import com.example.breakingchange.TransitiveDependencyAnalyzer;
import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;
import com.example.dependencyupdate.DependencyUpdate;
import com.example.dependencyupdate.NoDependencyUpdateException;
import com.example.dependencyupdate.VersionResolutionException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;

public class DependencyAnalysis {
  private Dependency dependency;
  private List<BreakingChange> directBreakingChanges;
  private List<BreakingChange> transitiveBreakingChanges;

  private static final Logger LOGGER = Logger.getLogger(DependencyAnalysis.class.getName());
  private static final RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
  private static final RepositorySystemSession session = RepositorySystemFactory.newSession(system);
  private static final BreakingChangeAnalyzer breakingChangeAnalyzer =
      new BreakingChangeAnalyzer(system, session);
  private static final TransitiveDependencyAnalyzer transitiveDependencyAnalyzer =
      new TransitiveDependencyAnalyzer(system, session, breakingChangeAnalyzer);

  public DependencyAnalysis(Dependency dep) {
    this.dependency = dep;
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

        // 2. Find transitive dependency version changes
        List<TransitiveDependencyAnalyzer.TransitiveDependencyChange> transitiveChanges =
            transitiveDependencyAnalyzer.findTransitiveDependencyChanges(current, nextVersion);

        // 3. Analyze breaking changes in each transitive dependency update
        this.transitiveBreakingChanges = new ArrayList<>();
        for (TransitiveDependencyAnalyzer.TransitiveDependencyChange transitiveChange :
            transitiveChanges) {
          LOGGER.info("Analyzing transitive breaking changes for: " + transitiveChange);
          List<BreakingChange> changes =
              transitiveDependencyAnalyzer.analyzeTransitiveBreakingChanges(transitiveChange);
          this.transitiveBreakingChanges.addAll(changes);
        }

        current = nextVersion;
      } catch (Exception e) {
        LOGGER.log(
            Level.WARNING,
            "Failed to analyze breaking changes between "
                + current
                + " and "
                + nextVersion
                + ": "
                + e.getMessage(),
            e);
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
