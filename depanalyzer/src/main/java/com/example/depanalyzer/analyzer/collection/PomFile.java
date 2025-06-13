package com.example.depanalyzer.analyzer.collection;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;

public class PomFile {
  private File pomFile;

  public PomFile(String pomFile) {
    this.pomFile = new File(pomFile);
  }

  public List<Dependency> getDependencies() throws IOException, XmlPullParserException {
    MavenXpp3Reader reader = new MavenXpp3Reader();
    FileReader fileReader = new FileReader(pomFile);
    Model model = reader.read(fileReader);
    return model.getDependencies().stream()
        .map(
            dep ->
                new Dependency(
                    new DefaultArtifact(
                        dep.getGroupId(),
                        dep.getArtifactId(),
                        dep.getClassifier(),
                        dep.getType(),
                        dep.getVersion()),
                    dep.getScope()))
        .collect(Collectors.toList());
  }
}
