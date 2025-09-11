package com.example.normalisation;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

public class DependencyDatabase {

  private final Map<String, Artifact> fqnToArtifact = new HashMap<>();
  private final String rootPackage;

  /**
   * Constructs the database from a set of artifacts. Each artifact should have an associated JAR
   * file.
   *
   * @param artifacts Set of artifacts to index
   * @throws IOException if a JAR cannot be read
   */
  public DependencyDatabase(String rootPackage, Set<Artifact> artifacts) throws IOException {
    this.rootPackage = rootPackage;
    for (Artifact artifact : artifacts) {
      File jarFile = artifact.getFile();
      if (jarFile != null && jarFile.exists()) {
        indexJar(jarFile, artifact);
      }
    }
  }

  /**
   * Returns the Artifact for a given fully qualified class name.
   *
   * @param fullyQualifiedName fully qualified name (e.g., "com.example.MyClass")
   * @return Artifact, or null if not found
   */
  public Artifact getArtifact(String fqn) {
    Artifact artifact = fqnToArtifact.get(fqn);
    if (artifact != null) {
      return artifact;
    }

    // JDK internal
    if (fqn.startsWith("java.") || fqn.startsWith("javax.") || fqn.startsWith("jdk.")) {
      return new DefaultArtifact("JDK", "java.base", "N/A", "N/A");
    }

    // Client internal
    if (rootPackage != null && fqn.startsWith(rootPackage + ".")) {
      return new DefaultArtifact("CLIENT", "client-code", "N/A", "N/A");
    }

    return null;
  }

  private void indexJar(File jarFile, Artifact artifact) throws IOException {
    try (JarFile jar = new JarFile(jarFile)) {
      Enumeration<JarEntry> entries = jar.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        if (entry.getName().endsWith(".class") && !entry.isDirectory()) {
          String classFqn =
              entry.getName().replace('/', '.').replace('\\', '.').replaceAll("\\.class$", "");

          // Collapse inner classes to their outer class
          int dollarIndex = classFqn.indexOf('$');
          if (dollarIndex != -1) {
            classFqn = classFqn.substring(0, dollarIndex);
          }

          fqnToArtifact.put(classFqn, artifact);
        }
      }
    } catch(ZipException e) {
      System.err.println("SEVERE: Failed to read JAR file: " + jarFile.getAbsolutePath());
    }
  }
}
