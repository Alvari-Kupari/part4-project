package com.example.depanalyzer.analyzer.collection;

import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
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
                  int dollar = className.indexOf('$'); // Remove inner classes
                  if (dollar > 0) {
                    className = className.substring(0, dollar);
                  }
                  transitiveClassNames.add(className);
                });
      } catch (IOException e) {
        System.out.println("Failed to read jar: " + jar.getName());
      }
    }

    // transitiveClassNames.forEach(System.out::println);
  }

  public boolean isFromTransitiveJar(ResolvedDeclaration resolvedDecl) {
    if (resolvedDecl instanceof ResolvedMethodDeclaration method) {
      String qualified = method.getQualifiedName();
      String declaringType = qualified.substring(0, qualified.lastIndexOf('.'));
      return transitiveClassNames.contains(declaringType);
    }
    if (resolvedDecl instanceof ResolvedReferenceTypeDeclaration type) {
      return transitiveClassNames.contains(type.getQualifiedName());
    }
    if (resolvedDecl instanceof ResolvedConstructorDeclaration constructor) {
      return transitiveClassNames.contains(constructor.declaringType().getQualifiedName());
    }
    if (resolvedDecl instanceof ResolvedFieldDeclaration field) {
      return transitiveClassNames.contains(field.declaringType().getQualifiedName());
    }
    if (resolvedDecl instanceof ResolvedValueDeclaration value && value.isField()) {
      return transitiveClassNames.contains(value.asField().declaringType().getQualifiedName());
    }

    return false;
  }
}
