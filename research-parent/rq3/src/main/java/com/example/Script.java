package com.example;

import com.example.depanalyzer.analyzer.analysis.RepositorySystemFactory;
import com.example.depanalyzer.analyzer.dependencycollection.PomReader;
import com.example.dependencyupdate.DependencyUpdate;
import com.example.dependencyupdate.NoDependencyUpdateException;
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
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.version.InvalidVersionSpecificationException;

public class Script {

  public static void main(String[] args) throws IOException, XmlPullParserException {

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

    RepositorySystem system = RepositorySystemFactory.newRepositorySystem();
    RepositorySystemSession session = RepositorySystemFactory.newSession(system);

    PomReader pom = new PomReader(pomFile);

    List<Dependency> deps = pom.getDependencies();

    for (Dependency dep : deps) {
      System.out.println();
      System.out.println("Inspecting dep: " + dep.toString());

      DependencyUpdate update = new DependencyUpdate(dep, system, session);

      try {
        Dependency latestMinor = update.getLatestMinorVersion();
        System.out.println("Latest minor version: " + latestMinor.toString());
      } catch (VersionRangeResolutionException e) {

        e.printStackTrace();
      } catch (InvalidVersionSpecificationException e) {

        e.printStackTrace();
      } catch (NoDependencyUpdateException e) {
        System.out.println("No update available for dependency: " + dep);
      }
    }
  }
}
