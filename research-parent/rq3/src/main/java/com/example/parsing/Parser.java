package com.example.parsing;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.aether.artifact.Artifact;

public class Parser {
  private static final String SRC_MAIN_JAVA = "src/main/java";
  private static final String JAVA_FILE_EXTENSION = "java";
  private JavaParser parser;
  private Path srcMainJavaPath;

  public Parser(
      Path repoPath, Set<Artifact> artifacts, ParserConfiguration.LanguageLevel javaVersion)
      throws IOException {
    System.out.println("LANGAUGE LEVEL " + String.valueOf(javaVersion));
    this.srcMainJavaPath = repoPath.resolve(SRC_MAIN_JAVA);
    ParserConfiguration config = (new ParserConfiguration()).setLanguageLevel(javaVersion);
    CombinedTypeSolver typeSolver = new CombinedTypeSolver(new TypeSolver[0]);
    JavaParserTypeSolver javaSolver = new JavaParserTypeSolver(this.srcMainJavaPath);
    ReflectionTypeSolver reflectionSolver = new ReflectionTypeSolver();
    typeSolver.add(javaSolver);
    typeSolver.add(reflectionSolver);

   for (Artifact artifact : artifacts) {
    File jarFile = artifact.getFile();
    if (jarFile == null || !jarFile.exists()) {
        System.err.println("Artifact JAR file does not exist: " + artifact);
        continue;
    }

    if (!isValidJarFile(jarFile)) {
        System.err.println("Invalid JAR file: " + jarFile.getAbsolutePath());
        continue;
    }

    JarTypeSolver jarTypeSolver = new JarTypeSolver(jarFile);
    typeSolver.add(jarTypeSolver);
}


    JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
    config.setSymbolResolver(symbolSolver);
    this.parser = new JavaParser(config);
  }

  public List<Path> getJavaFiles() throws IOException {
    return Files.walk(this.srcMainJavaPath)
        .filter(Files::isRegularFile)
        .filter(this::isJavaFile)
        .collect(Collectors.toList());
  }

  public ParseResult<CompilationUnit> parse(Path file) throws IOException {
    return this.parser.parse(file);
  }

  private boolean isJavaFile(Path file) {
    return this.getFileExtension(file).equals(JAVA_FILE_EXTENSION);
  }

  private String getFileExtension(Path file) {
    String fileName = file.getFileName().toString();
    int dotIndex = fileName.lastIndexOf(46);
    return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
  }

  private boolean isValidJarFile(File file) {
    try (java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(file)) {
        zipFile.entries(); // just attempt to access entries
        return true;
    } catch (IOException e) {
        return false;
    }
}

}
