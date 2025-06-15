package com.example.depanalyzer.analyzer.collection;

import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TransitiveDatabase {
  private final Set<String> jarPaths;

  public TransitiveDatabase(List<File> jarFiles) {
    this.jarPaths = jarFiles.stream().map(File::getAbsolutePath).collect(Collectors.toSet());

    System.out.println("Transitive JAR paths:");
    jarPaths.forEach(System.out::println);
  }

  public boolean isFromTransitiveJar(ResolvedDeclaration resolvedDecl) {
    try {
      if (resolvedDecl instanceof ResolvedReferenceTypeDeclaration) {
        String qualifiedName = ((ResolvedReferenceTypeDeclaration) resolvedDecl).getQualifiedName();
        Class<?> realClass = Class.forName(qualifiedName);
        String sourcePath = realClass.getProtectionDomain().getCodeSource().getLocation().getPath();

        System.out.println("Real class: " + qualifiedName);
        System.out.println("Source path: " + sourcePath);

        boolean match = jarPaths.stream().anyMatch(sourcePath::contains);
        System.out.println("Matches transitive jar: " + match);
        return match;
      }

      System.out.println("Not a reference type: " + resolvedDecl.getClass().getName());
      return false;

    } catch (Exception e) {
      System.out.println("Exception in isFromTransitiveJar: " + e.getMessage());
      return false;
    }
  }
}
