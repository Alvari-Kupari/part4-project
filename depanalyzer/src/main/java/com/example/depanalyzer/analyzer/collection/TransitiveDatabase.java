package com.example.depanalyzer.analyzer.collection;

import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;

public class TransitiveDatabase {
  private final Set<String> transitiveClassNames;

  public TransitiveDatabase(List<File> jarFiles) {
    transitiveClassNames = new HashSet<>();

    for (File jar : jarFiles) {
      try (JarFile jarFile = new JarFile(jar)) {
        jarFile.stream()
            .filter(e -> e.getName().endsWith(".class"))
            .forEach(
                e -> {
                  String className = e.getName().replace("/", ".").replace(".class", "");
                  transitiveClassNames.add(className);
                });
      } catch (IOException e) {
        System.out.println("Failed to read jar: " + jar.getName());
      }
    }

    // transitiveClassNames.forEach(System.out::println);
  }

  // public boolean isFromTransitiveJar(ResolvedDeclaration resolvedDecl) {
  //   try {
  //     if (resolvedDecl instanceof ResolvedReferenceTypeDeclaration) {
  //       String qualifiedName = ((ResolvedReferenceTypeDeclaration)
  // resolvedDecl).getQualifiedName();
  //       return transitiveClassNames.contains(qualifiedName);
  //     }
  //   } catch (Exception e) {
  //     System.out.println("Exception in isFromTransitiveJar: " + e.getMessage());
  //   }
  //   return false;
  // }

  public boolean isFromTransitiveJar(ResolvedDeclaration resolvedDecl) {
    try {
      String name = resolvedDecl.getName();
      String className = resolvedDecl.getClass().getSimpleName();
      String qualified = null;

      if (resolvedDecl instanceof ResolvedReferenceTypeDeclaration) {
        qualified = ((ResolvedReferenceTypeDeclaration) resolvedDecl).getQualifiedName();
        System.out.println("[DEBUG] ResolvedReferenceTypeDeclaration: " + qualified);
        return transitiveClassNames.contains(qualified);
      }

      // Hardcoded debug for launch()
      if (name.equals("launch")) {
        System.out.println("[DEBUG] Detected method named 'launch'");
        System.out.println("[DEBUG] Decl class: " + className);
        System.out.println("[DEBUG] Full: " + resolvedDecl);
      }

      // Try generic qualified name extraction
      if (resolvedDecl instanceof ResolvedMethodDeclaration) {
        ResolvedMethodDeclaration method = (ResolvedMethodDeclaration) resolvedDecl;
        qualified = method.getQualifiedName(); // e.g., javafx.application.Application.launch
        System.out.println("[DEBUG] Method: " + qualified);
        return transitiveClassNames.stream().anyMatch(qualified::startsWith);
      }

    } catch (Exception e) {
      System.out.println("Exception in isFromTransitiveJar: " + e.getMessage());
    }

    return false;
  }
}
