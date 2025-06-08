import os
import csv
import re
from pathlib import Path

class ProjectSummaryAggregator:
    def __init__(self, csv_files_dir, output_dir="output"):
        self.csv_files_dir = Path(csv_files_dir)
        self.output_dir = Path(output_dir)
        self.project_summaries = []
        
        # Create output directory if it doesn't exist
        self.output_dir.mkdir(exist_ok=True)
    
    def extract_summary_from_csv(self, csv_file_path):
        """Extract summary statistics from an individual project CSV file"""
        project_name = csv_file_path.stem.replace('_version_conflicts', '')
        
        summary_data = {
            'project_name': project_name,
            'total_unique_dependencies': 0,
            'dependencies_with_version_conflicts_count': 0,
            'dependencies_with_version_conflicts_percentage': 0.0,
            'dependencies_with_version_managed_count': 0,
            'dependencies_with_version_managed_percentage': 0.0
        }
        
        try:
            with open(csv_file_path, 'r', encoding='utf-8') as file:
                reader = csv.reader(file)
                rows = list(reader)
                
                # Look for the summary section
                summary_started = False
                for row in rows:
                    if len(row) >= 2:
                        metric = row[0].strip()
                        value = row[1].strip()
                        
                        # Check if we've reached the summary section
                        if metric == 'PROJECT SUMMARY':
                            summary_started = True
                            continue
                        
                        # Parse summary metrics
                        if summary_started and metric and value:
                            if metric == 'Total unique dependencies':
                                try:
                                    summary_data['total_unique_dependencies'] = int(value)
                                except ValueError:
                                    pass
                            elif metric == 'Dependencies with version conflicts (count)':
                                try:
                                    summary_data['dependencies_with_version_conflicts_count'] = int(value)
                                except ValueError:
                                    pass
                            elif metric == 'Dependencies with version conflicts (%)':
                                try:
                                    # Remove % sign and convert to float
                                    pct_value = value.replace('%', '').strip()
                                    summary_data['dependencies_with_version_conflicts_percentage'] = float(pct_value)
                                except ValueError:
                                    pass
                            elif metric == 'Dependencies with version managed (count)':
                                try:
                                    summary_data['dependencies_with_version_managed_count'] = int(value)
                                except ValueError:
                                    pass
                            elif metric == 'Dependencies with version managed (%)':
                                try:
                                    # Remove % sign and convert to float
                                    pct_value = value.replace('%', '').strip()
                                    summary_data['dependencies_with_version_managed_percentage'] = float(pct_value)
                                except ValueError:
                                    pass
        
        except Exception as e:
            print(f"Error reading {csv_file_path}: {e}")
        
        return summary_data
    
    def aggregate_all_summaries(self):
        """Read all project CSV files and extract their summary data"""
        # Find all version conflicts CSV files (exclude the summary file)
        csv_files = [f for f in self.csv_files_dir.glob('*_version_conflicts.csv')]
        
        if not csv_files:
            print("No project CSV files found!")
            return
        
        print(f"Found {len(csv_files)} project CSV files to process")
        
        for csv_file in csv_files:
            print(f"Processing: {csv_file.name}")
            summary = self.extract_summary_from_csv(csv_file)
            self.project_summaries.append(summary)
    
    def export_aggregated_summary(self):
        """Export the aggregated summary to a new CSV file"""
        if not self.project_summaries:
            print("No project summaries to export!")
            return
        
        output_file = "aggregated_projects_summary.csv"
        output_path = self.output_dir / output_file
        
        fieldnames = [
            'project_name',
            'total_unique_dependencies',
            'dependencies_with_version_conflicts_count',
            'dependencies_with_version_conflicts_percentage',
            'dependencies_with_version_managed_count',
            'dependencies_with_version_managed_percentage'
        ]
        
        with open(output_path, 'w', newline='', encoding='utf-8') as csvfile:
            writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
            writer.writeheader()
            
            # Write each project's summary
            for project_summary in self.project_summaries:
                writer.writerow(project_summary)
            
            # Calculate totals across all projects
            total_unique_deps = sum(p['total_unique_dependencies'] for p in self.project_summaries)
            total_version_conflicts = sum(p['dependencies_with_version_conflicts_count'] for p in self.project_summaries)
            total_version_managed = sum(p['dependencies_with_version_managed_count'] for p in self.project_summaries)
            
            # Calculate aggregate percentages
            total_version_conflicts_pct = (total_version_conflicts / total_unique_deps * 100) if total_unique_deps > 0 else 0
            total_version_managed_pct = (total_version_managed / total_unique_deps * 100) if total_unique_deps > 0 else 0
            
            # Add empty row for separation
            writer.writerow({})
            
            # Write totals row
            totals_row = {
                'project_name': 'TOTAL (All Projects)',
                'total_unique_dependencies': total_unique_deps,
                'dependencies_with_version_conflicts_count': total_version_conflicts,
                'dependencies_with_version_conflicts_percentage': round(total_version_conflicts_pct, 2),
                'dependencies_with_version_managed_count': total_version_managed,
                'dependencies_with_version_managed_percentage': round(total_version_managed_pct, 2)
            }
            writer.writerow(totals_row)
        
        print(f"Aggregated summary exported to: {output_path}")
        
        # Print summary to console
        print(f"\n=== AGGREGATED SUMMARY ===")
        print(f"Total projects processed: {len(self.project_summaries)}")
        print(f"Total unique dependencies across all projects: {total_unique_deps}")
        print(f"Total dependencies with version conflicts: {total_version_conflicts} ({total_version_conflicts_pct:.2f}%)")
        print(f"Total dependencies with version managed: {total_version_managed} ({total_version_managed_pct:.2f}%)")
    
    def run(self):
        """Main execution method"""
        print("Starting project summary aggregation...")
        self.aggregate_all_summaries()
        self.export_aggregated_summary()

def main():
    csv_files_dir = "output"  # Directory containing the individual project CSV files
    output_dir = "output"     # Directory to place the aggregated summary
    
    if not os.path.exists(csv_files_dir):
        print(f"Error: Directory {csv_files_dir} not found!")
        return
    
    aggregator = ProjectSummaryAggregator(csv_files_dir, output_dir)
    aggregator.run()

if __name__ == "__main__":
    main()