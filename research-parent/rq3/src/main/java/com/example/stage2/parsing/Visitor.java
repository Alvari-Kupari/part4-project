package com.example.stage2.parsing;

import com.example.depanalyzer.analyzer.analysis.Parser;
import com.example.stage2.SymbolChecker;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.*;
import com.github.javaparser.resolution.types.ResolvedType;

public class Visitor extends VoidVisitorAdapter<Void> {
  Parser a;
  private final SymbolChecker checker;

  public Visitor(SymbolChecker checker) {
    this.checker = checker;
  }

  @Override
  public void visit(MethodCallExpr n, Void arg) {
    ResolvedMethodDeclaration resolved = n.resolve();
    String fqn = resolved.getQualifiedSignature();
    checker.checkNameUsage(fqn, "MethodCallExpr");
    super.visit(n, arg);
  }

  @Override
  public void visit(ObjectCreationExpr n, Void arg) {
    ResolvedConstructorDeclaration resolved = n.resolve();
    String fqn = resolved.getQualifiedSignature();
    checker.checkNameUsage(fqn, "ObjectCreationExpr");
    super.visit(n, arg);
  }

  @Override
  public void visit(FieldAccessExpr n, Void arg) {
    ResolvedValueDeclaration resolved = n.resolve();
    String fqn = resolved.getType().describe();
    checker.checkNameUsage(fqn, "FieldAccessExpr");
    super.visit(n, arg);
  }

  @Override
  public void visit(NameExpr n, Void arg) {
    ResolvedValueDeclaration resolved = n.resolve();
    String fqn = resolved.getType().describe();
    checker.checkNameUsage(fqn, "NameExpr");
    super.visit(n, arg);
  }

  @Override
  public void visit(MethodReferenceExpr n, Void arg) {
    ResolvedMethodLikeDeclaration resolved = n.resolve();
    String fqn = resolved.getQualifiedSignature();
    checker.checkNameUsage(fqn, "MethodReferenceExpr");
    super.visit(n, arg);
  }

  @Override
  public void visit(ClassOrInterfaceType n, Void arg) {
    ResolvedType resolved = n.resolve();
    String fqn = resolved.describe();
    checker.checkNameUsage(fqn, "ClassOrInterfaceType");
    super.visit(n, arg);
  }
}
