package com.example.parsing;

import com.example.BreakingChangeUse;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.*;
import com.github.javaparser.resolution.types.ResolvedType;
import java.util.List;

public class Visitor extends VoidVisitorAdapter<List<BreakingChangeUse>> {
  private final SymbolChecker checker;
  private String currentFile = "unknown";

  public Visitor(SymbolChecker checker) {
    this.checker = checker;
  }

  public void setCurrentFile(String currentFile) {
    this.currentFile = currentFile;
  }

  @Override
  public void visit(MethodCallExpr n, List<BreakingChangeUse> uses) {
    try {
      ResolvedMethodDeclaration resolved = n.resolve();
      String fqn = resolved.getQualifiedSignature();
      int line = n.getBegin().map(pos -> pos.line).orElse(-1);
      checker.checkNameUsage(fqn, uses, "MethodCallExpr", currentFile, line);
    } catch (Exception e) {
      // Symbol couldn't be resolved - this is common and not necessarily an error
      // Continue processing other nodes
    }
    super.visit(n, uses);
  }

  @Override
  public void visit(ObjectCreationExpr n, List<BreakingChangeUse> uses) {
    try {
      ResolvedConstructorDeclaration resolved = n.resolve();
      String fqn = resolved.getQualifiedSignature();
      int line = n.getBegin().map(pos -> pos.line).orElse(-1);
      checker.checkNameUsage(fqn, uses, "ObjectCreationExpr", currentFile, line);
    } catch (Exception e) {
      // Try to get type information instead
      try {
        String typeName = n.getType().getNameAsString();
        int line = n.getBegin().map(pos -> pos.line).orElse(-1);
        checker.checkNameUsage(typeName, uses, "ObjectCreationExpr", currentFile, line);
      } catch (Exception e2) {
        // Ignore if we can't resolve
      }
    }
    super.visit(n, uses);
  }

  @Override
  public void visit(FieldAccessExpr n, List<BreakingChangeUse> uses) {
    try {
      ResolvedValueDeclaration resolved = n.resolve();
      String fqn = resolved.getType().describe();
      int line = n.getBegin().map(pos -> pos.line).orElse(-1);
      checker.checkNameUsage(fqn, uses, "FieldAccessExpr", currentFile, line);

      // Also check the field name itself
      String fieldName = n.getNameAsString();
      checker.checkNameUsage(fieldName, uses, "FieldAccessExpr", currentFile, line);
    } catch (Exception e) {
      // Try just the field name
      try {
        String fieldName = n.getNameAsString();
        int line = n.getBegin().map(pos -> pos.line).orElse(-1);
        checker.checkNameUsage(fieldName, uses, "FieldAccessExpr", currentFile, line);
      } catch (Exception e2) {
        // Ignore if we can't resolve
      }
    }
    super.visit(n, uses);
  }

  @Override
  public void visit(NameExpr n, List<BreakingChangeUse> uses) {
    try {
      ResolvedValueDeclaration resolved = n.resolve();
      String fqn = resolved.getType().describe();
      int line = n.getBegin().map(pos -> pos.line).orElse(-1);
      checker.checkNameUsage(fqn, uses, "NameExpr", currentFile, line);
    } catch (Exception e) {
      // Try just the name
      try {
        String name = n.getNameAsString();
        int line = n.getBegin().map(pos -> pos.line).orElse(-1);
        checker.checkNameUsage(name, uses, "NameExpr", currentFile, line);
      } catch (Exception e2) {
        // Ignore if we can't resolve
      }
    }
    super.visit(n, uses);
  }

  @Override
  public void visit(MethodReferenceExpr n, List<BreakingChangeUse> uses) {
    try {
      ResolvedMethodLikeDeclaration resolved = n.resolve();
      String fqn = resolved.getQualifiedSignature();
      int line = n.getBegin().map(pos -> pos.line).orElse(-1);
      checker.checkNameUsage(fqn, uses, "MethodReferenceExpr", currentFile, line);
    } catch (Exception e) {
      // Ignore if we can't resolve
    }
    super.visit(n, uses);
  }

  @Override
  public void visit(ClassOrInterfaceType n, List<BreakingChangeUse> uses) {
    try {
      ResolvedType resolved = n.resolve();
      String fqn = resolved.describe();
      int line = n.getBegin().map(pos -> pos.line).orElse(-1);
      checker.checkNameUsage(fqn, uses, "ClassOrInterfaceType", currentFile, line);
    } catch (Exception e) {
      // Try just the type name
      try {
        String typeName = n.getNameAsString();
        int line = n.getBegin().map(pos -> pos.line).orElse(-1);
        checker.checkNameUsage(typeName, uses, "ClassOrInterfaceType", currentFile, line);
      } catch (Exception e2) {
        // Ignore if we can't resolve
      }
    }
    super.visit(n, uses);
  }
}
