package com.example.depanalyzer.analyzer.analysis;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.ast.CompilationUnit;
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

  public Parser(String repoPath, Set<Artifact> artifacts, LanguageLevel javaVersion)
      throws IOException {
    System.out.println("LANGAUGE LEVEL " + javaVersion);
    srcMainJavaPath = Path.of(repoPath, SRC_MAIN_JAVA);
    ParserConfiguration config = new ParserConfiguration().setLanguageLevel(javaVersion);

    CombinedTypeSolver typeSolver = new CombinedTypeSolver();
    JavaParserTypeSolver javaSolver = new JavaParserTypeSolver(srcMainJavaPath);
    ReflectionTypeSolver reflectionSolver = new ReflectionTypeSolver();

    typeSolver.add(javaSolver);
    typeSolver.add(reflectionSolver);

    for (Artifact artifact : artifacts) {
      File jarFile = artifact.getFile();
      JarTypeSolver jarTypeSolver = new JarTypeSolver(jarFile);
      typeSolver.add(jarTypeSolver);
    }

    // Now set the resolver
    JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
    config.setSymbolResolver(symbolSolver);

    parser = new JavaParser(config);

    System.out.println("Testing symbol resolution for: java.util.List");
    System.out.println(typeSolver.tryToSolveType("java.util.List"));

    System.out.println(
        "Testing symbol resolution for: org.apache.http.impl.client.CloseableHttpClient");
    System.out.println(
        typeSolver.tryToSolveType("org.apache.http.impl.client.CloseableHttpClient"));
  }

  public List<Path> getJavaFiles() throws IOException {
    return Files.walk(srcMainJavaPath)
        .filter(Files::isRegularFile)
        .filter(file -> isJavaFile(file))
        .collect(Collectors.toList());
  }

  public ParseResult<CompilationUnit> parse(Path file) throws IOException {
    return parser.parse(file);
  }

  private boolean isJavaFile(Path file) {
    return getFileExtension(file).equals(JAVA_FILE_EXTENSION);
  }

  private String getFileExtension(Path file) {
    String fileName = file.getFileName().toString();
    int dotIndex = fileName.lastIndexOf('.');

    return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
  }
}
