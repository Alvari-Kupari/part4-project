package com.example.depanalyzer;

import com.example.depanalyzer.analyzer.analysis.Parser;
import com.example.depanalyzer.analyzer.analysis.visitors.TransitiveUsageVisitor;
import com.example.depanalyzer.analyzer.collection.DependencyTraverser;
import com.example.depanalyzer.analyzer.collection.DependencyTree;
import com.example.depanalyzer.analyzer.collection.PomFile;
import com.example.depanalyzer.analyzer.collection.RepositorySystemFactory;
import com.example.depanalyzer.analyzer.report.UsageReport;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import java.nio.file.Path;
import java.util.List;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;

/** Hello world! */
public class Main {
  public static void main(String[] args) throws Exception {
    System.out.println("Hello World!");

    String repoPath =
        "C:\\Users\\Alvari\\Documents\\UNI\\archive\\SOFTENG_206\\r"
            + "epos\\escaipe-room-beta-and-final-team-27";

    RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
    RepositorySystemSession session = RepositorySystemFactory.newSession(system);

    PomFile pom = new PomFile(repoPath);

    List<Dependency> dependencies = pom.getDependencies();

    DependencyTree tree = new DependencyTree();

    for (Dependency dep : dependencies) {

      DependencyTraverser traverser = new DependencyTraverser(dep, system, session);

      traverser.traverse(tree);
    }

    tree.print();

    System.out.println("tree size: " + tree.size());

    Parser parser = new Parser(repoPath);
    UsageReport report = new UsageReport();

    for (Path javaFile : parser.getJavaFiles()) {
      ParseResult<CompilationUnit> result = parser.parse(javaFile);
      TransitiveUsageVisitor visitor = new TransitiveUsageVisitor(javaFile);

      visitor.visit(result.getResult().get(), report);
    }

    report.print();
  }
}
