package com.example.depanalyzer.analyzer.analysis;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {
  private static final String SRC_MAIN_JAVA = "src/main/java";
  private static final String JAVA_FILE_EXTENSION = "java";
  private JavaParser parser;
  private Path srcMainJavaPath;

  public Parser(String repoPath) throws IOException {
    srcMainJavaPath = Path.of(repoPath, SRC_MAIN_JAVA);
    ParserConfiguration config = new ParserConfiguration();

    config.setLanguageLevel(LanguageLevel.JAVA_21);
    JavaParserTypeSolver javaParserTypeSolver = new JavaParserTypeSolver(srcMainJavaPath, config);

    CombinedTypeSolver typeSolver = new CombinedTypeSolver();
    typeSolver.add(new ReflectionTypeSolver());
    typeSolver.add(javaParserTypeSolver);

    // Now set the resolver
    JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
    config.setSymbolResolver(symbolSolver);

    parser = new JavaParser(config);
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
