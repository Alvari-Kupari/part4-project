package com.example.depanalyzer.analyzer.analysis.visitors;

import com.example.depanalyzer.analyzer.analysis.DependencyDatabase;
import com.example.depanalyzer.analyzer.report.Usage;
import com.example.depanalyzer.analyzer.report.UsageReport;
import com.github.javaparser.ast.Node;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public class UsageAnalyzer {
  public final File file;
  public final DependencyDatabase database;

  public UsageAnalyzer(Path file, DependencyDatabase database) {
    this.file = file.toFile();
    this.database = database;
  }

  public int getFirstLine(Node node) {
    return node.getRange().get().begin.line;
  }

  public int getLastLine(Node node) {
    return node.getRange().get().end.line;
  }

  public int getFirstColumn(Node node) {
    return node.getRange().get().begin.column;
  }

  public int getLastColumn(Node node) {
    return node.getRange().get().end.column;
  }

  public void printUnsolvedSymbol(UnsolvedSymbolException e, String node, int line, String note) {
    // System.out.println(
    //     e.getMessage()
    //         + " of type: "
    //         + node
    //         + " at file: "
    //         + file
    //         + " at line: "
    //         + line
    //         + ". "
    //         + note);
  }

  public void printSolvedSymbol(String node, int line) {
    // System.out.println("Resolved " + node + " at file: " + file + " at line: " + line);
  }

  public void checkIfTransitive(
      ResolvedDeclaration resolvedDecl, Node node, Usage.Type type, UsageReport report) {

    Optional<String> result = database.checkIfTransitive(resolvedDecl);

    if (result.isEmpty()) {
      return;
    }

    String libName = result.get();

    int lineNumber = getFirstLine(node);
    int startColumn = getFirstColumn(node);
    int endColumn = getLastColumn(node);

    report.addUsage(new Usage(lineNumber, file, type, startColumn, endColumn, libName));
  }
}
