package com.example.depanalyzer.analyzer.collection;

import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TransitiveDatabase {
  private final Set<String> jarPaths;

  public TransitiveDatabase(List<File> jarFiles) {
    this.jarPaths = jarFiles.stream().map(File::getAbsolutePath).collect(Collectors.toSet());
  }

  public boolean isFromTransitiveJar(ResolvedDeclaration resolvedDecl) {
    try {
      Class<?> clazz = resolvedDecl.getClass();
      System.out.println("ResolvedDecl class: " + clazz.getName());

      if (clazz.getProtectionDomain() == null) {
        System.out.println("No protection domain.");
        return false;
      }

      if (clazz.getProtectionDomain().getCodeSource() == null) {
        System.out.println("No code source.");
        return false;
      }

      String sourcePath = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
      System.out.println("Source path: " + sourcePath);

      boolean match = jarPaths.stream().anyMatch(sourcePath::contains);
      System.out.println("Matches transitive jar: " + match);
      return match;

    } catch (Exception e) {
      System.out.println("Exception in isFromTransitiveJar: " + e.getMessage());
      return false;
    }
  }
}
