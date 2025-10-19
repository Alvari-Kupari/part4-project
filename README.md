# Research Compendium: Understanding and Resolving Transitive Dependency Conflicts

**Project Number:** 106  
**Team Members:** Tony Yin (tyin363), Alvari Kupari (akup390)  
**Supervisors:** Dr. Kelly Blincoe, Dr. Valerio Terragni, Dhanushka Jayasuriya  
**Course:** SOFTENG 700 - Research Project Part 4  
**Date:** October 2025

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Research Questions](#research-questions)
3. [Repository Structure](#repository-structure)
4. [Methodology](#methodology)
5. [Tools and Technologies](#tools-and-technologies)
6. [Data Collection](#data-collection)
7. [Analysis and Results](#analysis-and-results)
8. [Replication Guide](#replication-guide)
9. [Key Findings](#key-findings)
10. [Future Work](#future-work)
11. [Individual Contributions](#individual-contributions)

---

## Project Overview

This research project investigates the nature and impact of dependency conflicts in Java projects, with a particular focus on transitive dependencies and breaking changes. The study analyzes **1,030 open-source Maven repositories** containing **5,827 valid submodules** to understand how frequently dependency conflicts arise, their characteristics, and their actual impact on client code.

### Background

Modern software development heavily relies on third-party libraries managed through package managers such as Maven, Gradle, or npm. These external components enable developers to reuse functionality and focus on core application logic. However, this reliance introduces complex networks of dependencies—both **direct** (explicitly declared) and **transitive** (indirectly included through other dependencies). While transitive dependencies simplify configuration, they also obscure the underlying dependency structure, making it difficult for developers to maintain visibility, manage updates, and prevent unexpected failures.

Dependency-related issues have emerged as a major challenge in modern software ecosystems. Version conflicts, outdated libraries, and breaking changes in transitive dependencies can lead to build errors, runtime crashes, or subtle behavioral inconsistencies. Prior research has shown that approximately one in five breaking changes in Java projects originates from transitive dependencies, emphasizing the hidden risks they pose.

### Objectives

- Quantify the frequency of dependency version conflicts in real-world Java projects
- Analyze breaking changes in both direct and transitive dependencies
- Assess the actual impact of breaking changes on client code
- Develop tooling to help developers identify and manage transitive dependency issues
- Provide empirical evidence for dependency management best practices

---

## Research Questions

### RQ1: Dependency Conflict Frequency

**How frequently do conflicting versions of the same dependency arise within dependency trees?**

This question examines the prevalence of version conflicts in Java dependency trees, analyzing both direct and transitive dependencies across our dataset of 1,030 repositories containing 5,827 valid submodules.

**Key Metrics:**

- Conflict frequency per project and submodule
- Depth at which conflicts occur in dependency trees
- Distribution between direct and transitive dependency conflicts
- Most conflict-prone libraries

**Key Findings:**

- **45.8%** of submodules contain version conflicts
- **99.6%** of conflicts occur in transitive dependencies
- Conflicts are concentrated in popular libraries (e.g., Jackson, gRPC, SLF4J)

---

### RQ2: Breaking Changes in Direct vs. Transitive Dependencies

**Are breaking changes in transitive dependencies that affect client code more likely to occur compared to direct dependencies?**

This question investigates whether breaking changes that actually impact client code differ between direct and transitive dependencies, focusing on **used breaking changes** rather than all detected changes.

**Key Metrics:**

- Breaking change detection using bytecode comparison (Japicmp)
- Client code usage verification using static analysis (JavaParser)
- Normalized usage rates accounting for dependency exposure
- Comparison of direct vs. transitive dependency impact

**Key Findings:**

- Breaking changes from **transitive dependencies** are approximately **1.5 times more likely** to impact client code compared to direct dependencies
- Only **0.20%** of all detected breaking changes actually affect client code
- **73.5%** of breaking changes originate from transitive dependencies
- **68.8%** of breaking changes are method-related

---

### Tool Development: Transitive Dependency Usage Detector

**Developed as a practical byproduct to address research needs**

To support our analysis and provide value to developers, we developed a Java-based Maven plugin that detects and reports transitive dependency usage in Maven projects.

**Tool Features:**

- Analyzes complete dependency trees to identify transitive dependencies
- Detects which transitive dependencies are actually used in client code
- Reports file locations, code snippets, and dependency metadata
- Generates detailed reports for proactive dependency management

**Purpose:**

- Enhances visibility into hidden transitive relationships
- Helps developers understand which transitive dependencies their code relies on
- Supports informed decision-making about dependency updates
- Enables proactive monitoring of potential breaking changes

**Note:** While this tool was essential for our RQ2 analysis, it is presented as a practical contribution rather than a separate research question.

---

## Repository Structure

```
part4-project/
│
├── README.md                          # This file - main documentation
├── .gitignore                         # Git ignore rules
├── repo_check.csv                     # Repository validation data
│
├── analysis_results/                  # Generated analysis outputs
│   ├── change_types_distribution.png  # Visual: distribution of change types
│   ├── change_types_summary.csv       # Summary statistics for change types
│   ├── most_problematic_libraries.csv # Libraries with most breaking changes
│   ├── overview_dashboard.png         # Overall analysis dashboard
│   ├── project_level_analysis.png     # Per-project analysis visualization
│   ├── project_level_summary.csv      # Per-project summary statistics
│   ├── summary_statistics.csv         # Overall summary statistics
│   └── top_problematic_libraries.png  # Visualization of problematic libraries
│
├── data/                              # All research data
│   ├── download_analysis/             # Repository download and filtering analysis
│   │   ├── commit_numbers.png         # Commit distribution analysis
│   │   ├── conflict_patterns.csv      # Conflict pattern data
│   │   ├── logs-1000-repos.csv        # Download logs for 1000 repositories
│   │   └── ...                        # Additional analysis files
│   │
│   ├── rq1/                           # RQ1: Conflict frequency analysis
│   │   ├── csv/                       # Processed conflict data in CSV format
│   │   ├── dot_files/                 # Dependency tree DOT files
│   │   ├── results/                   # Analysis results and summaries
│   │   └── more-results/              # Extended analysis results
│   │
│   └── rq3/                           # RQ2: Breaking changes analysis (legacy folder name)
│       ├── csv/                       # Breaking change data in CSV format
│       ├── figures/                   # Generated visualizations
│       ├── normalisation/             # Data normalization scripts/results
│       └── analysis_results/          # Final analysis results and reports
│
├── research-parent/                   # Maven parent project
│   ├── pom.xml                        # Parent POM configuration
│   │
│   ├── depanalyzer/                   # Transitive dependency usage detection tool
│   │   └── (Java source code)         # Tool implementation for detecting used transitive deps
│   │
│   └── rq3/                           # RQ2 breaking change analysis tooling
│       ├── README.md                  # Implementation notes
│       ├── RQ3_IMPLEMENTATION_README.md
│       ├── IMPLEMENTATION_COMPLETE.md
│       └── (Java source code)         # Breaking change detection using Japicmp + JavaParser
│
├── downloading-script/                # Repository download automation
│   ├── build.gradle                   # Gradle build configuration
│   ├── src/                           # Java source for downloader
│   ├── config/                        # Configuration files
│   └── output/                        # Downloaded repositories
│
├── DependencyTreeAnalysis/            # Python analysis scripts
│   ├── generate_dot_file.py           # Generate dependency tree visualizations
│   ├── dot_to_csv.py                  # Convert DOT files to CSV
│   ├── csv_to_graphs.py               # Generate graphs from CSV data
│   ├── conflict_stats_summary.py      # Conflict statistics generation
│   └── get_valid_projects.py          # Project validation utilities
│
├── download-analysis/                 # Repository filtering analysis
│   ├── plot_repo_sizes.py             # Repository size analysis
│   ├── plot_valid_project.py          # Valid project distribution
│   ├── print_commit_filter.py         # Commit filtering analysis
│   └── ...                            # Additional analysis scripts
│
├── Python Analysis Scripts/           # RQ-specific analysis
│   ├── comprehensive_analysis.py      # Main analysis orchestrator
│   ├── analyze_used_breaking_changes.py # Breaking change usage analysis
│   ├── rq1_summary_stats_fixed.py     # RQ1 statistical analysis
│   ├── rq1_depth_bar_chart.py         # RQ1 depth visualization
│   └── rq3_used_breaking_changes_analysis.csv # RQ3 analysis data
│
├── DeepDependencyAnalyzer/            # Deep dependency analysis tool
│   └── target/                        # Compiled artifacts
│
└── legacy/                            # Earlier iterations and experiments
    ├── DetectTransitiveDependency.py  # Initial detection scripts
    ├── TreeAnalysis.py                # Early tree analysis
    └── ...                            # Historical artifacts

```

---

## Methodology

### Phase 1: Data Collection

1. **Repository Selection**

   - Source: GitHub public repositories
   - Language: Java (Maven-based projects)
   - Criteria: Active development, sufficient commit history, valid POM files
   - Total collected: 1,000+ repositories
   - **Final dataset: 1,030 repositories with 5,827 valid submodules**

2. **Filtering Process**
   - Repository size filtering (see `data/download_analysis/`)
   - Build validation (POM file integrity)
   - Dependency tree generation validation
   - Commit interval analysis
   - Multi-module project handling

### Phase 2: Dependency Tree Analysis (RQ1)

1. **Tree Generation**

   - Tool: Maven Dependency Plugin (`mvn dependency:tree`)
   - Output format: DOT graph files (GraphViz format)
   - Conversion: DOT → CSV for statistical analysis

2. **Conflict Detection**

   - Script: `DependencyTreeAnalysis/generate_dot_file.py`
   - Identifies version conflicts at each tree depth
   - Distinguishes between direct and transitive conflicts
   - Tracks conflict patterns across submodules

3. **Statistical Analysis**
   - Script: `rq1_summary_stats_fixed.py`
   - Metrics: conflict frequency, depth distribution, library patterns
   - Visualization: `rq1_depth_bar_chart.py`
   - Results: Conflict rates per submodule and repository

### Phase 3: Breaking Change Analysis (RQ2)

Our RQ2 methodology consisted of five stages:

#### Stage 1: Resolving POM Files

- Resolve effective POM files for each submodule
- Extract dependency information including versions
- Handle multi-module Maven projects
- Success rate: 560/1050 repositories successfully generated effective POMs

#### Stage 2: Dependency Resolution and Updates

- Identify all direct and transitive dependencies
- Simulate dependency updates to newer versions
- Track which dependencies change during updates
- Categorize as direct or transitive dependency changes

#### Stage 3: Detecting Breaking Changes

- Tool: **Japicmp** for bytecode-level API comparison
- Compare old vs. new versions of each dependency
- Detect syntactic breaking changes:
  - Method signature changes
  - Class modifications
  - Field changes
  - Constructor changes
- Output: Comprehensive list of all breaking changes

#### Stage 4: Verifying Client Usage

- Tool: **JavaParser** for static code analysis
- Parse client source code to build symbol tables
- Cross-reference breaking changes with actual code usage
- Filter to identify **used breaking changes** only
- This crucial step reduces false positives significantly

#### Stage 5: Normalization

- Calculate exposure rates (how often dependencies are updated)
- Normalize usage rates by dependency type
- Account for the fact that projects have more transitive than direct dependencies
- **Key metric: Normalized usage rate = (Used breaking changes / Total breaking changes) / (Dependency exposure)**

### Phase 4: Tool Development

Developed **`depanalyzer`** - a Maven plugin for transitive dependency usage detection:

- Analyzes complete dependency trees
- Performs static analysis on client code
- Identifies which transitive dependencies are actively used
- Generates detailed reports with file locations and code snippets
- Integrated with Maven build lifecycle for easy adoption

---

## Tools and Technologies

### Development Tools

| Tool       | Version | Purpose                              |
| ---------- | ------- | ------------------------------------ |
| **Java**   | 11+     | Primary implementation language      |
| **Maven**  | 3.6+    | Build tool and dependency management |
| **Gradle** | 7.x     | Repository downloader build system   |
| **Python** | 3.8+    | Data analysis and visualization      |

### Analysis Libraries

| Library                     | Purpose                                                                 |
| --------------------------- | ----------------------------------------------------------------------- |
| **Japicmp**                 | Java API comparison and breaking change detection via bytecode analysis |
| **JavaParser**              | Static code analysis to verify client usage of breaking changes         |
| **Maven Dependency Plugin** | Dependency tree generation and resolution                               |
| **Graphviz**                | DOT file processing and visualization                                   |
| **pandas**                  | Data analysis and manipulation                                          |
| **matplotlib/seaborn**      | Data visualization and statistical plotting                             |

### Development Environment

- **IDE:** IntelliJ IDEA / VS Code
- **Version Control:** Git / GitHub
- **OS:** macOS / Linux compatible

---

## Data Collection

### Repository Download Process

**Script:** `downloading-script/`

1. **GitHub API Query**

   - Search criteria: Java, Maven, star count > 100, recent activity
   - Download metadata and repository contents

2. **Filtering Pipeline**
   - Size validation (reject oversized repositories)
   - Build validation (POM must be valid)
   - Commit history verification

**Logs and Results:**

- `data/download_analysis/logs-1000-repos.csv` - Download logs
- `data/download_analysis/repo-sizes-*.txt` - Size analysis
- `data/download_analysis/filter-log-*.txt` - Filtering decisions

### Dependency Tree Extraction

**Process:**

1. Run `mvn dependency:tree` on each valid project
2. Generate DOT graph representation
3. Parse and convert to CSV for analysis

**Output Locations:**

- DOT files: `data/rq1/dot_files/`
- CSV files: `data/rq1/csv/`
- Results: `data/rq1/results/`

### Breaking Change Data (RQ2)

**Collection Method:**

1. Resolve effective POM files to extract all dependencies
2. Simulate dependency updates to identify version changes
3. For each update, compare API compatibility using **Japicmp**
4. Categorize changes (METHOD_CHANGE, CLASS_CHANGE, FIELD_CHANGE, CONSTRUCTOR_CHANGE)
5. Use **JavaParser** to verify which breaking changes are actually used in client code
6. Normalize results to account for dependency exposure rates

**Output Locations:**

- Raw data: `data/rq3/csv/`
- Normalization data: `data/rq3/normalisation/`
- Analysis results: `data/rq3/analysis_results/`
- Visualizations: `data/rq3/figures/`

---

## Analysis and Results

### RQ1: Dependency Conflict Analysis

**Key Statistics:**

- **Repositories analyzed:** 1,030
- **Valid submodules:** 5,827
- **Submodules with conflicts:** 45.8%
- **Conflicts in transitive dependencies:** 99.6%
- **Conflicts in direct dependencies:** 0.4%

**Key Insights:**

- Nearly half of all submodules experience version conflicts
- Conflicts overwhelmingly occur in transitive dependencies
- Most conflict-prone libraries: Jackson (jackson-databind, jackson-core), SLF4J, Guava
- Conflicts tend to occur deeper in dependency trees

**Visualizations:**

- Conflict depth distribution: Generated by `rq1_depth_bar_chart.py`
- Conflict patterns: `data/download_analysis/conflict_patterns.csv`
- Frequency analysis: `data/download_analysis/conflict_frequency_distribution.png`

**Detailed Results:** Located in `data/rq1/results/` and `rq1_analysis_results/`

---

### RQ2: Breaking Changes Analysis

**Overall Statistics:**

- **Total breaking changes detected:** 2,053,431
- **Breaking changes actually used in client code:** 4,201 (0.20%)
- **Unique libraries with breaking changes:** 1,329
- **Projects with used breaking changes:** 316 (17.5%)

**Distribution:**

- Direct dependency breaking changes: 26.5%
- Transitive dependency breaking changes: 73.5%

**Key Finding: Normalized Usage Rates**
After normalizing for dependency exposure:

- **Transitive dependencies:** Breaking changes are ~1.5x more likely to impact client code
- **Direct dependencies:** Lower normalized impact despite explicit declaration
- This suggests transitive dependencies pose hidden risks that are harder to manage

**Change Types:**

- METHOD_CHANGE: 68.8%
- CLASS_CHANGE: 12.4%
- FIELD_CHANGE: 10.2%
- CONSTRUCTOR_CHANGE: 8.7%

**Top Problematic Libraries:**

1. `com.fasterxml.jackson.core:jackson-databind` - 187,728 changes
2. `io.grpc:grpc-xds` - 163,175 changes
3. `io.prometheus:prometheus-metrics-exposition-formats` - 130,690 changes
4. `io.etcd:jetcd-core` - 98,445 changes
5. `org.bouncycastle:bcprov-jdk18on` - 72,912 changes

**Detailed Analysis:** See `data/rq3/analysis_results/` for comprehensive breakdowns

---

### Tool: Transitive Dependency Usage Detector

**Purpose:**
Developed as part of the research to detect which transitive dependencies are actually used in client code.

**Capabilities:**

- Scans Maven project dependency trees
- Identifies all transitive dependencies
- Performs static analysis to find usage in source code
- Reports exact file locations and code snippets
- Generates actionable reports for developers

**Potential Applications:**

- Proactive dependency management
- Security vulnerability assessment (which transitive deps are actually at risk?)
- Dependency cleanup and optimization
- Breaking change impact prediction

**Implementation:** Located in `research-parent/depanalyzer/`

---

## Replication Guide

### Prerequisites

1. **System Requirements:**

   - Java 11 or higher
   - Maven 3.6+
   - Python 3.8+
   - Graphviz (for DOT file processing)
   - Git

2. **Python Dependencies:**
   ```bash
   pip install pandas matplotlib seaborn numpy
   ```

### Step-by-Step Replication

#### Step 1: Repository Download

```bash
cd downloading-script
./gradlew run
```

Configuration: Edit `config/` files to adjust search criteria and download parameters.

#### Step 2: Generate Dependency Trees (RQ1)

```bash
cd DependencyTreeAnalysis
python generate_dot_file.py
python dot_to_csv.py
```

This generates dependency trees and converts them to analyzable CSV format.

#### Step 3: Analyze Conflicts (RQ1)

```bash
python conflict_stats_summary.py
python csv_to_graphs.py
```

#### Step 4: Run RQ1 Statistical Analysis

```bash
cd ..
python rq1_summary_stats_fixed.py
python rq1_depth_bar_chart.py
```

#### Step 5: Build Analysis Tools

```bash
cd research-parent
mvn clean install
```

This builds:

- `depanalyzer` - Main dependency analysis tool
- `rq3` - Breaking change detection tool

#### Step 6: Run Breaking Change Analysis (RQ2)

```bash
cd rq3
mvn exec:java
```

This will:

- Resolve effective POM files
- Identify dependency updates
- Detect breaking changes using Japicmp
- Verify client usage with JavaParser
- Generate normalized statistics

Or run the compiled JAR with appropriate parameters.

#### Step 7: Analyze Breaking Changes

```bash
cd ../..
python analyze_used_breaking_changes.py
python comprehensive_analysis.py
```

This generates the final analysis report and visualizations.

### Verification

Expected outputs:

- `data/rq1/results/` - Conflict analysis CSV files and statistics
- `data/rq3/analysis_results/` - Breaking changes analysis results
- `data/rq3/normalisation/` - Normalized usage rate calculations
- `data/rq3/figures/` - Generated visualizations for RQ2
- `rq1_analysis_results/` - Additional RQ1 analysis outputs

---

## Key Findings

### Finding 1: High Prevalence of Version Conflicts (RQ1)

**45.8% of submodules contain version conflicts**, with **99.6%** occurring in transitive dependencies:

- Version conflicts are pervasive in real-world Java projects
- Direct dependencies rarely conflict (only 0.4%)
- Transitive dependencies create hidden complexity
- Popular libraries (Jackson, SLF4J, Guava) are most conflict-prone
- Conflicts occur deeper in dependency trees, making them harder to detect

**Implication:** Developers need better tooling to visualize and manage transitive dependency conflicts.

---

### Finding 2: Transitive Dependencies Pose Greater Risk (RQ2)

Breaking changes from **transitive dependencies are ~1.5x more likely** to impact client code (after normalization):

- Despite being indirectly included, transitive deps have higher normalized impact
- Direct dependencies may be better managed due to explicit declaration
- Transitive dependencies introduce hidden coupling
- Developers often unaware they're using transitive dependency APIs

**Implication:** Projects should monitor transitive dependencies as closely as direct ones.

---

### Finding 3: Low Overall Breaking Change Usage Rate

Despite detecting **2,053,431 breaking changes**, only **0.20%** actually affect client code:

- Many dependencies are not fully utilized
- Breaking changes often occur in unused APIs
- Library maintainers could use usage data to minimize disruptive changes
- Static analysis (like our tool) is essential to filter noise

**Implication:** Not all breaking changes are equal; focus on those that actually impact code.

---

### Finding 4: Method Changes Dominate

**68.8%** of all breaking changes are method-related:

- API evolution primarily affects method signatures
- Method deprecation and removal are common patterns
- Suggests need for method-level compatibility checking
- Other change types (class, field, constructor) are less frequent

**Implication:** Tools should prioritize method-level change detection.

---

### Finding 5: Library Concentration

A small number of popular libraries account for disproportionate breaking changes:

- **Jackson (databind, core):** 187,728 + 69,447 = 257,175 changes (12.5%)
- **gRPC, Prometheus, Bouncy Castle** also highly problematic
- Popular libraries evolve rapidly
- Ecosystem-wide impact from major library updates

**Implication:** High-traffic libraries need stricter versioning discipline and better communication of breaking changes.

---

## Future Work

### Immediate Extensions

1. **Behavioral Breaking Change Detection**

   - Current work focuses on syntactic changes (API structure)
   - Future: Detect behavioral changes (same API, different behavior)
   - Requires test-based or execution-based analysis
   - Would provide more complete breaking change coverage

2. **IDE Integration**

   - Integrate `depanalyzer` tool into IDEs (IntelliJ, Eclipse, VS Code)
   - Real-time warnings about transitive dependency usage
   - Proactive notifications of breaking changes in dependencies
   - Inline suggestions for dependency updates

3. **Expanded Ecosystem Analysis**
   - Apply methodology to other ecosystems (npm, PyPI, NuGet)
   - Compare dependency management practices across languages
   - Identify ecosystem-specific patterns and challenges

### Long-term Research Directions

1. **Automated Remediation**

   - Automatic dependency constraint generation
   - Breaking change impact prediction using ML
   - Automated migration path suggestions
   - Code refactoring recommendations for breaking changes

2. **Temporal Analysis**

   - Track how breaking change patterns evolve over time
   - Study correlation with library popularity and maturity
   - Analyze developer response times to breaking changes
   - Identify trends in dependency management practices

3. **Security-Oriented Analysis**

   - Combine breaking change detection with CVE databases
   - Identify which vulnerable transitive dependencies are actually used
   - Prioritize security updates based on actual code exposure
   - Develop risk scoring for dependency updates

4. **Developer Tools and Best Practices**
   - Dependency health dashboards
   - Update risk assessment frameworks
   - Breaking change migration guides
   - Semantic versioning compliance checkers

---

## Individual Contributions

### Tony Yin (tyin363)

- **Repository Mining:** Developed GitHub repository download pipeline using Gradle
- **RQ1 Analysis:** Statistical analysis of dependency conflicts, depth distribution analysis
- **Data Processing:** Python scripts for CSV generation and data transformation
- **Visualization:** Created all charts and graphs for RQ1 conflict analysis
- **Documentation:** Main README author, compendium organization
- **Tool Development:** Contributed to data pipeline and analysis workflow
- **Report Writing:** Co-author of final report, responsible for RQ1 sections

### Alvari Kupari (akup390)

- **Tool Architecture:** Designed and implemented `depanalyzer` Maven plugin
- **RQ2 Implementation:** Breaking change detection using Japicmp and JavaParser
- **Static Analysis:** Client code usage verification and symbol table construction
- **Normalization:** Developed methodology for normalizing usage rates
- **Maven Integration:** POM resolution, dependency tree analysis, multi-module support
- **Tool Development:** Primary developer of Java-based analysis tools
- **Report Writing:** Co-author of final report, responsible for RQ2 sections

### Joint Contributions

- Research question formulation and refinement
- Methodology design and validation
- Dataset curation and filtering criteria
- Result interpretation and statistical analysis
- Code review, testing, and debugging
- Report structure and narrative development
- Presentation preparation and delivery

---

## Appendix

### Data Files Description

| File/Directory                           | Description                                                        |
| ---------------------------------------- | ------------------------------------------------------------------ |
| `repo_check.csv`                         | Repository validation results and filtering decisions              |
| `rq3_used_breaking_changes_analysis.csv` | Detailed analysis of breaking changes actually used in client code |
| `boxplot_main_statistics.csv`            | Statistical summaries for box plot visualizations                  |
| `data/rq1/csv/*.csv`                     | Per-submodule dependency conflict data                             |
| `data/rq1/results/*.csv`                 | Aggregated RQ1 conflict statistics                                 |
| `data/rq3/csv/*.csv`                     | Per-project breaking change detection results                      |
| `data/rq3/normalisation/*.csv`           | Normalized usage rate calculations                                 |
| `data/rq3/analysis_results/*.csv`        | Final RQ2 statistical summaries                                    |

### Script Descriptions

| Script                             | Purpose                                       | Input                        | Output                            |
| ---------------------------------- | --------------------------------------------- | ---------------------------- | --------------------------------- |
| `generate_dot_file.py`             | Generate dependency trees from Maven projects | Maven POM files              | DOT graph files                   |
| `dot_to_csv.py`                    | Convert dependency trees to CSV format        | DOT files                    | CSV files with tree structure     |
| `comprehensive_analysis.py`        | Generate comprehensive RQ2 analysis           | Breaking change CSV data     | Statistical summaries and reports |
| `rq1_summary_stats_fixed.py`       | Compute RQ1 conflict statistics               | RQ1 conflict CSV files       | Summary statistics CSV            |
| `rq1_depth_bar_chart.py`           | Visualize conflict depth distribution         | RQ1 results                  | Depth distribution bar charts     |
| `analyze_used_breaking_changes.py` | Analyze which breaking changes are used       | Breaking change + usage data | Usage analysis CSV                |
| `rq3_change_types_plots.py`        | Visualize breaking change type distribution   | RQ2 CSV data                 | Change type charts                |
| `rq3_top_problematic_libraries.py` | Identify and visualize problematic libraries  | RQ2 CSV data                 | Library ranking visualizations    |
| `conflict_stats_summary.py`        | Summarize conflict statistics                 | Conflict CSV data            | Summary statistics                |
| `csv_to_graphs.py`                 | Generate various conflict analysis graphs     | Conflict CSV data            | Multiple visualization outputs    |

### Configuration Files

- `research-parent/pom.xml` - Maven parent configuration
- `downloading-script/build.gradle` - Repository downloader configuration
- `downloading-script/config/` - Download and filtering parameters

---

## Contact Information

For questions or clarifications about this research:

- **Tony Yin:** tyin363@aucklanduni.ac.nz
- **Alvari Kupari:** akup390@aucklanduni.ac.nz

---

## License

This research project is conducted for academic purposes as part of SOFTENG 700 at the University of Auckland.

---

## Acknowledgments

- University of Auckland, Department of Software Engineering
- GitHub for providing access to public repositories
- Open-source community for the tools and libraries used in this research

---

**Last Updated:** October 19, 2025  
**Version:** 2.0  
**Status:** Final Submission for Research Compendium
