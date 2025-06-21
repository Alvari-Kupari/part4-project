package com.example.depanalyzer.analyzer.dependencycollection;

import com.github.javaparser.ParserConfiguration.LanguageLevel;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;

public class PomFile {
  private Model model;

  public PomFile(String pomFile) throws IOException, XmlPullParserException {
    Path path = Path.of(pomFile, "pom.xml");

    MavenXpp3Reader reader = new MavenXpp3Reader();
    FileReader fileReader = new FileReader(path.toFile());
    this.model = reader.read(fileReader);
  }

  public LanguageLevel getJavaVersion() {

    String source = model.getProperties().getProperty("maven.compiler.source");
    if (source == null) {
      System.out.println("No compile java version found -> defaulting to current latest.");
      return LanguageLevel.CURRENT;
    }

    return switch (source) {
      case "1.0" -> LanguageLevel.JAVA_1_0;
      case "1.1" -> LanguageLevel.JAVA_1_1;
      case "1.2" -> LanguageLevel.JAVA_1_2;
      case "1.3" -> LanguageLevel.JAVA_1_3;
      case "1.4" -> LanguageLevel.JAVA_1_4;
      case "5", "1.5" -> LanguageLevel.JAVA_5;
      case "6", "1.6" -> LanguageLevel.JAVA_6;
      case "7", "1.7" -> LanguageLevel.JAVA_7;
      case "8", "1.8" -> LanguageLevel.JAVA_8;
      case "9" -> LanguageLevel.JAVA_9;
      case "10" -> LanguageLevel.JAVA_10;
      case "11" -> LanguageLevel.JAVA_11;
      case "12" -> LanguageLevel.JAVA_12;
      case "13" -> LanguageLevel.JAVA_13;
      case "14" -> LanguageLevel.JAVA_14;
      case "15" -> LanguageLevel.JAVA_15;
      case "16" -> LanguageLevel.JAVA_16;
      case "17" -> LanguageLevel.JAVA_17;
      case "18" -> LanguageLevel.JAVA_18;
      case "19" -> LanguageLevel.JAVA_19;
      case "20" -> LanguageLevel.JAVA_20;
      case "21" -> LanguageLevel.JAVA_21;
      default -> {
        System.out.println("Unknown Java version: " + source + ", defaulting to CURRENT");
        yield LanguageLevel.CURRENT;
      }
    };
  }

  public List<Dependency> getDependencies() throws IOException, XmlPullParserException {

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
