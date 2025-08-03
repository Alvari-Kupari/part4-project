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
    File effectivePom = projectDir.resolve("effective-pom.xml").toFile();

    generateEffectivePom(projectDir.toFile());

    try (FileReader reader = new FileReader(effectivePom)) {
      try {
        this.model = new MavenXpp3Reader().read(reader);
      } catch (XmlPullParserException e) {
        throw new PomException(e);
      }
    }

    effectivePom.deleteOnExit();
  }

  private void generateEffectivePom(File directory) throws IOException, InterruptedException {
    ProcessBuilder builder =
        new ProcessBuilder(
            "cmd", "/c", "mvn.cmd", "help:effective-pom", "-Doutput=effective-pom.xml");
    builder.directory(directory);
    builder.redirectErrorStream(true);
    Process process = builder.start();
    int exitCode = process.waitFor(); // <-- Wait for process to fully finish

    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      while ((reader.readLine()) != null) {
        /* drain */
      }
    }

    if (exitCode != 0) {
      throw new IOException("Failed to generate effective POM");
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
