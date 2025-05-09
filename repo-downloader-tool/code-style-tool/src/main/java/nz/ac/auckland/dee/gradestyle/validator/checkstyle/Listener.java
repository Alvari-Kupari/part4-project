package nz.ac.auckland.dee.gradestyle.validator.checkstyle;

import static nz.ac.auckland.dee.gradestyle.validator.checkstyle.NamingCheck.CLASS_NOUN_KEY;
import static nz.ac.auckland.dee.gradestyle.validator.checkstyle.NamingCheck.METHOD_VERB_KEY;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import java.nio.file.Path;
import nz.ac.auckland.dee.gradestyle.validator.Type;
import nz.ac.auckland.dee.gradestyle.validator.Violation;
import nz.ac.auckland.dee.gradestyle.validator.Violations;

public class Listener implements AuditListener {
  private Violations violations;

  private Path file;

  public Listener(Violations violations) {
    this.violations = violations;
  }

  @Override
  public void fileStarted(AuditEvent event) {
    file = Path.of(event.getFileName());
  }

  @Override
  public void addError(AuditEvent event) {
    Type type = getViolationType(event.getViolation().getKey(), getModuleName(event));

    if (type != null) {
      violations.getViolations().add(new Violation(type, file, event.getLine()));
    }
  }

  private String getModuleName(AuditEvent event) {
    if (event.getModuleId() != null) {
      return event.getModuleId();
    }

    String[] components = event.getSourceName().split("\\.");
    String className = components[components.length - 1];
    String suffix = "Check";

    return className.substring(0, className.length() - suffix.length());
  }

  private Type getViolationType(String key, String module) {
    switch (key) {
      case "javadoc.parse.rule.error":
      case "javadoc.missed.html.close":
      case "javadoc.wrong.singleton.html.tag":
        return Type.Javadoc_Invalid;
    }

    switch (module) {
      case "FileTabCharacter":
        return Type.Formatting_IndentationTabs;
      case "LineLength":
        return Type.Formatting_LineLength;
      case "OuterTypeFilename":
        return Type.ClassNames_File;
      case "IllegalTokenText":
        return Type.Formatting_EscapedCharacter;
      case "AvoidEscapedUnicodeCharacters":
        return Type.Formatting_UnicodeCharacter;
      case "AvoidStarImport":
        return Type.Formatting_ImportStar;
      case "OneTopLevelClass":
        return Type.Formatting_OneTopLevel;
      case "NoLineWrap":
        return Type.Formatting_SingleLine;
      case "EmptyBlock":
        return Type.Useless_EmptyBlock;
      case "NeedBraces":
        return Type.Formatting_MissingBraces;
      case "LeftCurly":
      case "RightCurly":
        return Type.Formatting_BracePlacement;
      case "WhitespaceAfter":
      case "WhitespaceAround":
        return Type.Formatting_Padding;
      case "OneStatementPerLine":
        return Type.Formatting_MultiLine;
      case "MultipleVariableDeclarations":
        return Type.Formatting_MultipleDeclarations;
      case "ArrayTypeStyle":
        return Type.Formatting_ArrayStyle;
      case "FallThrough":
        return Type.Formatting_FallThrough;
      case "UpperEll":
        return Type.Formatting_UpperEll;
      case "ModifierOrder":
        return Type.Formatting_ModifierOrder;
      case "EmptyLineSeparator":
        return Type.Formatting_EmptyLine;
      case "SeparatorWrap":
      case "OperatorWrap":
        return Type.Formatting_LineWrapping;
      case "PackageName":
        return Type.PackageNames_Regex;
      case "PackageDeclaration":
        return Type.PackageNames_File;
      case "TypeName":
        return Type.ClassNames_Regex;
      case "MemberName":
      case "ParameterName":
      case "LambdaParameterName":
      case "CatchParameterName":
      case "LocalVariableName":
      case "PatternVariableName":
      case "RecordComponentName":
        return Type.VariableNames_Regex;
      case "ClassTypeParameterName":
      case "RecordTypeParameterName":
      case "MethodTypeParameterName":
      case "InterfaceTypeParameterName":
        return Type.VariableNames_TypeRegex;
      case "NoFinalizer":
        return Type.MethodNames_Finalize;
      case "GenericWhitespace":
        return Type.Formatting_Padding;
      case "Indentation":
        return Type.Formatting_IndentationSize;
      case "AbbreviationAsWordInNameVariables":
        return Type.VariableNames_Abbreviation;
      case "AbbreviationAsWordInNameClasses":
        return Type.ClassNames_Abbreviation;
      case "AbbreviationAsWordInNameMethods":
        return Type.MethodNames_Abbreviation;
      case "NoWhitespaceBeforeCaseDefaultColon":
        return Type.Formatting_Padding;
      case "OverloadMethodsDeclarationOrder":
        return Type.Ordering_Overloaded;
      case "VariableDeclarationUsageDistance":
        return Type.Ordering_DeclarationUsage;
      case "CustomImportOrder":
        switch (key) {
          case "custom.import.order":
            Type type = Type.Ordering_Import;
            type.setOrderingMessage(
                "Import is out of order. Ensure imports follow the specified custom order.");
            return type;
          case "custom.import.order.lex":
            type = Type.Ordering_Import;
            type.setOrderingMessage(
                "Import is out of lexicographical order. Ensure imports are alphabetically sorted"
                    + " within their group.");
            return type;
          case "custom.import.order.nonGroup.import":
            type = Type.Ordering_Import;
            type.setOrderingMessage(
                "Import does not belong to a defined group. Ensure all imports match the defined"
                    + " custom groups.");
            return type;
          case "custom.import.order.nonGroup.expected":
            type = Type.Ordering_Import;
            type.setOrderingMessage(
                "Import does not match the expected group. Check the custom group definitions.");
            return type;
          case "custom.import.order.line.separator":
            return Type.Formatting_MissingImportSeparator;
          case "custom.import.order.separated.internally":
            return Type.Formatting_ExcessImportSeparator;
        }
        break;

      case "MethodParamPad":
      case "NoWhitespaceBefore":
      case "ParenPad":
        return Type.Formatting_Padding;
      case "AnnotationLocation":
        return Type.Formatting_AnnotationLocation;
      case "NonEmptyAtclauseDescription":
        return Type.Javadoc_MissingTagDescription;
      case "InvalidJavadocPosition":
        return Type.Javadoc_Position;
      case "JavadocTagContinuationIndentation":
        return Type.Javadoc_TagDescriptionIndentation;
      case "SummaryJavadoc":
        switch (key) {
          case "summary.javaDoc":
          case "summary.javaDoc.missing":
            return Type.Javadoc_MissingSummary;
          case "summary.javaDoc.missing.period":
          case "summary.first.sentence":
            return Type.Javadoc_MissingPeriod;
        }
        break;
      case "JavadocParagraph":
        return Type.Javadoc_Paragraph;
      case "RequireEmptyLineBeforeBlockTagGroup":
        return Type.Javadoc_EmptyLine;
      case "AtclauseOrder":
        return Type.Javadoc_TagOrder;
      case "JavadocMethod":
        switch (key) {
          case "javadoc.expectedTag":
          case "javadoc.return.expected":
            return Type.Javadoc_MissingTag;
          case "javadoc.duplicateTag":
          case "javadoc.unusedTag":
          case "javadoc.unusedTagGeneral":
          case "javadoc.invalidInheritDoc":
            return Type.Javadoc_ExtraTag;
        }
        break;

      case "MethodName":
        return Type.MethodNames_Regex;
      case "SingleLineJavadoc":
        return Type.Javadoc_SingleLine;
      case "EmptyCatchBlock":
        return Type.EmptyCatchBlock;
      case "CommentsIndentation":
        return Type.Formatting_IndentationSize;

      case "Naming":
        switch (key) {
          case CLASS_NOUN_KEY:
            return Type.ClassNames_Noun;
          case METHOD_VERB_KEY:
            return Type.MethodNames_Verb;
        }
        break;
    }

    throw new IllegalArgumentException(
        "Unknown checkstyle module (" + module + ") or key (" + key + ")");
  }

  @Override
  public void auditStarted(AuditEvent event) {}

  @Override
  public void auditFinished(AuditEvent event) {}

  @Override
  public void fileFinished(AuditEvent event) {}

  @Override
  public void addException(AuditEvent event, Throwable throwable) {}
}
