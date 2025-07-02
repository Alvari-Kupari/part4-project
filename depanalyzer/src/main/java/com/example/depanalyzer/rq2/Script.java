package com.example.depanalyzer.rq2;

import japicmp.cmp.JApiCmpArchive;
import japicmp.cmp.JarArchiveComparator;
import japicmp.cmp.JarArchiveComparatorOptions;
import japicmp.model.JApiClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Script {

  public static void main(String[] args) {
    // Example usage
    Script script = new Script();
    System.out.println("PomFile initialized: ");

    File jarFilePath = null;
    List<JApiCmpArchive> oldArchives = new ArrayList<>();
    List<JApiCmpArchive> newArchives = new ArrayList<>();

    JarArchiveComparatorOptions comparatorOptions = new JarArchiveComparatorOptions();
    JarArchiveComparator jarArchiveComparator = new JarArchiveComparator(comparatorOptions);
    JApiCmpArchive archive = new JApiCmpArchive(jarFilePath, null);
    List<JApiClass> jApiClasses = jarArchiveComparator.compare(oldArchives, newArchives);
  }
}
