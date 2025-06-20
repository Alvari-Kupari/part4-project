package com.example.depanalyzer.analyzer.analysis.visitors;

import com.example.depanalyzer.analyzer.report.Usage;
import com.example.depanalyzer.analyzer.report.UsageReport;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

public class AnnotationVisitor extends VoidVisitorAdapter<UsageReport> {

  private final UsageAnalyzer helper;

  public AnnotationVisitor(UsageAnalyzer helper) {
    this.helper = helper;
  }

  @Override
  public void visit(ClassOrInterfaceDeclaration n, UsageReport report) {
    super.visit(n, report);
    handleAnnotations(n, report);
  }

  @Override
  public void visit(EnumDeclaration n, UsageReport report) {
    super.visit(n, report);
    handleAnnotations(n, report);
  }

  @Override
  public void visit(AnnotationDeclaration n, UsageReport report) {
    super.visit(n, report);
    handleAnnotations(n, report);
  }

  @Override
  public void visit(MethodDeclaration n, UsageReport report) {
    super.visit(n, report);
    handleAnnotations(n, report);
  }

  @Override
  public void visit(ConstructorDeclaration n, UsageReport report) {
    super.visit(n, report);
    handleAnnotations(n, report);
  }

  @Override
  public void visit(FieldDeclaration n, UsageReport report) {
    super.visit(n, report);
    handleAnnotations(n, report);
  }

  @Override
  public void visit(EnumConstantDeclaration n, UsageReport report) {
    super.visit(n, report);
    handleAnnotations(n, report);
  }

  @Override
  public void visit(Parameter n, UsageReport report) {
    super.visit(n, report);
    handleAnnotations(n, report);
  }

  @Override
  public void visit(TypeParameter n, UsageReport report) {
    super.visit(n, report);
    handleAnnotations(n, report);
  }

  @Override
  public void visit(RecordDeclaration n, UsageReport report) {
    super.visit(n, report);
    handleAnnotations(n, report); // Annotations on the record itself

    // Annotations on record components (parameters)
    for (Parameter component : n.getParameters()) {
      handleAnnotations(component, report);
    }
  }

  private void handleAnnotations(NodeWithAnnotations<?> annotated, UsageReport report) {
    for (var annotation : annotated.getAnnotations()) {
      try {
        ResolvedAnnotationDeclaration resolved = annotation.resolve();
        ResolvedReferenceTypeDeclaration decl = resolved.asReferenceType();
        helper.printSolvedSymbol(
            resolved.getQualifiedName(), helper.getFirstLine((Node) annotation));
        helper.checkIfTransitive(decl, (Node) annotation, Usage.Type.ANNOTATION, report);
      } catch (UnsolvedSymbolException e) {
        helper.printUnsolvedSymbol(
            e, annotation.getNameAsString(), helper.getFirstLine((Node) annotation), "annotation");
      } catch (Exception ignored) {
        System.out.println("Uknown exception: " + ignored.getMessage());
      }
    }
  }
}
