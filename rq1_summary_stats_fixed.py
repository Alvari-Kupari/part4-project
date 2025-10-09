#!/usr/bin/env python3
"""
RQ1 Summary Statistics: Dependency Conflict Analysis Summary (FIXED)

This script analyzes the dependency conflict stats summary CSV file to generate
comprehensive summary statistics. FIXED to only count omitted conflicts as actual conflicts.

Author: Research Team
Date: October 2025
"""

import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np
from pathlib import Path
import warnings
warnings.filterwarnings('ignore')

class RQ1SummaryAnalyzer:
    def __init__(self, csv_file_path):
        """
        Initialize the analyzer with the path to the summary CSV file.
        
        Args:
            csv_file_path (str): Path to the dependency conflict stats summary CSV
        """
        self.csv_file_path = Path(csv_file_path)
        self.df = None
        
    def load_data(self):
        """
        Load the summary CSV file.
        """
        print("Loading summary statistics data...")
        
        if not self.csv_file_path.exists():
            raise FileNotFoundError(f"File not found: {self.csv_file_path}")
        
        self.df = pd.read_csv(self.csv_file_path)
        print(f"Loaded {len(self.df)} records from {self.csv_file_path}")
        
        # Display basic info about the dataset
        print(f"Columns: {list(self.df.columns)}")
        print(f"Data shape: {self.df.shape}")
        
        return self.df
    
    def calculate_basic_stats(self):
        """
        Calculate basic summary statistics.
        FIXED: Only count omitted conflicts as actual conflicts.
        
        Returns:
            dict: Dictionary with basic statistics
        """
        if self.df is None:
            raise ValueError("Data not loaded. Call load_data() first.")
        
        stats = {
            'total_records': len(self.df),
            'unique_projects': self.df['project'].nunique(),
            'total_submodules': len(self.df),  # Each row is a submodule
            'unique_submodule_names': self.df['submodule'].nunique(),
            # FIXED: Only count omitted conflicts as actual conflicts
            'submodules_with_conflicts': len(self.df[self.df['omitted_conflict_count'] > 0]),
            'submodules_without_conflicts': len(self.df[self.df['omitted_conflict_count'] == 0])
        }
        
        # Calculate project-level conflict statistics
        # FIXED: Only consider omitted conflicts
        project_has_conflicts = self.df.groupby('project').apply(
            lambda x: (x['omitted_conflict_count'] > 0).any()
        )
        
        stats['unique_projects_with_conflicts'] = project_has_conflicts.sum()
        stats['unique_projects_without_conflicts'] = (~project_has_conflicts).sum()
        
        # Dependency statistics
        stats['total_dependencies'] = self.df['total_unique_dependencies'].sum()
        stats['avg_dependencies_per_submodule'] = self.df['total_unique_dependencies'].mean()
        stats['median_dependencies_per_submodule'] = self.df['total_unique_dependencies'].median()
        stats['max_dependencies_in_submodule'] = self.df['total_unique_dependencies'].max()
        stats['min_dependencies_in_submodule'] = self.df['total_unique_dependencies'].min()
        
        # Conflict statistics
        # FIXED: Separate omitted conflicts (actual conflicts) from version managed (resolved conflicts)
        stats['total_omitted_conflicts'] = self.df['omitted_conflict_count'].sum()
        stats['total_version_managed_conflicts'] = self.df['version_managed_count'].sum()
        stats['total_all_issues'] = stats['total_omitted_conflicts'] + stats['total_version_managed_conflicts']
        
        # Average conflict rates
        stats['avg_omitted_conflict_percentage'] = self.df['omitted_conflict_percentage'].mean()
        stats['avg_version_managed_percentage'] = self.df['version_managed_percentage'].mean()
        
        return stats
    
    def analyze_project_patterns(self):
        """
        Analyze patterns at the project level.
        FIXED: Only count omitted conflicts as actual conflicts.
        
        Returns:
            dict: Project-level analysis results
        """
        # Group by project to get project-level statistics
        project_stats = self.df.groupby('project').agg({
            'submodule': 'count',  # Number of submodules per project
            'total_unique_dependencies': ['sum', 'mean'],
            'omitted_conflict_count': 'sum',
            'version_managed_count': 'sum',
            'omitted_conflict_percentage': 'mean',
            'version_managed_percentage': 'mean'
        }).round(2)
        
        # Flatten column names
        project_stats.columns = [
            'num_submodules', 'total_dependencies', 'avg_dependencies_per_submodule',
            'total_omitted_conflicts', 'total_version_managed_conflicts',
            'avg_omitted_conflict_percentage', 'avg_version_managed_percentage'
        ]
        
        # FIXED: Only use omitted conflicts for "conflict" metrics
        project_stats['total_conflicts'] = project_stats['total_omitted_conflicts']
        project_stats['avg_conflict_percentage'] = project_stats['avg_omitted_conflict_percentage']
        
        analysis = {
            'project_stats': project_stats,
            'most_submodules': project_stats['num_submodules'].max(),
            'project_with_most_submodules': project_stats['num_submodules'].idxmax(),
            'most_conflicts_project': project_stats['total_conflicts'].idxmax(),
            'highest_conflict_rate_project': project_stats['avg_conflict_percentage'].idxmax(),
            'projects_with_multi_submodules': len(project_stats[project_stats['num_submodules'] > 1]),
            'single_submodule_projects': len(project_stats[project_stats['num_submodules'] == 1])
        }
        
        return analysis
    
    def analyze_conflict_distribution(self):
        """
        Analyze the distribution of conflicts across submodules.
        FIXED: Only count omitted conflicts as actual conflicts.
        
        Returns:
            dict: Conflict distribution analysis
        """
        distribution = {
            # FIXED: Only count omitted conflicts as actual conflicts
            'submodules_with_no_conflicts': len(self.df[self.df['omitted_conflict_count'] == 0]),
            'submodules_with_conflicts': len(self.df[self.df['omitted_conflict_count'] > 0]),
            'submodules_with_only_omitted': len(self.df[(self.df['omitted_conflict_count'] > 0) & 
                                                       (self.df['version_managed_count'] == 0)]),
            'submodules_with_only_version_managed': len(self.df[(self.df['omitted_conflict_count'] == 0) & 
                                                               (self.df['version_managed_count'] > 0)]),
            'submodules_with_both_types': len(self.df[(self.df['omitted_conflict_count'] > 0) & 
                                                     (self.df['version_managed_count'] > 0)])
        }
        
        # FIXED: Conflict severity based only on omitted conflict percentage
        distribution['high_conflict_submodules'] = len(self.df[self.df['omitted_conflict_percentage'] > 20])
        distribution['medium_conflict_submodules'] = len(self.df[(self.df['omitted_conflict_percentage'] > 10) & 
                                                                (self.df['omitted_conflict_percentage'] <= 20)])
        distribution['low_conflict_submodules'] = len(self.df[(self.df['omitted_conflict_percentage'] > 0) & 
                                                             (self.df['omitted_conflict_percentage'] <= 10)])
        
        return distribution
    
    def create_visualizations(self, output_dir="analysis_results"):
        """
        Create summary visualizations.
        FIXED: Only show omitted conflicts as actual conflicts.
        
        Args:
            output_dir (str): Directory to save plots
        """
        Path(output_dir).mkdir(exist_ok=True)
        
        # Set up plotting style
        plt.style.use('default')
        sns.set_palette("husl")
        
        # Create a figure with multiple subplots
        fig, ((ax1, ax2), (ax3, ax4)) = plt.subplots(2, 2, figsize=(16, 12))
        fig.suptitle('RQ1: Dependency Conflict Analysis Summary (Fixed)', fontsize=16, fontweight='bold')
        
        # Plot 1: Distribution of dependencies per submodule
        ax1.hist(self.df['total_unique_dependencies'], bins=30, alpha=0.7, color='skyblue', edgecolor='black')
        ax1.set_xlabel('Number of Dependencies')
        ax1.set_ylabel('Number of Submodules')
        ax1.set_title('Distribution of Dependencies per Submodule')
        ax1.grid(True, alpha=0.3)
        
        # Plot 2: FIXED - Only show omitted conflict percentages
        ax2.hist(self.df['omitted_conflict_percentage'], bins=30, alpha=0.7, color='lightcoral', edgecolor='black')
        ax2.set_xlabel('Conflict Percentage (%)')
        ax2.set_ylabel('Number of Submodules')
        ax2.set_title('Distribution of Conflict Percentages (Omitted Conflicts Only)')
        ax2.grid(True, alpha=0.3)
        
        # Plot 3: FIXED - Show omitted vs version managed as different categories
        conflict_types = ['Omitted Conflicts\n(Actual Conflicts)', 'Version Managed\n(Resolved Issues)']
        conflict_totals = [self.df['omitted_conflict_count'].sum(), self.df['version_managed_count'].sum()]
        bars = ax3.bar(conflict_types, conflict_totals, color=['red', 'lightgreen'], alpha=0.7, edgecolor='black')
        ax3.set_ylabel('Total Count')
        ax3.set_title('Omitted Conflicts vs Version Managed Issues')
        ax3.grid(True, alpha=0.3, axis='y')
        
        # Add value labels on bars
        for bar, value in zip(bars, conflict_totals):
            ax3.text(bar.get_x() + bar.get_width()/2., bar.get_height() + max(conflict_totals)*0.01,
                    f'{value:,}', ha='center', va='bottom', fontweight='bold')
        
        # Plot 4: FIXED - Pie chart showing modules with/without conflicts (like the original)
        conflict_status = ['No Conflicts', 'Has Conflicts']
        conflict_counts = [
            len(self.df[self.df['omitted_conflict_percentage'] == 0]),
            len(self.df[self.df['omitted_conflict_percentage'] > 0])
        ]
        colors = ['#8fd9b6', '#ffb347']  # Same colors as original pie chart
        
        wedges, texts, autotexts = ax4.pie(conflict_counts, labels=conflict_status, autopct='%1.1f%%', 
                                          colors=colors, startangle=90)
        ax4.set_title('Modules: No Version Conflict vs. Has Version Conflict')
        
        plt.tight_layout()
        
        # Save the plot
        output_file = Path(output_dir) / 'rq1_summary_statistics_fixed.png'
        plt.savefig(output_file, dpi=300, bbox_inches='tight')
        plt.show()
        
        print(f"Fixed summary visualizations saved to: {output_file}")
    
    def generate_report(self, output_dir="analysis_results"):
        """
        Generate a comprehensive text report.
        FIXED: Only count omitted conflicts as actual conflicts.
        
        Args:
            output_dir (str): Directory to save the report
        """
        Path(output_dir).mkdir(exist_ok=True)
        
        # Calculate all statistics
        basic_stats = self.calculate_basic_stats()
        project_analysis = self.analyze_project_patterns()
        conflict_distribution = self.analyze_conflict_distribution()
        
        # Create report
        report_file = Path(output_dir) / 'rq1_comprehensive_summary_report_fixed.txt'
        
        with open(report_file, 'w') as f:
            f.write("RQ1: Dependency Conflict Analysis - Comprehensive Summary Report (FIXED)\n")
            f.write("=" * 75 + "\n")
            f.write("NOTE: Only omitted conflicts are counted as actual conflicts.\n")
            f.write("Version managed conflicts are resolved issues, not active conflicts.\n")
            f.write("=" * 75 + "\n\n")
            
            # Basic Statistics
            f.write("BASIC STATISTICS\n")
            f.write("-" * 20 + "\n")
            f.write(f"Total Records (Submodules): {basic_stats['total_records']:,}\n")
            f.write(f"Unique Projects: {basic_stats['unique_projects']:,}\n")
            f.write(f"Unique Submodule Names: {basic_stats['unique_submodule_names']:,}\n")
            f.write(f"Submodules with Conflicts: {basic_stats['submodules_with_conflicts']:,}\n")
            f.write(f"Submodules without Conflicts: {basic_stats['submodules_without_conflicts']:,}\n\n")
            
            # Project-Level Statistics
            f.write("PROJECT-LEVEL STATISTICS\n")
            f.write("-" * 25 + "\n")
            f.write(f"Unique Projects with Conflicts: {basic_stats['unique_projects_with_conflicts']:,}\n")
            f.write(f"Unique Projects without Conflicts: {basic_stats['unique_projects_without_conflicts']:,}\n")
            f.write(f"Percentage of Projects with Conflicts: {(basic_stats['unique_projects_with_conflicts'] / basic_stats['unique_projects'] * 100):.1f}%\n")
            f.write(f"Percentage of Projects without Conflicts: {(basic_stats['unique_projects_without_conflicts'] / basic_stats['unique_projects'] * 100):.1f}%\n\n")
            
            # Dependency Statistics
            f.write("DEPENDENCY STATISTICS\n")
            f.write("-" * 20 + "\n")
            f.write(f"Total Dependencies Analyzed: {basic_stats['total_dependencies']:,}\n")
            f.write(f"Average Dependencies per Submodule: {basic_stats['avg_dependencies_per_submodule']:.1f}\n")
            f.write(f"Median Dependencies per Submodule: {basic_stats['median_dependencies_per_submodule']:.1f}\n")
            f.write(f"Max Dependencies in a Submodule: {basic_stats['max_dependencies_in_submodule']:,}\n")
            f.write(f"Min Dependencies in a Submodule: {basic_stats['min_dependencies_in_submodule']:,}\n\n")
            
            # Conflict Statistics (FIXED)
            f.write("CONFLICT STATISTICS (FIXED)\n")
            f.write("-" * 25 + "\n")
            f.write(f"Total Omitted Conflicts (Actual Conflicts): {basic_stats['total_omitted_conflicts']:,}\n")
            f.write(f"Total Version Managed (Resolved Issues): {basic_stats['total_version_managed_conflicts']:,}\n")
            f.write(f"Total All Issues (Omitted + Version Managed): {basic_stats['total_all_issues']:,}\n")
            f.write(f"Average Conflict Percentage (Omitted Only): {basic_stats['avg_omitted_conflict_percentage']:.2f}%\n")
            f.write(f"Average Version Managed Percentage: {basic_stats['avg_version_managed_percentage']:.2f}%\n\n")
            
            # Verification with pie chart
            conflicts_count = len(self.df[self.df['omitted_conflict_percentage'] > 0])
            no_conflicts_count = len(self.df[self.df['omitted_conflict_percentage'] == 0])
            total_submodules = len(self.df)
            
            f.write("PIE CHART VERIFICATION\n")
            f.write("-" * 20 + "\n")
            f.write(f"Submodules with conflicts (omitted_conflict_percentage > 0): {conflicts_count:,}\n")
            f.write(f"Submodules without conflicts (omitted_conflict_percentage = 0): {no_conflicts_count:,}\n")
            f.write(f"Total submodules: {total_submodules:,}\n")
            f.write(f"Percentage with conflicts: {(conflicts_count / total_submodules * 100):.1f}%\n")
            f.write(f"Percentage without conflicts: {(no_conflicts_count / total_submodules * 100):.1f}%\n\n")
            
            # Project-Level Analysis
            f.write("PROJECT-LEVEL ANALYSIS\n")
            f.write("-" * 25 + "\n")
            f.write(f"Projects with Multiple Submodules: {project_analysis['projects_with_multi_submodules']:,}\n")
            f.write(f"Single Submodule Projects: {project_analysis['single_submodule_projects']:,}\n")
            f.write(f"Max Submodules in a Project: {project_analysis['most_submodules']:,}\n")
            f.write(f"Project with Most Submodules: {project_analysis['project_with_most_submodules']}\n")
            f.write(f"Project with Most Conflicts: {project_analysis['most_conflicts_project']}\n")
            f.write(f"Project with Highest Conflict Rate: {project_analysis['highest_conflict_rate_project']}\n\n")
            
            # Conflict Distribution
            f.write("CONFLICT DISTRIBUTION\n")
            f.write("-" * 20 + "\n")
            f.write(f"Submodules with No Conflicts: {conflict_distribution['submodules_with_no_conflicts']:,}\n")
            f.write(f"Submodules with Conflicts: {conflict_distribution['submodules_with_conflicts']:,}\n")
            f.write(f"Submodules with Only Omitted Conflicts: {conflict_distribution['submodules_with_only_omitted']:,}\n")
            f.write(f"Submodules with Only Version Managed: {conflict_distribution['submodules_with_only_version_managed']:,}\n")
            f.write(f"Submodules with Both Types: {conflict_distribution['submodules_with_both_types']:,}\n\n")
            
            # Conflict Severity (FIXED)
            f.write("CONFLICT SEVERITY (by omitted conflict percentage only)\n")
            f.write("-" * 50 + "\n")
            f.write(f"High Conflict Submodules (>20%): {conflict_distribution['high_conflict_submodules']:,}\n")
            f.write(f"Medium Conflict Submodules (10-20%): {conflict_distribution['medium_conflict_submodules']:,}\n")
            f.write(f"Low Conflict Submodules (0-10%): {conflict_distribution['low_conflict_submodules']:,}\n\n")
            
            # Top Projects by Various Metrics
            f.write("TOP PROJECTS BY METRICS\n")
            f.write("-" * 25 + "\n")
            project_stats = project_analysis['project_stats']
            
            f.write("Top 5 Projects by Total Conflicts (Omitted Only):\n")
            top_conflicts = project_stats.nlargest(5, 'total_conflicts')
            for project, row in top_conflicts.iterrows():
                f.write(f"  {project}: {row['total_conflicts']} conflicts\n")
            
            f.write("\nTop 5 Projects by Conflict Percentage (Omitted Only):\n")
            top_percentage = project_stats.nlargest(5, 'avg_conflict_percentage')
            for project, row in top_percentage.iterrows():
                f.write(f"  {project}: {row['avg_conflict_percentage']:.1f}% conflict rate\n")
            
            f.write("\nTop 5 Projects by Number of Submodules:\n")
            top_submodules = project_stats.nlargest(5, 'num_submodules')
            for project, row in top_submodules.iterrows():
                f.write(f"  {project}: {row['num_submodules']} submodules\n")
        
        print(f"Fixed comprehensive report saved to: {report_file}")
        
        # Save detailed project statistics as CSV
        csv_file = Path(output_dir) / 'rq1_project_level_statistics_fixed.csv'
        project_analysis['project_stats'].to_csv(csv_file)
        print(f"Fixed project-level statistics saved to: {csv_file}")
        
        return basic_stats, project_analysis, conflict_distribution
    
    def run_complete_analysis(self):
        """
        Run the complete summary analysis pipeline.
        """
        print("Starting RQ1 Summary Statistics Analysis (FIXED)...")
        print("=" * 55)
        
        # Load data
        self.load_data()
        
        # Generate report and statistics
        basic_stats, project_analysis, conflict_distribution = self.generate_report()
        
        # Create visualizations
        self.create_visualizations()
        
        print("\nFixed Summary Analysis Complete!")
        print("=" * 55)
        print(f"Key Findings (FIXED - Only Omitted Conflicts Counted):")
        print(f"- Analyzed {basic_stats['unique_projects']:,} projects with {basic_stats['total_records']:,} submodules")
        print(f"- Projects with conflicts: {basic_stats['unique_projects_with_conflicts']:,} ({(basic_stats['unique_projects_with_conflicts'] / basic_stats['unique_projects'] * 100):.1f}%)")
        print(f"- Projects without conflicts: {basic_stats['unique_projects_without_conflicts']:,} ({(basic_stats['unique_projects_without_conflicts'] / basic_stats['unique_projects'] * 100):.1f}%)")
        print(f"- Total dependencies: {basic_stats['total_dependencies']:,}")
        print(f"- Total actual conflicts (omitted): {basic_stats['total_omitted_conflicts']:,}")
        print(f"- Total resolved issues (version managed): {basic_stats['total_version_managed_conflicts']:,}")
        print(f"- Average conflict rate: {basic_stats['avg_omitted_conflict_percentage']:.2f}%")
        
        return {
            'basic_stats': basic_stats,
            'project_analysis': project_analysis,
            'conflict_distribution': conflict_distribution
        }


def main():
    """
    Main execution function.
    """
    # Set up path to the summary CSV file
    csv_file = "/Users/tonyyin/Desktop/Courses/SOFTENG700/part4-project/data/rq1/results/dependency_conflict_stats_summary.csv"
    
    # Initialize analyzer
    analyzer = RQ1SummaryAnalyzer(csv_file)
    
    # Run complete analysis
    results = analyzer.run_complete_analysis()
    
    print("\nFixed files generated in 'analysis_results' directory:")
    print("- rq1_summary_statistics_fixed.png (corrected visualizations)")
    print("- rq1_comprehensive_summary_report_fixed.txt (corrected detailed report)")
    print("- rq1_project_level_statistics_fixed.csv (corrected project-level data)")


if __name__ == "__main__":
    main()