# RQ3 Breaking Change Client Analysis Implementation

This implementation provides comprehensive analysis for **RQ3: Are breaking changes in transitive dependencies more likely to occur in non-major releases compared to direct dependencies?**

## Overview

The implementation consists of several key components that work together to:

1. **Detect all breaking changes** (direct and transitive) between dependency versions
2. **Analyze client code usage** to identify which breaking changes are actually used
3. **Output structured CSV data** for analysis and manual inspection
4. **Support RQ3 research** with version classification and normalization metrics

## Key Components

### 1. Enhanced BreakingChangeUse Class (`BreakingChangeUse.java`)

Represents a breaking change and tracks whether it's used in client code:

```java
public class BreakingChangeUse {
  private final BreakingChange breakingChange;
  private final boolean isUsedInClient;
  private final String usageLocation;  // File path where used
  private final int lineNumber;        // Line number where used
  private final String usageContext;   // Specific symbol used
  private final String usageType;      // Type of usage (method, field, etc.)
}
```

**Factory methods:**

- `BreakingChangeUse.unused(BreakingChange)` - For detected but unused breaking changes
- `BreakingChangeUse.used(...)` - For breaking changes actually used in client code

### 2. Enhanced SymbolChecker (`parsing/SymbolChecker.java`)

Intelligently detects breaking change usage in client code:

```java
public class SymbolChecker {
  // Maps symbols to breaking changes for efficient lookup
  private final Map<String, BreakingChange> breakingChangesBySymbol;

  // Initialize with breaking changes to look for
  public void setBreakingChanges(List<BreakingChange> direct, List<BreakingChange> transitive);

  // Check if a symbol matches any breaking change
  public void checkNameUsage(String fqn, List<BreakingChangeUse> uses, String exprType,
                            String location, int line);
}
```

**Features:**

- **Multiple lookup strategies**: Direct FQN matching, class name matching, member name matching
- **Inner class support**: Handles nested class scenarios
- **Flexible matching**: Works with partial names and method signatures

### 3. Enhanced Visitor (`parsing/Visitor.java`)

AST visitor that detects symbol usage with error handling:

```java
public class Visitor extends VoidVisitorAdapter<List<BreakingChangeUse>> {
  // Visits different expression types:
  // - MethodCallExpr (method calls)
  // - ObjectCreationExpr (constructor calls)
  // - FieldAccessExpr (field access)
  // - NameExpr (variable/type references)
  // - MethodReferenceExpr (method references)
  // - ClassOrInterfaceType (type usage)
}
```

**Features:**

- **Robust error handling**: Continues analysis even when symbols can't be resolved
- **Location tracking**: Captures file path and line numbers
- **Multiple resolution strategies**: Tries different approaches when primary resolution fails

### 4. Enhanced ClientAnalysis (`ClientAnalysis.java`)

Main analysis orchestrator:

```java
public class ClientAnalysis {
  public List<BreakingChangeUse> execute() throws IOException, PomException {
    // 1. Initialize symbol checker with breaking changes
    // 2. Parse project dependencies and setup classpath
    // 3. Analyze all Java files for breaking change usage
    // 4. Return comprehensive list of used/unused breaking changes
  }
}
```

**Process:**

1. **Initialize**: Set up symbol checker with all breaking changes
2. **Parse dependencies**: Resolve project dependencies for classpath
3. **Analyze files**: Visit each Java file to detect usage
4. **Aggregate results**: Combine used and unused breaking changes

### 5. CSV Output System

#### Main CSV Writer (`CsvWriter.java`)

Outputs breaking changes used in client code:

**Columns:**

- `Library_Name`, `Old_Version`, `New_Version`
- `Class_Name`, `Member_Name`, `Change_Type`, `Description`
- `Binary_Compatible`, `Source_Compatible`, `Is_Transitive`, `Depth`
- `Direct_Parent_Dependency`
- `Is_Used_In_Client`, `Usage_Location`, `Usage_Line`, `Usage_Context`, `Usage_Type`
- `Unique_Symbols_Count`, `Affected_Symbols_Count` (for normalization)

#### All Breaking Changes CSV Writer (`AllBreakingChangesCsvWriter.java`)

Outputs all detected breaking changes (regardless of usage):

**Additional columns:**

- `Is_Major_Release`, `Release_Type` (for RQ3 analysis)

### 6. RQ3 Analysis Utilities (`util/RQ3AnalysisUtil.java`)

Helper utilities for RQ3 research:

```java
public class RQ3AnalysisUtil {
  // Classify breaking changes by dependency type and release type
  public static BreakingChangeClassification analyzeForRQ3(
      List<BreakingChange> direct, List<BreakingChange> transitive);

  // Determine if version change is major
  public static boolean isMajorVersionChange(String oldVer, String newVer);

  // Determine release type (MAJOR, MINOR, PATCH)
  public static String determineReleaseType(String oldVer, String newVer);
}
```

## Output Files

The analysis generates three types of CSV files:

### 1. `{repo}_{submodule}.csv`

- **Purpose**: Breaking changes actually used in client code
- **Use case**: Understanding real impact on client applications

### 2. `{repo}_{submodule}-all.csv`

- **Purpose**: All breaking changes (used and unused) with usage details
- **Use case**: Complete analysis including unused breaking changes

### 3. `{repo}_{submodule}-breaking-changes.csv`

- **Purpose**: All detected breaking changes (middle-stage output)
- **Use case**: Manual inspection and validation of breaking change detection

## Usage

### Running the Analysis

```bash
# The main script will automatically generate all three CSV types
java -cp ... com.example.Script
```

### Testing with Known Examples

```java
// Test with a known library (e.g., JUnit)
java -cp ... com.example.test.ClientAnalysisTest
```

### Manual Testing

You can manually add breaking changes to the repository to test if your client analysis correctly detects them:

1. Find a breaking change from the `-breaking-changes.csv` file
2. Add usage of that breaking change to your client code
3. Re-run the analysis
4. Verify it appears as "used" in the output

## RQ3 Research Support

### Version Classification

The implementation automatically classifies version changes:

- **Major**: X.y.z → (X+1).y.z
- **Minor**: x.Y.z → x.(Y+1).z
- **Patch**: x.y.Z → x.y.(Z+1)

### Metrics for Normalization

- **Unique_Symbols_Count**: Number of unique symbols affected by the breaking change
- **Affected_Symbols_Count**: Potential impact scope (extensible for more sophisticated metrics)
- **Depth**: Dependency depth (1 = direct, >1 = transitive)

### Analysis Questions

The output data supports answering:

1. **Are transitive breaking changes more likely in non-major releases?**

   - Compare `Is_Transitive=true` vs `Is_Transitive=false`
   - Group by `Release_Type` (MAJOR vs MINOR/PATCH)

2. **What's the usage rate of breaking changes?**

   - Compare rows with `Is_Used_In_Client=true` vs `false`

3. **How does depth affect breaking change likelihood?**
   - Analyze by `Depth` values

## Configuration

### Paths (in Script.java)

```java
public static final Path csvFolder = Paths.get("/Users/tonyyin/Desktop/Projects/csv");
private static final Path reposFolder = Paths.get("/Users/tonyyin/Desktop/Projects/repo");
```

### Logging

The implementation provides detailed logging at each stage:

- Repository and submodule processing
- Breaking change detection counts
- Client analysis progress
- Final statistics

## Error Handling

The implementation includes robust error handling:

- **Symbol resolution failures**: Continues analysis with fallback strategies
- **Parse errors**: Logs warnings but continues with other files
- **Dependency resolution errors**: Tracked by FailureTracker
- **Missing files**: Graceful handling with informative logging

## Extensibility

The modular design allows for easy extension:

- **Additional AST node types**: Add new visit methods to Visitor
- **Custom symbol matching**: Extend SymbolChecker logic
- **Different output formats**: Create new CSV writers
- **Advanced metrics**: Extend RQ3AnalysisUtil

## Performance Considerations

- **Efficient symbol lookup**: HashMap-based symbol resolution
- **Incremental file processing**: Progress logging for large projects
- **Memory management**: Processes files individually rather than loading all at once
- **Early termination**: Skips client analysis when no breaking changes found

## Troubleshooting

### Common Issues

1. **No breaking changes detected**: Check dependency version ranges and JAPICMP configuration
2. **Symbols not resolved**: Verify classpath setup and dependency resolution
3. **Empty usage results**: Check that breaking changes are actually used in the analyzed code
4. **Performance issues**: Consider filtering to smaller subsets for initial testing

### Debugging Tips

1. **Enable detailed logging**: Check console output for analysis progress
2. **Inspect intermediate CSVs**: Use `-breaking-changes.csv` to verify detection
3. **Test with known examples**: Use the ClientAnalysisTest with JUnit
4. **Manual verification**: Add deliberate breaking change usage to test detection
