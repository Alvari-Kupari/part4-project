package com.example.depanalyzer;

import com.example.depanalyzer.analyzer.analysis.DependencyDatabase;
import com.example.depanalyzer.analyzer.analysis.Parser;
import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;
import com.example.depanalyzer.analyzer.analysis.visitors.Visitor;
import com.example.depanalyzer.analyzer.dependencycollection.DependencyFile;
import com.example.depanalyzer.analyzer.dependencycollection.DependencyTraverser;
import com.example.depanalyzer.analyzer.dependencycollection.DependencyTree;
import com.example.depanalyzer.analyzer.dependencycollection.PomFile;
import com.example.depanalyzer.analyzer.report.UsageReport;
import com.example.depanalyzer.request.Request;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

    List<DependencyFile> jarFiles = new ArrayList<>();

    tree.getTransitiveDependencies()
        .forEach(
            dep -> {
              List<DependencyFile> files = new Request(system, session).resolve(dep);
              jarFiles.addAll(files);
            });

    Parser parser =
        new Parser(
            repoPath, jarFiles.stream().map(DependencyFile::getFile).collect(Collectors.toList()));
    UsageReport report = new UsageReport();
    DependencyDatabase database = new DependencyDatabase(jarFiles);

    for (Path javaFile : parser.getJavaFiles()) {
      ParseResult<CompilationUnit> result = parser.parse(javaFile);
      Visitor visitor = new Visitor(javaFile, database);

      visitor.visit(result.getResult().get(), report);
    }

    report.print();
  }
}
