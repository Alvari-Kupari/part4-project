package com.example.dependencyupdate;

import com.example.depanalyzer.analyzer.dependencycollection.DependencyAdapter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.graph.Dependency;

public class PomWriter {
  private Model model;
  private File pomFile;

  public PomWriter(Path pomPath) throws IOException, XmlPullParserException {

    this.pomFile = pomPath.toFile();

    MavenXpp3Reader reader = new MavenXpp3Reader();
    FileReader fileReader = new FileReader(pomFile);

    this.model = reader.read(fileReader);
  }

  public List<Dependency> getDependencies() {

    return model.getDependencies().stream()
        .map(DependencyAdapter::toAether)
        .collect(Collectors.toList());
  }

  public void writeToPom() throws IOException {
    MavenXpp3Writer writer = new MavenXpp3Writer();
    FileWriter fileWriter = new FileWriter(pomFile);
    writer.write(fileWriter, model);
  }
}
