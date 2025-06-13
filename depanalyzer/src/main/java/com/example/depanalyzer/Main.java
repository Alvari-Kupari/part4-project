package com.example.depanalyzer;

import com.example.depanalyzer.analyzer.collection.DependencyTraverser;
import com.example.depanalyzer.analyzer.collection.DependencyTree;
import com.example.depanalyzer.analyzer.collection.PomFile;
import com.example.depanalyzer.analyzer.collection.RepositorySystemFactory;
import java.util.List;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;

/** Hello world! */
public class Main {
  public static void main(String[] args) throws Exception {
    System.out.println("Hello World!");

    String pathName =
        "C:\\Users\\Alvari\\Documents\\UNI\\archive\\SOFTENG_206\\r"
            + "epos\\escaipe-room-beta-and-final-team-27\\pom.xml";

    RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
    RepositorySystemSession session = RepositorySystemFactory.newSession(system);

    PomFile reader = new PomFile(pathName);

    List<Dependency> dependencies = reader.getDependencies();

    DependencyTree tree = new DependencyTree();

    for (Dependency dep : dependencies) {

      DependencyTraverser traverser = new DependencyTraverser(dep, system, session);

      traverser.traverse(tree);
    }

    tree.print();

    System.out.println("tree size: " + tree.size());
  }
}
