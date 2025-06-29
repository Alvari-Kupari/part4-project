package com.example.depanalyzer.analyzer.analysis;

import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.jar.JarFile;
import org.eclipse.aether.artifact.Artifact;

public class DependencyDatabase {
  // key is the library name, value is the set of classes in the library
  private final Map<String, Set<String>> libraries;

  public DependencyDatabase(Collection<Artifact> artifacts) throws IOException {
    libraries = new HashMap<>();

    for (Artifact artifact : artifacts) {

      String libraryName = getArtifactString(artifact);
      libraries.computeIfAbsent(libraryName, k -> new HashSet<>());
      JarFile jarFile = new JarFile(artifact.getFile());
      jarFile.stream()
          .filter(e -> e.getName().endsWith(".class"))
          .forEach(
              e -> {
                String className = normalizeClassName(e.getName());
                libraries.get(libraryName).add(className);
              });
      jarFile.close();
    }
  }

  public Optional<String> checkIfTransitive(ResolvedDeclaration resolvedDecl) {
    if (resolvedDecl instanceof ResolvedMethodDeclaration method) {
      String qualified = method.getQualifiedName();
      String declaringType = qualified.substring(0, qualified.lastIndexOf('.'));

      return checkMatch(declaringType);
    }
    if (resolvedDecl instanceof ResolvedReferenceTypeDeclaration type) {
      return checkMatch(type.getQualifiedName());
    }
    if (resolvedDecl instanceof ResolvedConstructorDeclaration constructor) {

      return checkMatch(constructor.declaringType().getQualifiedName());
    }
    if (resolvedDecl instanceof ResolvedFieldDeclaration field) {

      return checkMatch(field.declaringType().getQualifiedName());
    }
    if (resolvedDecl instanceof ResolvedValueDeclaration value && value.isField()) {
      return checkMatch(value.asField().declaringType().getQualifiedName());
    }

    return Optional.empty();
  }

  private Optional<String> checkMatch(String matchToCheck) {
    for (Entry<String, Set<String>> entry : libraries.entrySet()) {
      if (entry.getValue().contains(matchToCheck)) {
        return Optional.ofNullable(entry.getKey());
      }
    }
    return Optional.empty();
  }

  private String normalizeClassName(String classEntry) {
    String className = classEntry.replace("/", ".").replace(".class", "");
    int dollar = className.indexOf('$'); // Remove inner classes
    if (dollar > 0) {
      className = className.substring(0, dollar);
    }

    return className;
  }

  private String getArtifactString(Artifact artifact) {
    return artifact.toString();
  }
}
