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
      System.out.println(libraryName);
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

    // for (String className : libraries.get("org.apache.maven.artifact.Artifact")) {
    //   System.out.println("Classname: " + className);
    // }
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
        // System.out.println("MATCH FOUND: " + matchToCheck + ". LIBRARY: " + entry.getKey());
        return Optional.ofNullable(entry.getKey());
      }
    }
    org.apache.maven.artifact.Artifact art;
    Artifact a;
    return Optional.empty();
  }

  //  MATCH between org.eclipse.aether.artifact.Artifact and org.apache.maven.artifact.Artifact.
  // LIBRARY: org.apache.maven.resolver:maven-resolver-api:jar:1.6.3

  // what was added: org/apache/maven/artifact/Artifact.class and
  // org/eclipse/aether/artifact/Artifact.class
  private String normalizeClassName(String classEntry) {
    // System.out.println(classEntry);
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
