package com.example.depanalyzer.analyzer.analysis.visitors;

import com.example.depanalyzer.analyzer.analysis.DependencyDatabase;
import com.example.depanalyzer.analyzer.report.Usage;
import com.example.depanalyzer.analyzer.report.UsageReport;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public class Visitor extends VoidVisitorAdapter<UsageReport> {

  private File file;
  private DependencyDatabase database;

  public Visitor(Path file, DependencyDatabase database) {
    this.file = file.toFile();
    this.database = database;
  }

  @Override
  public void visit(MethodCallExpr methodCallExpr, UsageReport report) {
    super.visit(methodCallExpr, report);

    ResolvedMethodDeclaration method;

    try {
      method = methodCallExpr.resolve();
    } catch (UnsolvedSymbolException e) {
      printUnsolvedSymbol(
          e, methodCallExpr.getNameAsString(), getFirstLine(methodCallExpr), "method call");
      return;
    }

    printSolvedSymbol(methodCallExpr.getNameAsString(), getFirstLine(methodCallExpr));

    checkIfTransitive(method, methodCallExpr, Usage.Type.METHOD_CALL, report);
  }

  @Override
  public void visit(NameExpr nameExpr, UsageReport report) {
    super.visit(nameExpr, report);

    ResolvedValueDeclaration value;

    try {
      value = nameExpr.resolve();
    } catch (UnsolvedSymbolException e) {
      printUnsolvedSymbol(e, nameExpr.getNameAsString(), getFirstLine(nameExpr), "Name Expr");
      return;
    }

    printSolvedSymbol(nameExpr.getNameAsString(), getFirstLine(nameExpr));

    checkIfTransitive(value, nameExpr, Usage.Type.NAME_EXPRESSION, report);
  }

  @Override
  public void visit(ObjectCreationExpr creationExpr, UsageReport report) {
    super.visit(creationExpr, report);

    ResolvedConstructorDeclaration constructor;

    try {
      constructor = creationExpr.resolve();

    } catch (UnsolvedSymbolException e) {
      printUnsolvedSymbol(
          e, creationExpr.getTypeAsString(), getFirstLine(creationExpr), "Object creation");
      return;
    }

    printSolvedSymbol(constructor.getClassName(), getFirstLine(creationExpr));

    checkIfTransitive(constructor, creationExpr, Usage.Type.OBJECT_CREATION, report);
  }

  @Override
  public void visit(FieldAccessExpr fieldAccessExpr, UsageReport report) {
    super.visit(fieldAccessExpr, report);

    ResolvedValueDeclaration value;

    try {
      value = fieldAccessExpr.resolve();
    } catch (UnsolvedSymbolException e) {
      printUnsolvedSymbol(
          e, fieldAccessExpr.getNameAsString(), getFirstLine(fieldAccessExpr), "Field access");
      return;
    }

    printSolvedSymbol(value.getName(), getFirstLine(fieldAccessExpr));

    checkIfTransitive(value, fieldAccessExpr, Usage.Type.FIELD_ACCESS, report);
  }

  @Override
  public void visit(ClassOrInterfaceType classType, UsageReport report) {
    super.visit(classType, report);

    ResolvedReferenceType type;

    try {
      type = classType.resolve().asReferenceType();
    } catch (UnsolvedSymbolException e) {
      printUnsolvedSymbol(e, classType.getNameAsString(), getFirstLine(classType), "Class type");
      return;
    }

    printSolvedSymbol(type.getQualifiedName(), getFirstLine(classType));

    ResolvedReferenceTypeDeclaration decl = type.getTypeDeclaration().get();

    checkIfTransitive(decl, classType, Usage.Type.CLASS_TYPE, report);
  }

  @Override
  public void visit(VariableDeclarator var, UsageReport report) {
    super.visit(var, report);

    try {
      var.getType().resolve();
    } catch (UnsolvedSymbolException e) {
      printUnsolvedSymbol(e, var.getTypeAsString(), getFirstLine(var), "variable type");
      return;
    }

    ResolvedReferenceTypeDeclaration typeDecl;
    try {
      typeDecl = var.getType().resolve().asReferenceType().getTypeDeclaration().get();
    } catch (Exception e) {
      return;
    }

    printSolvedSymbol(typeDecl.getQualifiedName(), getFirstLine(var));

    checkIfTransitive(typeDecl, var, Usage.Type.VARIABLE_DECLARATION, report);
  }

  private int getFirstLine(Node node) {
    return node.getRange().get().begin.line;
  }

  private int getLastLine(Node node) {
    return node.getRange().get().end.line;
  }

  private int getFirstColumn(Node node) {
    return node.getRange().get().begin.column;
  }

  private int getLastColumn(Node node) {
    return node.getRange().get().end.column;
  }

  private void printUnsolvedSymbol(UnsolvedSymbolException e, String node, int line, String note) {
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

  private void printSolvedSymbol(String node, int line) {
    // System.out.println("Resolved " + node + " at file: " + file + " at line: " + line);
  }

  private void checkIfTransitive(
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
