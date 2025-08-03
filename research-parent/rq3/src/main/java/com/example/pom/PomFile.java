package com.example.pom;

import com.example.depanalyzer.analyzer.dependencycollection.DependencyAdapter;
import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.graph.Dependency;

public class PomFile {
  private final Model model;

  public PomFile(Path projectDir) throws IOException, PomException, InterruptedException {
    File pom = projectDir.resolve("effective-pom.xml").toFile();

    try (FileReader reader = new FileReader(pom)) {
      try {
        this.model = new MavenXpp3Reader().read(reader);
      } catch (XmlPullParserException e) {
        throw new PomException(e);
      }
    }
  }

  public List<Dependency> getDependencies() {
    return model.getDependencies().stream()
        .filter(dep -> !isSnapshot(dep))
        .map(DependencyAdapter::toAether)
        .collect(Collectors.toList());
  }

  private boolean isSnapshot(org.apache.maven.model.Dependency dep) {
    String version = dep.getVersion();
    return version != null && version.endsWith("-SNAPSHOT");
  }
}
