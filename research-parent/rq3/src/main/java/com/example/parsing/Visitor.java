package com.example.parsing;

import com.example.BreakingChangeUse;
import com.example.depanalyzer.analyzer.analysis.Parser;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.*;
import com.github.javaparser.resolution.types.ResolvedType;
import java.util.List;

public class Visitor extends VoidVisitorAdapter<List<BreakingChangeUse>> {
  Parser a;
  private final SymbolChecker checker;

  public Visitor(SymbolChecker checker) {
    this.checker = checker;
  }

  @Override
  public void visit(MethodCallExpr n, List<BreakingChangeUse> uses) {
    ResolvedMethodDeclaration resolved = n.resolve();
    String fqn = resolved.getQualifiedSignature();
    checker.checkNameUsage(fqn, uses, "MethodCallExpr");
    super.visit(n, uses);
  }

  @Override
  public void visit(ObjectCreationExpr n, List<BreakingChangeUse> uses) {
    ResolvedConstructorDeclaration resolved = n.resolve();
    String fqn = resolved.getQualifiedSignature();
    checker.checkNameUsage(fqn, uses, "ObjectCreationExpr");
    super.visit(n, uses);
  }

  @Override
  public void visit(FieldAccessExpr n, List<BreakingChangeUse> uses) {
    ResolvedValueDeclaration resolved = n.resolve();
    String fqn = resolved.getType().describe();
    checker.checkNameUsage(fqn, uses, "FieldAccessExpr");
    super.visit(n, uses);
  }

  @Override
  public void visit(NameExpr n, List<BreakingChangeUse> uses) {
    ResolvedValueDeclaration resolved = n.resolve();
    String fqn = resolved.getType().describe();
    checker.checkNameUsage(fqn, uses, "NameExpr");
    super.visit(n, uses);
  }

  @Override
  public void visit(MethodReferenceExpr n, List<BreakingChangeUse> uses) {
    ResolvedMethodLikeDeclaration resolved = n.resolve();
    String fqn = resolved.getQualifiedSignature();
    checker.checkNameUsage(fqn, uses, "MethodReferenceExpr");
    super.visit(n, uses);
  }

  @Override
  public void visit(ClassOrInterfaceType n, List<BreakingChangeUse> uses) {
    ResolvedType resolved = n.resolve();
    String fqn = resolved.describe();
    checker.checkNameUsage(fqn, uses, "ClassOrInterfaceType");
    super.visit(n, uses);
  }
}
