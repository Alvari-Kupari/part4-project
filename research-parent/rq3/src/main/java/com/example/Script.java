package com.example;

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
  }
}
