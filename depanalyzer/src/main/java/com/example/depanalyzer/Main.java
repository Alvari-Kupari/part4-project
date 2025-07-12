package com.example.depanalyzer;

import com.example.depanalyzer.analyzer.analysis.DependencyDatabase;
import com.example.depanalyzer.analyzer.analysis.Parser;
import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;
import com.example.depanalyzer.analyzer.analysis.visitors.AnnotationVisitor;
import com.example.depanalyzer.analyzer.analysis.visitors.ExpressionVisitor;
import com.example.depanalyzer.analyzer.analysis.visitors.UsageAnalyzer;
import com.example.depanalyzer.analyzer.dependencycollection.DependencyTraverser;
import com.example.depanalyzer.analyzer.dependencycollection.PomFile;
import com.example.depanalyzer.analyzer.dependencycollection.Request;
import com.example.depanalyzer.analyzer.dependencytree.Tree;
import com.example.depanalyzer.analyzer.report.UsageReport;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;

/** Hello world! */
public class Main {
  private static final String laptopRepoPath =
      "C:\\Users\\Poika\\OneDrive\\Documents\\UNI\\archive\\SOFTENG_206\\r"
          + "epos\\escaipe-room-beta-and-final-team-27";
  private static final String pcRepoPath =
      "C:\\Users\\Alvari\\Documents\\UNI\\archive\\SOFTENG_206\\r"
          + "epos\\escaipe-room-beta-and-final-team-27";

  private static final String otherPath =
      "C:\\Users\\Alvari\\Documents\\UNI\\softeng_700\\part4-project\\depanalyzer";

  public static void main(String[] args) throws Exception {

    String repoPath = otherPath;

    // for jar running
    for (String arg : args) {
      if (arg.startsWith("--project=")) {
        repoPath = arg.substring("--project=".length());
      }
    }

    System.out.println("Analyzing project at: " + repoPath);

    RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
    RepositorySystemSession session = RepositorySystemFactory.newSession(system);

    PomFile pom = new PomFile(repoPath);

    List<Dependency> dependencies = pom.getDependencies();

    Tree tree = new Tree();

    for (Dependency dep : dependencies) {

      DependencyTraverser traverser = new DependencyTraverser(dep, system, session);

      traverser.traverse(tree);
    }

    Set<Artifact> allArtifacts = new HashSet<>();
    Set<Artifact> transitiveArtifacts = new HashSet<>();

    tree.getAllDependencies()
        .forEach(
            dep -> {
              Set<Artifact> artifacts = new Request(system, session).resolve(dep);
              allArtifacts.addAll(artifacts);
            });

    tree.getTransitiveDependencies()
        .forEach(
            dep -> {
              Set<Artifact> artifacts = new Request(system, session).resolve(dep);
              transitiveArtifacts.addAll(artifacts);
            });

    Parser parser = new Parser(repoPath, allArtifacts, pom.getJavaVersion());
    UsageReport report = new UsageReport();
    DependencyDatabase database = new DependencyDatabase(transitiveArtifacts);

    for (Path javaFile : parser.getJavaFiles()) {
      ParseResult<CompilationUnit> result = parser.parse(javaFile);
      UsageAnalyzer helper = new UsageAnalyzer(javaFile, database);
      ExpressionVisitor visitor = new ExpressionVisitor(helper);
      AnnotationVisitor annotationVisitor = new AnnotationVisitor(helper);

      visitor.visit(result.getResult().get(), report);
      annotationVisitor.visit(result.getResult().get(), report);
    }

    Path markdown = Path.of("report.md");
    report.writeToMarkDown(markdown);
  }
}
