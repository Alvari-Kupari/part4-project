package com.example.parsing;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;

public class Visitor extends VoidVisitorAdapter<SymbolChecker> {

  private final SymbolChecker checker;

  public Visitor(SymbolChecker checker) {
    this.checker = checker;
  }

  @Override
  public void visit(ClassOrInterfaceType n, SymbolChecker arg) {
    try {
      ResolvedReferenceType resolved = n.resolve().asReferenceType();
      checker.addClassUse(resolved.getQualifiedName());
    } catch (Throwable ignored) {
    }
    super.visit(n, arg);
  }

  @Override
  public void visit(ObjectCreationExpr n, SymbolChecker arg) {
    try {
      ResolvedReferenceTypeDeclaration decl = n.getType().resolve().asReferenceType().getTypeDeclaration().orElse(null);
      if (decl != null) {
        String fqn = decl.getQualifiedName();
        checker.addClassUse(fqn);
        checker.addMethodUse(fqn + "#<init>");
      }
    } catch (Throwable ignored) {
    }
    super.visit(n, arg);
  }

  @Override
  public void visit(MethodCallExpr n, SymbolChecker arg) {
    try {
      ResolvedMethodDeclaration rmd = n.resolve();
      String owner = rmd.declaringType().getQualifiedName();
      checker.addMethodUse(owner + "#" + rmd.getName());
    } catch (Throwable ignored) {
    }
    super.visit(n, arg);
  }

  @Override
  public void visit(FieldAccessExpr n, SymbolChecker arg) {
    try {
      // Try to resolve the scope's type; this catches most field usages
      String owner = tryResolveTypeFqn(n.getScope());
      if (owner != null) {
        checker.addFieldUse(owner + "#" + n.getNameAsString());
      }
    } catch (Throwable ignored) {
    }
    super.visit(n, arg);
  }

  @Override
  public void visit(NameExpr n, SymbolChecker arg) {
    // Sometimes fields are bare NameExpr; leave to symbol solver complexity, skip here.
    super.visit(n, arg);
  }

  @Override
  public void visit(ClassOrInterfaceDeclaration n, SymbolChecker arg) {
    // record self FQN if resolvable (helps when client defines types that extend libs)
    try {
      String fqn = n.getFullyQualifiedName().orElse(null);
      if (fqn != null) checker.addClassUse(fqn);
    } catch (Throwable ignored) {
    }
    super.visit(n, arg);
  }

  private static String tryResolveTypeFqn(Expression expr) {
    try {
      ResolvedReferenceType type = expr.calculateResolvedType().asReferenceType();
      return type.getQualifiedName();
    } catch (UnsolvedSymbolException | UnsupportedOperationException | IllegalStateException e) {
      return null;
    }
  }
}
