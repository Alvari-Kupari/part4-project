import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np
from pathlib import Path
import glob
from collections import Counter, defaultdict
import warnings
warnings.filterwarnings('ignore')

class DependencyConflictAnalyzer:
    def __init__(self, csv_dir="output"):
        self.csv_dir = Path(csv_dir)
        self.all_data = None
        self.projects_summary = None
        
    def load_all_csv_files(self):
        """Load and combine all CSV files"""
        csv_files = list(self.csv_dir.glob("*_version_conflicts.csv"))
        
        all_dataframes = []
        projects_info = []
        
        for csv_file in csv_files:
            try:
                df = pd.read_csv(csv_file)
                project_name = csv_file.stem.replace('_version_conflicts', '')
                
                # Add project info
                projects_info.append({
                    'project_name': project_name,
                    'total_conflicts': len(df),
                    'unique_libraries': df['library_name'].nunique() if not df.empty else 0,
                    'has_conflicts': len(df) > 0
                })
                
                if not df.empty:
                    all_dataframes.append(df)
                    
            except Exception as e:
                print(f"Error loading {csv_file}: {e}")
        
        self.all_data = pd.concat(all_dataframes, ignore_index=True) if all_dataframes else pd.DataFrame()
        self.projects_summary = pd.DataFrame(projects_info)
        
        print(f"Loaded {len(csv_files)} CSV files")
        print(f"Total conflicts across all projects: {len(self.all_data)}")
        
    def create_visualizations(self):
        """Create the 4 most useful visualizations for RQ2"""
        plt.style.use('default')
        
        # Create output directory for visualizations
        output_dir = Path("../data/download_analysis")
        output_dir.mkdir(exist_ok=True)
        
        # 1. Project-level conflict frequency distribution (MOST IMPORTANT for RQ2)
        fig1, ax1 = plt.subplots(figsize=(10, 8))
        conflict_counts = self.projects_summary['total_conflicts']
        bins = [0, 1, 5, 10, 20, 50, 100, conflict_counts.max() + 1]
        plt.hist(conflict_counts, bins=bins, edgecolor='black', alpha=0.7, color='skyblue')
        plt.xlabel('Number of Conflicts per Project')
        plt.ylabel('Number of Projects')
        plt.title('RQ2: Distribution of Version Conflicts Across Projects')
        plt.yscale('log')
        
        # Add text annotations for key statistics
        total_projects = len(self.projects_summary)
        projects_with_conflicts = self.projects_summary['has_conflicts'].sum()
        plt.text(0.6, 0.8, f'Projects with conflicts:\n{projects_with_conflicts}/{total_projects} ({projects_with_conflicts/total_projects*100:.1f}%)', 
                 transform=ax1.transAxes, bbox=dict(boxstyle="round,pad=0.3", facecolor="yellow", alpha=0.7))
        
        plt.tight_layout()
        plt.savefig(output_dir / 'conflict_frequency_distribution.png', dpi=300, bbox_inches='tight')
        plt.close()
        
        # 2. Projects with vs without conflicts (Critical for frequency analysis)
        fig2, ax2 = plt.subplots(figsize=(8, 8))
        conflict_status = self.projects_summary['has_conflicts'].value_counts()
        colors = ['lightcoral', 'lightgreen']
        plt.pie([conflict_status[False], conflict_status[True]], 
                labels=[f'No Conflicts\n({conflict_status[False]} projects)', 
                       f'Has Conflicts\n({conflict_status[True]} projects)'], 
                autopct='%1.1f%%', startangle=90, colors=colors)
        plt.title('RQ2: Project Conflict Frequency Overview')
        
        plt.tight_layout()
        plt.savefig(output_dir / 'project_conflict_overview.png', dpi=300, bbox_inches='tight')
        plt.close()
        
        # 3. Most problematic libraries (Shows which dependencies cause most conflicts)
        fig3, ax3 = plt.subplots(figsize=(12, 8))
        if not self.all_data.empty:
            top_libraries = self.all_data['library_name'].value_counts().head(10)
            bars = plt.barh(range(len(top_libraries)), top_libraries.values, color='orange', alpha=0.7)
            plt.yticks(range(len(top_libraries)), [lib.split(':')[-1] if ':' in lib else lib for lib in top_libraries.index], fontsize=9)
            plt.xlabel('Number of Conflicts Across All Projects')
            plt.title('Top 10 Libraries Causing Version Conflicts')
            plt.gca().invert_yaxis()
            
            # Add value labels on bars
            for i, bar in enumerate(bars):
                width = bar.get_width()
                projects_affected = self.all_data[self.all_data['library_name'] == top_libraries.index[i]]['project'].nunique()
                plt.text(width + 0.5, bar.get_y() + bar.get_height()/2, 
                        f'{int(width)} ({projects_affected}p)', 
                        ha='left', va='center', fontsize=8)
        
        plt.tight_layout()
        plt.savefig(output_dir / 'top_problematic_libraries.png', dpi=300, bbox_inches='tight')
        plt.close()
        
        # 4. Cumulative conflicts analysis (Shows concentration of conflicts)
        fig4, ax4 = plt.subplots(figsize=(10, 8))
        sorted_conflicts = self.projects_summary['total_conflicts'].sort_values(ascending=False)
        cumulative_conflicts = sorted_conflicts.cumsum()
        total_conflicts = cumulative_conflicts.iloc[-1] if len(cumulative_conflicts) > 0 else 0
        
        plt.plot(range(1, len(cumulative_conflicts) + 1), cumulative_conflicts, 'b-', linewidth=2)
        plt.fill_between(range(1, len(cumulative_conflicts) + 1), cumulative_conflicts, alpha=0.3)
        plt.xlabel('Number of Projects (ranked by conflicts)')
        plt.ylabel('Cumulative Conflicts')
        plt.title('Cumulative Distribution of Version Conflicts')
        plt.grid(True, alpha=0.3)
        
        # Add 80/20 rule annotation
        if len(cumulative_conflicts) > 0:
            # Find where 80% of conflicts are reached
            eighty_percent = total_conflicts * 0.8
            projects_for_80_percent = (cumulative_conflicts <= eighty_percent).sum()
            percent_projects = projects_for_80_percent / len(cumulative_conflicts) * 100
            
            plt.axhline(y=eighty_percent, color='red', linestyle='--', alpha=0.7)
            plt.axvline(x=projects_for_80_percent, color='red', linestyle='--', alpha=0.7)
            plt.text(0.5, 0.6, f'{percent_projects:.1f}% of projects\ncause 80% of conflicts', 
                    transform=ax4.transAxes, bbox=dict(boxstyle="round,pad=0.3", facecolor="lightyellow"))
        
        plt.tight_layout()
        plt.savefig(output_dir / 'cumulative_conflicts_analysis.png', dpi=300, bbox_inches='tight')
        plt.close()
        
        # Create combined visualization for overview
        fig_combined = plt.figure(figsize=(16, 12))
        
        # Recreate all 4 plots in a single figure
        ax1 = plt.subplot(2, 2, 1)
        conflict_counts = self.projects_summary['total_conflicts']
        bins = [0, 1, 5, 10, 20, 50, 100, conflict_counts.max() + 1]
        plt.hist(conflict_counts, bins=bins, edgecolor='black', alpha=0.7, color='skyblue')
        plt.xlabel('Number of Conflicts per Project')
        plt.ylabel('Number of Projects')
        plt.title('RQ2: Distribution of Version Conflicts Across Projects')
        plt.yscale('log')
        plt.text(0.6, 0.8, f'Projects with conflicts:\n{projects_with_conflicts}/{total_projects} ({projects_with_conflicts/total_projects*100:.1f}%)', 
                 transform=ax1.transAxes, bbox=dict(boxstyle="round,pad=0.3", facecolor="yellow", alpha=0.7))
        
        ax2 = plt.subplot(2, 2, 2)
        conflict_status = self.projects_summary['has_conflicts'].value_counts()
        colors = ['lightcoral', 'lightgreen']
        plt.pie([conflict_status[False], conflict_status[True]], 
                labels=[f'No Conflicts\n({conflict_status[False]} projects)', 
                       f'Has Conflicts\n({conflict_status[True]} projects)'], 
                autopct='%1.1f%%', startangle=90, colors=colors)
        plt.title('RQ2: Project Conflict Frequency Overview')
        
        ax3 = plt.subplot(2, 2, 3)
        if not self.all_data.empty:
            top_libraries = self.all_data['library_name'].value_counts().head(10)
            bars = plt.barh(range(len(top_libraries)), top_libraries.values, color='orange', alpha=0.7)
            plt.yticks(range(len(top_libraries)), [lib.split(':')[-1] if ':' in lib else lib for lib in top_libraries.index], fontsize=9)
            plt.xlabel('Number of Conflicts Across All Projects')
            plt.title('Top 10 Libraries Causing Version Conflicts')
            plt.gca().invert_yaxis()
            
            for i, bar in enumerate(bars):
                width = bar.get_width()
                projects_affected = self.all_data[self.all_data['library_name'] == top_libraries.index[i]]['project'].nunique()
                plt.text(width + 0.5, bar.get_y() + bar.get_height()/2, 
                        f'{int(width)} ({projects_affected}p)', 
                        ha='left', va='center', fontsize=8)
        
        ax4 = plt.subplot(2, 2, 4)
        sorted_conflicts = self.projects_summary['total_conflicts'].sort_values(ascending=False)
        cumulative_conflicts = sorted_conflicts.cumsum()
        total_conflicts = cumulative_conflicts.iloc[-1] if len(cumulative_conflicts) > 0 else 0
        
        plt.plot(range(1, len(cumulative_conflicts) + 1), cumulative_conflicts, 'b-', linewidth=2)
        plt.fill_between(range(1, len(cumulative_conflicts) + 1), cumulative_conflicts, alpha=0.3)
        plt.xlabel('Number of Projects (ranked by conflicts)')
        plt.ylabel('Cumulative Conflicts')
        plt.title('Cumulative Distribution of Version Conflicts')
        plt.grid(True, alpha=0.3)
        
        if len(cumulative_conflicts) > 0:
            eighty_percent = total_conflicts * 0.8
            projects_for_80_percent = (cumulative_conflicts <= eighty_percent).sum()
            percent_projects = projects_for_80_percent / len(cumulative_conflicts) * 100
            
            plt.axhline(y=eighty_percent, color='red', linestyle='--', alpha=0.7)
            plt.axvline(x=projects_for_80_percent, color='red', linestyle='--', alpha=0.7)
            plt.text(0.5, 0.6, f'{percent_projects:.1f}% of projects\ncause 80% of conflicts', 
                    transform=ax4.transAxes, bbox=dict(boxstyle="round,pad=0.3", facecolor="lightyellow"))
        
        plt.tight_layout(pad=3.0)
        plt.savefig(output_dir / 'dependency_conflict_analysis_combined.png', dpi=300, bbox_inches='tight')
        plt.show()
        
        print(f"\n📁 Visualizations saved to {output_dir}:")
        print("├── conflict_frequency_distribution.png")
        print("├── project_conflict_overview.png") 
        print("├── top_problematic_libraries.png")
        print("├── cumulative_conflicts_analysis.png")
        print("└── dependency_conflict_analysis_combined.png")
    
    def generate_summary_tables(self):
        """Generate comprehensive summary tables"""
        print("=" * 80)
        print("DEPENDENCY VERSION CONFLICT ANALYSIS SUMMARY")
        print("=" * 80)
        
        # Overall statistics
        total_projects = len(self.projects_summary)
        projects_with_conflicts = self.projects_summary['has_conflicts'].sum()
        total_conflicts = self.all_data.shape[0] if not self.all_data.empty else 0
        unique_libraries = self.all_data['library_name'].nunique() if not self.all_data.empty else 0
        
        print(f"\n📊 OVERALL STATISTICS:")
        print(f"├── Total projects analyzed: {total_projects}")
        print(f"├── Projects with conflicts: {projects_with_conflicts} ({projects_with_conflicts/total_projects*100:.1f}%)")
        print(f"├── Projects without conflicts: {total_projects - projects_with_conflicts} ({(total_projects - projects_with_conflicts)/total_projects*100:.1f}%)")
        print(f"├── Total version conflicts: {total_conflicts:,}")
        print(f"├── Unique libraries with conflicts: {unique_libraries}")
        print(f"└── Average conflicts per project: {total_conflicts/total_projects:.2f}")
        
        if not self.all_data.empty:
            # Conflict frequency analysis
            print(f"\n🔍 CONFLICT FREQUENCY ANALYSIS:")
            conflict_ranges = [
                (1, 5, "Low (1-5 conflicts)"),
                (6, 20, "Medium (6-20 conflicts)"),
                (21, 50, "High (21-50 conflicts)"),
                (51, float('inf'), "Very High (50+ conflicts)")
            ]
            
            for min_val, max_val, label in conflict_ranges:
                count = len(self.projects_summary[
                    (self.projects_summary['total_conflicts'] >= min_val) & 
                    (self.projects_summary['total_conflicts'] <= max_val)
                ])
                percentage = count / total_projects * 100
                print(f"├── {label}: {count} projects ({percentage:.1f}%)")
            
            # Top problematic libraries
            print(f"\n🚨 TOP 10 MOST PROBLEMATIC LIBRARIES:")
            top_libraries = self.all_data['library_name'].value_counts().head(10)
            for i, (library, count) in enumerate(top_libraries.items(), 1):
                projects_affected = self.all_data[self.all_data['library_name'] == library]['project'].nunique()
                print(f"{i:2d}. {library}")
                print(f"    ├── Total conflicts: {count}")
                print(f"    └── Projects affected: {projects_affected}")
            
            # Conflict types breakdown
            print(f"\n⚙️  CONFLICT TYPES:")
            conflict_types = self.all_data['conflict_type'].value_counts()
            for conflict_type, count in conflict_types.items():
                percentage = count / total_conflicts * 100
                print(f"├── {conflict_type}: {count} ({percentage:.1f}%)")
            
            # Depth analysis
            print(f"\n📏 DEPENDENCY DEPTH ANALYSIS:")
            print(f"├── Selected version depth - Mean: {self.all_data['depth_selected'].mean():.2f}, Median: {self.all_data['depth_selected'].median():.1f}")
            print(f"├── Conflicting version depth - Mean: {self.all_data['depth_conflicting'].mean():.2f}, Median: {self.all_data['depth_conflicting'].median():.1f}")
            
            # Scope analysis
            print(f"\n🎯 SCOPE ANALYSIS:")
            scope_counts = self.all_data['scope_selected'].value_counts()
            for scope, count in scope_counts.items():
                percentage = count / total_conflicts * 100
                print(f"├── {scope}: {count} ({percentage:.1f}%)")
        
        # Research question specific insights
        print(f"\n🔬 RESEARCH QUESTION INSIGHTS (RQ2):")
        print(f"├── Conflict frequency: {projects_with_conflicts}/{total_projects} projects ({projects_with_conflicts/total_projects*100:.1f}%) experience version conflicts")
        if not self.all_data.empty:
            avg_conflicts_when_present = self.projects_summary[self.projects_summary['has_conflicts']]['total_conflicts'].mean()
            print(f"├── When conflicts occur: Average of {avg_conflicts_when_present:.1f} conflicts per affected project")
            print(f"├── Library reuse conflicts: {unique_libraries} unique libraries cause conflicts across multiple projects")
            
            # Most reused conflicting libraries
            library_project_counts = self.all_data.groupby('library_name')['project'].nunique().sort_values(ascending=False)
            multi_project_libraries = library_project_counts[library_project_counts > 1]
            print(f"└── Libraries causing conflicts in multiple projects: {len(multi_project_libraries)}")
    
    def export_detailed_tables(self):
        """Export detailed analysis tables to CSV in download_analysis folder"""
        if self.all_data.empty:
            print("No data to export")
            return
        
        # Create output directory for CSV files
        output_dir = Path("../data/download_analysis")
        output_dir.mkdir(exist_ok=True)
            
        # 1. Project summary table
        project_analysis = self.projects_summary.copy()
        project_analysis['conflict_density'] = project_analysis['total_conflicts'] / project_analysis['unique_libraries'].replace(0, 1)
        project_analysis.to_csv(output_dir / 'project_conflict_summary.csv', index=False)
        
        # 2. Library analysis table
        library_analysis = self.all_data.groupby('library_name').agg({
            'project': 'nunique',
            'version_selected': 'nunique',
            'conflicting_version': 'nunique',
            'conflict_type': lambda x: x.value_counts().to_dict(),
            'depth_selected': ['mean', 'min', 'max'],
            'depth_conflicting': ['mean', 'min', 'max']
        }).round(2)
        
        library_analysis.columns = ['projects_affected', 'unique_selected_versions', 'unique_conflicting_versions', 
                                   'conflict_type_breakdown', 'avg_selected_depth', 'min_selected_depth', 'max_selected_depth',
                                   'avg_conflicting_depth', 'min_conflicting_depth', 'max_conflicting_depth']
        
        library_analysis['total_conflicts'] = self.all_data['library_name'].value_counts()
        library_analysis = library_analysis.sort_values('total_conflicts', ascending=False)
        library_analysis.to_csv(output_dir / 'library_conflict_analysis.csv')
        
        # 3. Conflict patterns table
        conflict_patterns = self.all_data.groupby(['library_name', 'version_selected', 'conflicting_version']).agg({
            'project': 'nunique',
            'conflict_type': 'first'
        }).sort_values('project', ascending=False)
        conflict_patterns.to_csv(output_dir / 'conflict_patterns.csv')
        
        print(f"\n📁 CSV analysis tables exported to {output_dir}:")
        print("├── project_conflict_summary.csv")
        print("├── library_conflict_analysis.csv")
        print("└── conflict_patterns.csv")

def main():
    analyzer = DependencyConflictAnalyzer("output")
    
    print("Loading CSV files...")
    analyzer.load_all_csv_files()
    
    print("\nGenerating visualizations...")
    analyzer.create_visualizations()
    
    print("\nGenerating summary tables...")
    analyzer.generate_summary_tables()
    
    print("\nExporting detailed analysis...")
    analyzer.export_detailed_tables()

if __name__ == "__main__":
    main()