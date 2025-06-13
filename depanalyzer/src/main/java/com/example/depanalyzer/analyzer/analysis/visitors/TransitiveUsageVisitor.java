package com.example.depanalyzer.analyzer.analysis.visitors;

import com.example.depanalyzer.analyzer.report.Usage;
import com.example.depanalyzer.analyzer.report.UsageReport;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.nio.file.Path;

public class TransitiveUsageVisitor extends VoidVisitorAdapter<UsageReport> {

  private Path file;

  public TransitiveUsageVisitor(Path file) {
    this.file = file;
  }

  @Override
  public void visit(ClassOrInterfaceDeclaration n, UsageReport report) {
    super.visit(n, report);
    report.addUsage(new Usage(n.getNameAsString() + " at file: " + file.toString()));
  }
}
