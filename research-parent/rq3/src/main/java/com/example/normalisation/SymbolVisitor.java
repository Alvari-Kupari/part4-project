package com.example.normalisation;

import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.*;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import org.eclipse.aether.artifact.Artifact;

public class SymbolVisitor extends VoidVisitorAdapter<SymbolDatabase> {
  private String currentFile = "unknown";
  private DependencyDatabase dependencyDatabase;

  public SymbolVisitor(DependencyDatabase dependencyDatabase) {
    this.dependencyDatabase = dependencyDatabase;
  }

  public void setCurrentFile(String currentFile) {
    this.currentFile = currentFile;
  }

  @Override
  public void visit(MethodCallExpr n, SymbolDatabase symbolDatabase) {

    try {
      ResolvedMethodDeclaration r = n.resolve();
      String fqn = r.getQualifiedSignature(); // fully qualified with params
      addSymbol(
          symbolDatabase, "MethodCallExpr", r.declaringType().getQualifiedName(), r.getName(), n);
    } catch (Exception ignored) {
    }
    super.visit(n, symbolDatabase);
  }

  @Override
  public void visit(ObjectCreationExpr n, SymbolDatabase symbolDatabase) {

    try {
      ResolvedConstructorDeclaration r = n.resolve();
      addSymbol(
          symbolDatabase,
          "ObjectCreationExpr",
          r.declaringType().getQualifiedName(),
          r.getName(),
          n);
    } catch (Exception ignored) {
    }
    super.visit(n, symbolDatabase);
  }

  @Override
  public void visit(FieldAccessExpr n, SymbolDatabase symbolDatabase) {
    try {
      ResolvedValueDeclaration v = n.resolve();
      if (v.isField()) {
        ResolvedFieldDeclaration fld = v.asField();
        addSymbol(
            symbolDatabase,
            "FieldAccessExpr",
            fld.declaringType().getQualifiedName(),
            fld.getName(),
            n);
      }
    } catch (Exception ignored) {
    }
    super.visit(n, symbolDatabase);
  }

  @Override
  public void visit(MethodReferenceExpr n, SymbolDatabase db) {

    try {
      ResolvedMethodLikeDeclaration r = n.resolve();
      if (r instanceof ResolvedMethodDeclaration) {
        ResolvedMethodDeclaration m = (ResolvedMethodDeclaration) r;
        addSymbol(db, "MethodReferenceExpr", m.declaringType().getQualifiedName(), m.getName(), n);
      } else if (r instanceof ResolvedConstructorDeclaration) {
        ResolvedConstructorDeclaration c = (ResolvedConstructorDeclaration) r;
        addSymbol(db, "MethodReferenceExpr", c.declaringType().getQualifiedName(), c.getName(), n);
      }
    } catch (Exception ignored) {
    }
    super.visit(n, db);
  }

  @Override
  public void visit(ClassOrInterfaceType n, SymbolDatabase symbolDatabase) {

    try {
      ResolvedReferenceType rt = n.resolve().asReferenceType();
      addSymbol(
          symbolDatabase,
          "ClassOrInterfaceType",
          rt.getQualifiedName(),
          rt.getTypeDeclaration().get().getName(),
          n);

    } catch (Exception ignored) {
    }
    super.visit(n, symbolDatabase);
  }

  private void addSymbol(
      SymbolDatabase symbolDatabase,
      String symbolType,
      String className,
      String symbolName,
      com.github.javaparser.ast.Node n) {

    // Inner-class-safe lookup in DependencyDatabase
    ArtifactWrapper wrapper = dependencyDatabase.getArtifact(className);

    boolean isTransitive = wrapper.isTransitive();
    Artifact artifact = wrapper.getArtifact();

    Symbol symbol =
        new Symbol.Builder()
            .symbolType(symbolType)
            .className(className)
            .symbolName(symbolName)
            .usageLocation(currentFile == null ? "unknown" : currentFile)
            .lineNumber(n.getBegin().map(p -> p.line).orElse(-1))
            .library(artifact) // attach artifact here
            .isTransitive(isTransitive)
            .build();

    System.out.println("ADDING SYMBOL: " + symbol.toString());

    symbolDatabase.add(symbol);
  }
}
