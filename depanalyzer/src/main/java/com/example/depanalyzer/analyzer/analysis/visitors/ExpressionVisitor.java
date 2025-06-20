package com.example.depanalyzer.analyzer.analysis.visitors;

import com.example.depanalyzer.analyzer.report.Usage;
import com.example.depanalyzer.analyzer.report.UsageReport;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;

public class ExpressionVisitor extends VoidVisitorAdapter<UsageReport> {

  private final UsageAnalyzer helper;

  public ExpressionVisitor(UsageAnalyzer helper) {
    this.helper = helper;
  }

  @Override
  public void visit(MethodCallExpr methodCallExpr, UsageReport report) {
    super.visit(methodCallExpr, report);

    try {
      ResolvedMethodDeclaration method = methodCallExpr.resolve();
      helper.printSolvedSymbol(
          methodCallExpr.getNameAsString(), helper.getFirstLine(methodCallExpr));
      helper.checkIfTransitive(method, methodCallExpr.getName(), Usage.Type.METHOD_CALL, report);
    } catch (UnsolvedSymbolException e) {
      helper.printUnsolvedSymbol(
          e, methodCallExpr.getNameAsString(), helper.getFirstLine(methodCallExpr), "method call");
    }
  }

  @Override
  public void visit(NameExpr nameExpr, UsageReport report) {
    super.visit(nameExpr, report);

    try {
      ResolvedValueDeclaration value = nameExpr.resolve();
      helper.printSolvedSymbol(nameExpr.getNameAsString(), helper.getFirstLine(nameExpr));
      helper.checkIfTransitive(value, nameExpr, Usage.Type.NAME_EXPRESSION, report);
    } catch (UnsolvedSymbolException e) {
      helper.printUnsolvedSymbol(
          e, nameExpr.getNameAsString(), helper.getFirstLine(nameExpr), "Name Expr");
    }
  }

  @Override
  public void visit(ObjectCreationExpr creationExpr, UsageReport report) {
    super.visit(creationExpr, report);

    try {
      ResolvedConstructorDeclaration constructor = creationExpr.resolve();
      helper.printSolvedSymbol(constructor.getClassName(), helper.getFirstLine(creationExpr));
      helper.checkIfTransitive(constructor, creationExpr, Usage.Type.OBJECT_CREATION, report);
    } catch (UnsolvedSymbolException e) {
      helper.printUnsolvedSymbol(
          e, creationExpr.getTypeAsString(), helper.getFirstLine(creationExpr), "Object creation");
    }
  }

  @Override
  public void visit(FieldAccessExpr fieldAccessExpr, UsageReport report) {
    super.visit(fieldAccessExpr, report);

    try {
      ResolvedValueDeclaration value = fieldAccessExpr.resolve();
      helper.printSolvedSymbol(value.getName(), helper.getFirstLine(fieldAccessExpr));
      helper.checkIfTransitive(value, fieldAccessExpr, Usage.Type.FIELD_ACCESS, report);
    } catch (UnsolvedSymbolException e) {
      helper.printUnsolvedSymbol(
          e,
          fieldAccessExpr.getNameAsString(),
          helper.getFirstLine(fieldAccessExpr),
          "Field access");
    }
  }

  @Override
  public void visit(ClassOrInterfaceType classType, UsageReport report) {
    super.visit(classType, report);

    try {
      ResolvedReferenceType type = classType.resolve().asReferenceType();
      helper.printSolvedSymbol(type.getQualifiedName(), helper.getFirstLine(classType));
      ResolvedReferenceTypeDeclaration decl = type.getTypeDeclaration().get();
      helper.checkIfTransitive(decl, classType, Usage.Type.CLASS_TYPE, report);
    } catch (UnsolvedSymbolException e) {
      helper.printUnsolvedSymbol(
          e, classType.getNameAsString(), helper.getFirstLine(classType), "Class type");
    }
  }

  @Override
  public void visit(VariableDeclarator var, UsageReport report) {
    super.visit(var, report);

    try {
      var.getType().resolve();
      ResolvedReferenceTypeDeclaration typeDecl =
          var.getType().resolve().asReferenceType().getTypeDeclaration().get();
      helper.printSolvedSymbol(typeDecl.getQualifiedName(), helper.getFirstLine(var));
      helper.checkIfTransitive(typeDecl, var, Usage.Type.VARIABLE_DECLARATION, report);
    } catch (UnsolvedSymbolException e) {
      helper.printUnsolvedSymbol(
          e, var.getTypeAsString(), helper.getFirstLine(var), "variable type");
    } catch (Exception ignored) {
      // Skip invalid type resolution
    }
  }
}
