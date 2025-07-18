package com.example;

import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;
import com.example.dependencyupdate.PomWriter;
import japicmp.cmp.JApiCmpArchive;
import japicmp.cmp.JarArchiveComparator;
import japicmp.cmp.JarArchiveComparatorOptions;
import japicmp.model.JApiClass;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;

public class Script {

  public static void main(String[] args) throws Exception {

    File jarFilePath = null;
    List<JApiCmpArchive> oldArchives = new ArrayList<>();
    List<JApiCmpArchive> newArchives = new ArrayList<>();

    JarArchiveComparatorOptions comparatorOptions = new JarArchiveComparatorOptions();
    JarArchiveComparator jarArchiveComparator = new JarArchiveComparator(comparatorOptions);
    JApiCmpArchive archive = new JApiCmpArchive(jarFilePath, null);
    List<JApiClass> jApiClasses = jarArchiveComparator.compare(oldArchives, newArchives);

    Path pomFile =
        Path.of(
            "C:\\\\Users\\\\Alvari\\\\Documents\\\\UNI\\\\softeng_700\\\\mock-project", "pom.xml");

    if (!Files.exists(pomFile)) {
      throw new IOException("Pom file not found at: " + pomFile);
    }

    PomWriter pomWriter = new PomWriter(pomFile);

    List<Dependency> deps = pomWriter.getDependencies();

    RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
    RepositorySystemSession session = RepositorySystemFactory.newSession(system);

    for (Dependency dep : deps) {
      System.out.println("Analysing dependency: " + dep);

      DependencyUpdate updater = new DependencyUpdate(dep, system, session);

      boolean isFinished = false;
      do {
        isFinished = updater.update();
      } while (isFinished);
    }

    pomWriter.writeToPom();
  }
}
