# Research Compendium: Understanding and Resolving Transitive Dependency Conflicts

**Project Number:** 106  
**Team Members:** Tony Yin (tyin363), Alvari Kupari (akup390)  
**Supervisors:** Kelly Blincoe, Valerio Terragni,  Dhanushka Jayasuriya
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

This research project investigates the nature and impact of dependency conflicts in Java projects, with a particular focus on transitive dependencies and breaking changes. The study analyzes **1,808 Java projects** from GitHub to understand how frequently dependency conflicts arise, their characteristics, and their actual impact on client code.

### Background

Modern software development heavily relies on third-party dependencies, which often introduce their own transitive dependencies. This creates complex dependency trees where version conflicts can emerge, potentially leading to breaking changes that affect application functionality. Understanding these patterns is crucial for improving dependency management practices and tooling.

### Objectives

- Quantify the frequency of dependency conflicts in real-world Java projects
- Analyze breaking changes in both direct and transitive dependencies
- Develop tooling to help developers identify potential dependency issues
- Provide empirical evidence for dependency management best practices

---

## Research Questions

### RQ1: Dependency Conflict Frequency

**How frequently do conflicting versions of the same dependency arise within dependency trees?**

This question examines the prevalence of version conflicts in Java dependency trees, analyzing both direct and transitive dependencies across our dataset of 1,808 projects.

**Key Metrics:**

- Conflict frequency per project
- Depth at which conflicts occur
- Most conflict-prone libraries

### RQ2: Breaking Changes Analysis

**Are breaking changes in transitive dependencies that affect client code more likely to occur in non-major releases compared to direct dependencies?**

This question investigates whether breaking changes follow semantic versioning principles and compares the behavior of direct versus transitive dependencies.

**Key Metrics:**

- Breaking change distribution across version types (major, minor, patch)
- Direct vs. transitive dependency comparison
- Actual usage rate of breaking changes in client code

### RQ3: Tooling Development

**How can a tool be designed to help developers identify potential issues with transitive dependencies?**

This question explores the design and implementation of automated tools to detect and report dependency conflicts and breaking changes.

**Deliverables:**

- Dependency analysis tool (depanalyzer)
- Breaking change detection system
- Visualization and reporting capabilities

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
│   └── rq3/                           # RQ3: Breaking changes analysis
│       ├── csv/                       # Breaking change data in CSV format
│       ├── figures/                   # Generated visualizations
│       ├── normalisation/             # Data normalization scripts/results
│       └── results/                   # Analysis results including ANALYSIS_REPORT.md
│
├── research-parent/                   # Maven parent project
│   ├── pom.xml                        # Parent POM configuration
│   │
│   ├── depanalyzer/                   # Main dependency analysis tool
│   │   └── (Java source code)         # Tool implementation
│   │
│   └── rq3/                           # RQ3 specific tooling
│       ├── README.md                  # RQ3 implementation notes
│       ├── RQ3_IMPLEMENTATION_README.md
│       ├── IMPLEMENTATION_COMPLETE.md
│       └── (Java source code)         # Breaking change detection implementation
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
   - Valid for analysis: 1,808 projects

2. **Filtering Process**
   - Repository size filtering (see `data/download_analysis/`)
   - Build validation
   - Dependency tree generation validation
   - Commit interval analysis

### Phase 2: Dependency Tree Analysis (RQ1)

1. **Tree Generation**

   - Tool: Maven Dependency Plugin
   - Output format: DOT graph files
   - Conversion: DOT → CSV for analysis

2. **Conflict Detection**

   - Script: `DependencyTreeAnalysis/generate_dot_file.py`
   - Identifies version conflicts at each tree depth
   - Tracks both direct and transitive conflicts

3. **Statistical Analysis**
   - Script: `rq1_summary_stats_fixed.py`
   - Metrics: conflict frequency, depth distribution, library patterns
   - Visualization: `rq1_depth_bar_chart.py`

### Phase 3: Breaking Change Analysis (RQ3)

1. **Breaking Change Detection**

   - Tool: `research-parent/rq3/` (Java-based analyzer)
   - Uses: [japicmp](https://github.com/siom79/japicmp) for bytecode comparison
   - Detects: method changes, class changes, field changes, constructor changes

2. **Usage Analysis**

   - Cross-references breaking changes with actual code usage
   - Script: `analyze_used_breaking_changes.py`
   - Identifies which breaking changes actually impact client code

3. **Comprehensive Reporting**
   - Script: `comprehensive_analysis.py`
   - Generates: summary statistics, visualizations, detailed reports
   - Output: `data/rq3/results/ANALYSIS_REPORT.md`

### Phase 4: Tool Development (RQ3)

Developed `depanalyzer` - a Maven-based tool for automated dependency analysis and conflict detection.

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

| Library                     | Purpose                                           |
| --------------------------- | ------------------------------------------------- |
| **japicmp**                 | Java API comparison and breaking change detection |
| **Maven Dependency Plugin** | Dependency tree generation                        |
| **Graphviz**                | DOT file processing                               |
| **pandas**                  | Data analysis and manipulation                    |
| **matplotlib/seaborn**      | Data visualization                                |

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

### Breaking Change Data

**Collection Method:**

1. Extract all dependency updates from project history
2. For each update, compare API compatibility using japicmp
3. Categorize changes (METHOD_CHANGE, CLASS_CHANGE, FIELD_CHANGE, etc.)
4. Cross-reference with actual code usage

**Output Locations:**

- Raw data: `data/rq3/csv/`
- Analysis: `data/rq3/results/`
- Visualizations: `data/rq3/figures/`

---

## Analysis and Results

### RQ1: Dependency Conflict Analysis

**Key Statistics:**

- Projects with conflicts: [See `data/rq1/results/`]
- Average conflict depth: [Computed by `rq1_summary_stats_fixed.py`]
- Most conflict-prone libraries: [See analysis results]

**Visualizations:**

- Conflict depth distribution: `rq1_depth_bar_chart.py`
- Conflict patterns: `data/download_analysis/conflict_patterns.csv`

**Detailed Results:** Located in `data/rq1/results/` and `rq1_analysis_results/`

### RQ3: Breaking Changes Analysis

**Summary (from ANALYSIS_REPORT.md):**

- **Total projects analyzed:** 1,808
- **Total breaking changes detected:** 2,053,431
- **Breaking changes actually used:** 4,201 (0.20%)
- **Unique libraries with breaking changes:** 1,329
- **Projects with used breaking changes:** 316 (17.5%)

**Distribution:**

- Direct dependency changes: 26.5%
- Transitive dependency changes: 73.5%

**Change Types:**

- METHOD_CHANGE: 68.8%
- CLASS_CHANGE: 12.4%
- FIELD_CHANGE: 10.2%
- CONSTRUCTOR_CHANGE: 8.7%

**Top Problematic Libraries:**

1. jackson-databind: 187,728 changes
2. grpc-xds: 163,175 changes
3. prometheus-metrics-exposition-formats: 130,690 changes

**Detailed Report:** `data/rq3/results/ANALYSIS_REPORT.md`

### Visualizations

All generated visualizations are in `analysis_results/`:

- `overview_dashboard.png` - Overall analysis summary
- `change_types_distribution.png` - Distribution of breaking change types
- `top_problematic_libraries.png` - Most problematic libraries
- `project_level_analysis.png` - Per-project analysis

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

#### Step 6: Run Breaking Change Analysis (RQ3)

```bash
cd rq3
mvn exec:java
```

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

- `data/rq1/results/` - Conflict analysis CSV files
- `data/rq3/results/ANALYSIS_REPORT.md` - Comprehensive breaking changes report
- `analysis_results/` - All visualizations and summary CSVs

---

## Key Findings

### Finding 1: Low Breaking Change Usage Rate

Despite detecting over 2 million breaking changes, only **0.20%** actually affect client code. This suggests:

- Many dependencies are not fully utilized
- Breaking changes often occur in unused APIs
- Aggressive API deprecation may have limited impact

### Finding 2: Transitive Dependencies Dominate

**73.5%** of breaking changes come from transitive dependencies, highlighting:

- The hidden risk of indirect dependencies
- Need for better transitive dependency monitoring
- Importance of dependency tree analysis

### Finding 3: Method Changes Are Most Common

**68.8%** of all breaking changes are method-related, indicating:

- API evolution primarily affects method signatures
- Method deprecation is a common pattern
- Need for careful method-level compatibility checking

### Finding 4: Library Concentration

A small number of popular libraries (e.g., Jackson, gRPC) account for a disproportionate share of breaking changes, suggesting:

- Popular libraries evolve rapidly
- High-traffic libraries need stricter versioning discipline
- Ecosystem-wide impact from major library updates

### Finding 5: Poor Semantic Versioning Compliance

- Only 8.3% maintain binary compatibility
- 0.0% maintain source compatibility
- Suggests widespread non-compliance with semantic versioning principles

---

## Future Work

### Short-term Improvements

1. **Enhanced Tool Development**

   - IDE integration for real-time conflict detection
   - Automated dependency update recommendations
   - Compatibility prediction models

2. **Extended Analysis**
   - Analyze more ecosystems (npm, PyPI, etc.)
   - Temporal analysis of breaking change trends
   - Developer response patterns to breaking changes

### Long-term Research Directions

1. **Automated Remediation**

   - Automatic dependency constraint generation
   - Breaking change impact prediction
   - Automated migration path suggestions

2. **Ecosystem Studies**

   - Cross-language comparison of dependency practices
   - Impact of package manager features on conflict rates
   - Social network analysis of dependency relationships

3. **Developer Tools**
   - Proactive breaking change notifications
   - Dependency health scoring
   - Update risk assessment

---

## Individual Contributions

### Tony Yin (tyin363)

- Repository download pipeline development
- RQ1 statistical analysis and visualization
- Python analysis scripts
- Data processing and CSV generation
- Documentation and reporting

### Alvari Kupari (akup390)

- Java-based analysis tool development (depanalyzer)
- Breaking change detection implementation (RQ3)
- Maven plugin integration
- japicmp integration and configuration
- Tool architecture and design

### Joint Contributions

- Research question formulation
- Methodology design
- Result interpretation
- Report writing
- Code review and testing

---

## Appendix

### Data Files Description

| File/Directory                           | Description                               |
| ---------------------------------------- | ----------------------------------------- |
| `repo_check.csv`                         | Repository validation results             |
| `rq3_used_breaking_changes_analysis.csv` | Used breaking changes detailed analysis   |
| `analysis_results/*.csv`                 | Summary statistics and aggregated results |
| `data/rq1/csv/*.csv`                     | Per-project conflict data                 |
| `data/rq3/csv/*.csv`                     | Per-project breaking change data          |

### Script Descriptions

| Script                             | Purpose                   | Input                | Output             |
| ---------------------------------- | ------------------------- | -------------------- | ------------------ |
| `generate_dot_file.py`             | Generate dependency trees | Maven projects       | DOT files          |
| `dot_to_csv.py`                    | Convert trees to CSV      | DOT files            | CSV files          |
| `comprehensive_analysis.py`        | Generate final report     | All CSV data         | ANALYSIS_REPORT.md |
| `rq1_summary_stats_fixed.py`       | RQ1 statistics            | RQ1 CSV files        | Statistics CSV     |
| `analyze_used_breaking_changes.py` | Usage analysis            | Breaking change data | Usage statistics   |

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

**Last Updated:** October 15, 2025  
**Version:** 1.0  
**Status:** Final Submission
