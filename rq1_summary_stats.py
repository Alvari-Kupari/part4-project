#!/usr/bin/env python3
"""
RQ1 Summary Statistics: Dependency Conflict Analysis Summary

This script analyzes the dependency conflict stats summary CSV file to generate
comprehensive summary statistics including number of projects, submodules, 
conflict patterns, and overall statistics.

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
            'submodules_with_conflicts': len(self.df[(self.df['omitted_conflict_count'] > 0) | 
                                                    (self.df['version_managed_count'] > 0)]),
            'submodules_without_conflicts': len(self.df[(self.df['omitted_conflict_count'] == 0) & 
                                                       (self.df['version_managed_count'] == 0)])
        }
        
        # Calculate project-level conflict statistics
        project_has_conflicts = self.df.groupby('project').apply(
            lambda x: ((x['omitted_conflict_count'] > 0) | (x['version_managed_count'] > 0)).any()
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
        stats['total_omitted_conflicts'] = self.df['omitted_conflict_count'].sum()
        stats['total_version_managed_conflicts'] = self.df['version_managed_count'].sum()
        stats['total_all_conflicts'] = stats['total_omitted_conflicts'] + stats['total_version_managed_conflicts']
        
        # Average conflict rates
        stats['avg_omitted_conflict_percentage'] = self.df['omitted_conflict_percentage'].mean()
        stats['avg_version_managed_percentage'] = self.df['version_managed_percentage'].mean()
        stats['avg_total_conflict_percentage'] = (self.df['omitted_conflict_percentage'] + 
                                                 self.df['version_managed_percentage']).mean()
        
        return stats
    
    def analyze_project_patterns(self):
        """
        Analyze patterns at the project level.
        
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
        
        # Calculate total conflicts per project
        project_stats['total_conflicts'] = (project_stats['total_omitted_conflicts'] + 
                                           project_stats['total_version_managed_conflicts'])
        
        # Calculate overall conflict percentage per project
        project_stats['avg_total_conflict_percentage'] = (project_stats['avg_omitted_conflict_percentage'] + 
                                                         project_stats['avg_version_managed_percentage'])
        
        analysis = {
            'project_stats': project_stats,
            'most_submodules': project_stats['num_submodules'].max(),
            'project_with_most_submodules': project_stats['num_submodules'].idxmax(),
            'most_conflicts_project': project_stats['total_conflicts'].idxmax(),
            'highest_conflict_rate_project': project_stats['avg_total_conflict_percentage'].idxmax(),
            'projects_with_multi_submodules': len(project_stats[project_stats['num_submodules'] > 1]),
            'single_submodule_projects': len(project_stats[project_stats['num_submodules'] == 1])
        }
        
        return analysis
    
    def analyze_conflict_distribution(self):
        """
        Analyze the distribution of conflicts across submodules.
        
        Returns:
            dict: Conflict distribution analysis
        """
        # Calculate total conflicts per submodule
        self.df['total_conflicts'] = self.df['omitted_conflict_count'] + self.df['version_managed_count']
        self.df['total_conflict_percentage'] = self.df['omitted_conflict_percentage'] + self.df['version_managed_percentage']
        
        distribution = {
            'submodules_with_no_conflicts': len(self.df[self.df['total_conflicts'] == 0]),
            'submodules_with_conflicts': len(self.df[self.df['total_conflicts'] > 0]),
            'submodules_with_only_omitted': len(self.df[(self.df['omitted_conflict_count'] > 0) & 
                                                       (self.df['version_managed_count'] == 0)]),
            'submodules_with_only_version_managed': len(self.df[(self.df['omitted_conflict_count'] == 0) & 
                                                               (self.df['version_managed_count'] > 0)]),
            'submodules_with_both_types': len(self.df[(self.df['omitted_conflict_count'] > 0) & 
                                                     (self.df['version_managed_count'] > 0)])
        }
        
        # Conflict severity categories based on percentage
        distribution['high_conflict_submodules'] = len(self.df[self.df['total_conflict_percentage'] > 20])
        distribution['medium_conflict_submodules'] = len(self.df[(self.df['total_conflict_percentage'] > 10) & 
                                                                (self.df['total_conflict_percentage'] <= 20)])
        distribution['low_conflict_submodules'] = len(self.df[(self.df['total_conflict_percentage'] > 0) & 
                                                             (self.df['total_conflict_percentage'] <= 10)])
        
        return distribution
    
    def create_visualizations(self, output_dir="analysis_results"):
        """
        Create summary visualizations.
        
        Args:
            output_dir (str): Directory to save plots
        """
        Path(output_dir).mkdir(exist_ok=True)
        
        # Set up plotting style
        plt.style.use('default')
        sns.set_palette("husl")
        
        # Create a figure with multiple subplots
        fig, ((ax1, ax2), (ax3, ax4)) = plt.subplots(2, 2, figsize=(16, 12))
        fig.suptitle('RQ1: Dependency Conflict Analysis Summary', fontsize=16, fontweight='bold')
        
        # Plot 1: Distribution of dependencies per submodule
        ax1.hist(self.df['total_unique_dependencies'], bins=30, alpha=0.7, color='skyblue', edgecolor='black')
        ax1.set_xlabel('Number of Dependencies')
        ax1.set_ylabel('Number of Submodules')
        ax1.set_title('Distribution of Dependencies per Submodule')
        ax1.grid(True, alpha=0.3)
        
        # Plot 2: Conflict percentage distribution
        self.df['total_conflict_percentage'] = self.df['omitted_conflict_percentage'] + self.df['version_managed_percentage']
        ax2.hist(self.df['total_conflict_percentage'], bins=30, alpha=0.7, color='lightcoral', edgecolor='black')
        ax2.set_xlabel('Conflict Percentage (%)')
        ax2.set_ylabel('Number of Submodules')
        ax2.set_title('Distribution of Conflict Percentages')
        ax2.grid(True, alpha=0.3)
        
        # Plot 3: Conflict types comparison
        conflict_types = ['Omitted Conflicts', 'Version Managed Conflicts']
        conflict_totals = [self.df['omitted_conflict_count'].sum(), self.df['version_managed_count'].sum()]
        bars = ax3.bar(conflict_types, conflict_totals, color=['orange', 'lightgreen'], alpha=0.7, edgecolor='black')
        ax3.set_ylabel('Total Number of Conflicts')
        ax3.set_title('Conflict Types Comparison')
        ax3.grid(True, alpha=0.3, axis='y')
        
        # Add value labels on bars
        for bar, value in zip(bars, conflict_totals):
            ax3.text(bar.get_x() + bar.get_width()/2., bar.get_height() + max(conflict_totals)*0.01,
                    f'{value:,}', ha='center', va='bottom', fontweight='bold')
        
        # Plot 4: Project-level submodule distribution
        project_submodule_counts = self.df.groupby('project')['submodule'].count()
        ax4.hist(project_submodule_counts, bins=20, alpha=0.7, color='mediumpurple', edgecolor='black')
        ax4.set_xlabel('Number of Submodules per Project')
        ax4.set_ylabel('Number of Projects')
        ax4.set_title('Distribution of Submodules per Project')
        ax4.grid(True, alpha=0.3)
        
        plt.tight_layout()
        
        # Save the plot
        output_file = Path(output_dir) / 'rq1_summary_statistics.png'
        plt.savefig(output_file, dpi=300, bbox_inches='tight')
        plt.show()
        
        print(f"Summary visualizations saved to: {output_file}")
    
    def generate_report(self, output_dir="analysis_results"):
        """
        Generate a comprehensive text report.
        
        Args:
            output_dir (str): Directory to save the report
        """
        Path(output_dir).mkdir(exist_ok=True)
        
        # Calculate all statistics
        basic_stats = self.calculate_basic_stats()
        project_analysis = self.analyze_project_patterns()
        conflict_distribution = self.analyze_conflict_distribution()
        
        # Create report
        report_file = Path(output_dir) / 'rq1_comprehensive_summary_report.txt'
        
        with open(report_file, 'w') as f:
            f.write("RQ1: Dependency Conflict Analysis - Comprehensive Summary Report\n")
            f.write("=" * 70 + "\n\n")
            
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
            
            # Conflict Statistics
            f.write("CONFLICT STATISTICS\n")
            f.write("-" * 20 + "\n")
            f.write(f"Total Omitted Conflicts: {basic_stats['total_omitted_conflicts']:,}\n")
            f.write(f"Total Version Managed Conflicts: {basic_stats['total_version_managed_conflicts']:,}\n")
            f.write(f"Total All Conflicts: {basic_stats['total_all_conflicts']:,}\n")
            f.write(f"Average Omitted Conflict Percentage: {basic_stats['avg_omitted_conflict_percentage']:.2f}%\n")
            f.write(f"Average Version Managed Percentage: {basic_stats['avg_version_managed_percentage']:.2f}%\n")
            f.write(f"Average Total Conflict Percentage: {basic_stats['avg_total_conflict_percentage']:.2f}%\n\n")
            
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
            f.write(f"Submodules with Both Conflict Types: {conflict_distribution['submodules_with_both_types']:,}\n\n")
            
            # Conflict Severity
            f.write("CONFLICT SEVERITY (by percentage)\n")
            f.write("-" * 30 + "\n")
            f.write(f"High Conflict Submodules (>20%): {conflict_distribution['high_conflict_submodules']:,}\n")
            f.write(f"Medium Conflict Submodules (10-20%): {conflict_distribution['medium_conflict_submodules']:,}\n")
            f.write(f"Low Conflict Submodules (0-10%): {conflict_distribution['low_conflict_submodules']:,}\n\n")
            
            # Top Projects by Various Metrics
            f.write("TOP PROJECTS BY METRICS\n")
            f.write("-" * 25 + "\n")
            project_stats = project_analysis['project_stats']
            
            f.write("Top 5 Projects by Total Conflicts:\n")
            top_conflicts = project_stats.nlargest(5, 'total_conflicts')
            for project, row in top_conflicts.iterrows():
                f.write(f"  {project}: {row['total_conflicts']} conflicts\n")
            
            f.write("\nTop 5 Projects by Conflict Percentage:\n")
            top_percentage = project_stats.nlargest(5, 'avg_total_conflict_percentage')
            for project, row in top_percentage.iterrows():
                f.write(f"  {project}: {row['avg_total_conflict_percentage']:.1f}% conflict rate\n")
            
            f.write("\nTop 5 Projects by Number of Submodules:\n")
            top_submodules = project_stats.nlargest(5, 'num_submodules')
            for project, row in top_submodules.iterrows():
                f.write(f"  {project}: {row['num_submodules']} submodules\n")
        
        print(f"Comprehensive report saved to: {report_file}")
        
        # Save detailed project statistics as CSV
        csv_file = Path(output_dir) / 'rq1_project_level_statistics.csv'
        project_analysis['project_stats'].to_csv(csv_file)
        print(f"Project-level statistics saved to: {csv_file}")
        
        return basic_stats, project_analysis, conflict_distribution
    
    def run_complete_analysis(self):
        """
        Run the complete summary analysis pipeline.
        """
        print("Starting RQ1 Summary Statistics Analysis...")
        print("=" * 50)
        
        # Load data
        self.load_data()
        
        # Generate report and statistics
        basic_stats, project_analysis, conflict_distribution = self.generate_report()
        
        # Create visualizations
        self.create_visualizations()
        
        print("\nSummary Analysis Complete!")
        print("=" * 50)
        print(f"Key Findings:")
        print(f"- Analyzed {basic_stats['unique_projects']:,} projects with {basic_stats['total_records']:,} submodules")
        print(f"- Projects with conflicts: {basic_stats['unique_projects_with_conflicts']:,} ({(basic_stats['unique_projects_with_conflicts'] / basic_stats['unique_projects'] * 100):.1f}%)")
        print(f"- Projects without conflicts: {basic_stats['unique_projects_without_conflicts']:,} ({(basic_stats['unique_projects_without_conflicts'] / basic_stats['unique_projects'] * 100):.1f}%)")
        print(f"- Total dependencies: {basic_stats['total_dependencies']:,}")
        print(f"- Total conflicts: {basic_stats['total_all_conflicts']:,}")
        print(f"- Average conflict rate: {basic_stats['avg_total_conflict_percentage']:.2f}%")
        
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
    
    print("\nFiles generated in 'analysis_results' directory:")
    print("- rq1_summary_statistics.png (visualizations)")
    print("- rq1_comprehensive_summary_report.txt (detailed report)")
    print("- rq1_project_level_statistics.csv (project-level data)")


if __name__ == "__main__":
    main()