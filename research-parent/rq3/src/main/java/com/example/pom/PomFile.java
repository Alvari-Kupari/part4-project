package com.example.pom;

import com.example.depanalyzer.analyzer.dependencycollection.DependencyAdapter;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.graph.Dependency;

public class PomFile {
  public static final String POM_LOCATION = "pom.xml";
  private final Model model;

  public PomFile(Path projectDir) throws IOException, PomException {
    File pom = projectDir.resolve(POM_LOCATION).toFile();

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

  public ParserConfiguration.LanguageLevel getJavaVersion() {
    String source = this.model.getProperties().getProperty("maven.compiler.source");
    if (source == null) {
      System.out.println("No compile java version found -> defaulting to current latest.");
      return LanguageLevel.CURRENT;
    }

    ParserConfiguration.LanguageLevel var10000;
    switch (source) {
      case "1.0":
        var10000 = LanguageLevel.JAVA_1_0;
        break;
      case "1.1":
        var10000 = LanguageLevel.JAVA_1_1;
        break;
      case "1.2":
        var10000 = LanguageLevel.JAVA_1_2;
        break;
      case "1.3":
        var10000 = LanguageLevel.JAVA_1_3;
        break;
      case "1.4":
        var10000 = LanguageLevel.JAVA_1_4;
        break;
      case "5":
      case "1.5":
        var10000 = LanguageLevel.JAVA_5;
        break;
      case "6":
      case "1.6":
        var10000 = LanguageLevel.JAVA_6;
        break;
      case "7":
      case "1.7":
        var10000 = LanguageLevel.JAVA_7;
        break;
      case "8":
      case "1.8":
        var10000 = LanguageLevel.JAVA_8;
        break;
      case "9":
        var10000 = LanguageLevel.JAVA_9;
        break;
      case "10":
        var10000 = LanguageLevel.JAVA_10;
        break;
      case "11":
        var10000 = LanguageLevel.JAVA_11;
        break;
      case "12":
        var10000 = LanguageLevel.JAVA_12;
        break;
      case "13":
        var10000 = LanguageLevel.JAVA_13;
        break;
      case "14":
        var10000 = LanguageLevel.JAVA_14;
        break;
      case "15":
        var10000 = LanguageLevel.JAVA_15;
        break;
      case "16":
        var10000 = LanguageLevel.JAVA_16;
        break;
      case "17":
        var10000 = LanguageLevel.JAVA_17;
        break;
      case "18":
        var10000 = LanguageLevel.JAVA_18;
        break;
      case "19":
        var10000 = LanguageLevel.JAVA_19;
        break;
      case "20":
        var10000 = LanguageLevel.JAVA_20;
        break;
      case "21":
        var10000 = LanguageLevel.JAVA_21;
        break;
      default:
        System.out.println("Unknown Java version: " + source + ", defaulting to CURRENT");
        var10000 = LanguageLevel.CURRENT;
    }
    return var10000;
  }

  private boolean isSnapshot(org.apache.maven.model.Dependency dep) {
    String version = dep.getVersion();
    return version != null && version.endsWith("-SNAPSHOT");
  }
}
