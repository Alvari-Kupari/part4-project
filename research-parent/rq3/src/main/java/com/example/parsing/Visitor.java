package com.example.parsing;

import com.example.BreakingChangeUse;
import com.github.javaparser.ast.expr.*;
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
  public void visit(VariableDeclarationExpr n, List<BreakingChangeUse> uses) {
    // Handle variable declarations like "XmlFrameDecoder test0;"
    n.getVariables().forEach(variable -> {
      try {
        // Try to resolve the type of the variable
        ResolvedType resolvedType = variable.getType().resolve();
        String fqn = resolvedType.describe();
        int line = variable.getBegin().map(pos -> pos.line).orElse(-1);
        checker.checkNameUsage(fqn, uses, "VariableDeclarationExpr", currentFile, line);
      } catch (Exception e) {
        // Try just the type name if resolution fails
        try {
          String typeName = variable.getType().asString();
          int line = variable.getBegin().map(pos -> pos.line).orElse(-1);
          checker.checkNameUsage(typeName, uses, "VariableDeclarationExpr", currentFile, line);
        } catch (Exception e2) {
          // Ignore if we can't get type information
        }
      }
    });
    super.visit(n, uses);
  }
}
