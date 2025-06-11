import os
import csv
from pathlib import Path
from collections import defaultdict

class SubmoduleStats:
    def __init__(self, project, submodule):
        self.project = project
        self.submodule = submodule
        self.has_conflict = False
        self.has_version_management = False
        self.dependencies_with_conflict = set()
        self.dependencies_with_version_management = set()
        self.all_dependencies = set()

class ProjectSummaryAggregator:
    def __init__(self, csv_files_dir, output_dir="output"):
        self.csv_files_dir = Path(csv_files_dir)
        self.output_dir = Path(output_dir)
        self.submodule_stats = []
        self.project_set = set()

        # Create output directory if it doesn't exist
        self.output_dir.mkdir(exist_ok=True)

    def extract_submodule_stats_from_csv(self, csv_file_path):
        project_name = csv_file_path.stem.replace('_version_conflicts', '')
        submodule_stats_map = {}

        with open(csv_file_path, 'r', encoding='utf-8') as file:
            reader = csv.DictReader(file)
            for row in reader:
                # Stop at summary section
                if row.get('project', '').strip() == 'PROJECT SUMMARY':
                    break
                submodule = row.get('submodule', 'main')
                conflict_type = row.get('conflict_type', '').strip()
                library_name = row.get('library_name', '').strip()
                if not submodule:
                    submodule = 'main'
                key = (project_name, submodule)
                if key not in submodule_stats_map:
                    submodule_stats_map[key] = SubmoduleStats(project_name, submodule)
                stats = submodule_stats_map[key]
                stats.all_dependencies.add(library_name)
                if conflict_type == 'omitted_conflict':
                    stats.has_conflict = True
                    stats.dependencies_with_conflict.add(library_name)
                elif conflict_type == 'version_managed':
                    stats.has_version_management = True
                    stats.dependencies_with_version_management.add(library_name)
        return list(submodule_stats_map.values())

    def aggregate_all_submodules(self):
        csv_files = [f for f in self.csv_files_dir.glob('*_version_conflicts.csv')]
        for csv_file in csv_files:
            submodules = self.extract_submodule_stats_from_csv(csv_file)
            self.submodule_stats.extend(submodules)
            for s in submodules:
                self.project_set.add(s.project)

    def export_and_print_summary(self):
        total_projects = len(self.project_set)
        total_submodules = len(self.submodule_stats)
        submodules_with_conflict = [s for s in self.submodule_stats if s.has_conflict]
        submodules_with_version_mgmt = [s for s in self.submodule_stats if s.has_version_management]
        submodules_with_both = [s for s in self.submodule_stats if s.has_conflict and s.has_version_management]

        # Dependency-level stats
        all_deps = set()
        deps_with_conflict = set()
        deps_with_version_mgmt = set()

        for s in self.submodule_stats:
            all_deps.update(s.all_dependencies)
            deps_with_conflict.update(s.dependencies_with_conflict)
            deps_with_version_mgmt.update(s.dependencies_with_version_management)

        total_unique_deps = len(all_deps)
        conflict_count = len(deps_with_conflict)
        version_mgmt_count = len(deps_with_version_mgmt)
        conflict_pct = (conflict_count / total_unique_deps * 100) if total_unique_deps else 0
        version_mgmt_pct = (version_mgmt_count / total_unique_deps * 100) if total_unique_deps else 0

        # Print summary
        print("\n=== HIGH-LEVEL CONFLICT STATISTICS ===")
        print(f"Total Projects Analyzed: {total_projects}")
        print(f"Total Submodules Analyzed: {total_submodules}")
        print(f"Submodules with Conflict: {len(submodules_with_conflict)}")
        print(f"Submodules Using Version Management: {len(submodules_with_version_mgmt)}")
        print(f"Submodules with Both Conflicts and Version Management: {len(submodules_with_both)}")

        print("\n=== DEPENDENCY CONFLICT OCCURRENCE STATS (MODULE LEVEL) ===")
        print(f"Total unique dependencies analyzed: {total_unique_deps}")
        print(f"Dependencies with omitted_for_conflict: {conflict_count} ({conflict_pct:.2f}%)")
        print(f"Dependencies with version_managed: {version_mgmt_count} ({version_mgmt_pct:.2f}%)")

        # Export to CSV
        output_file = "aggregated_submodule_summary.csv"
        output_path = self.output_dir / output_file
        with open(output_path, 'w', newline='', encoding='utf-8') as csvfile:
            writer = csv.writer(csvfile)
            writer.writerow(["Metric", "Value"])
            writer.writerow(["Total Projects Analyzed", total_projects])
            writer.writerow(["Total Submodules Analyzed", total_submodules])
            writer.writerow(["Submodules with Conflict", len(submodules_with_conflict)])
            writer.writerow(["Submodules Using Version Management", len(submodules_with_version_mgmt)])
            writer.writerow(["Submodules with Both Conflicts and Version Management", len(submodules_with_both)])
            writer.writerow([])
            writer.writerow(["Total unique dependencies analyzed", total_unique_deps])
            writer.writerow(["Dependencies with omitted_for_conflict (count)", conflict_count])
            writer.writerow(["Dependencies with omitted_for_conflict (%)", f"{conflict_pct:.2f}"])
            writer.writerow(["Dependencies with version_managed (count)", version_mgmt_count])
            writer.writerow(["Dependencies with version_managed (%)", f"{version_mgmt_pct:.2f}"])
        print(f"\nSummary exported to: {output_path}")

    def export_project_summaries(self):
        # Group submodule stats by project
        projects = defaultdict(list)
        for s in self.submodule_stats:
            projects[s.project].append(s)

        for project, submodules in projects.items():
            total_submodules = len(submodules)
            submodules_with_conflict = [s for s in submodules if s.has_conflict]
            submodules_with_version_mgmt = [s for s in submodules if s.has_version_management]
            submodules_with_both = [s for s in submodules if s.has_conflict and s.has_version_management]

            all_deps = set()
            deps_with_conflict = set()
            deps_with_version_mgmt = set()
            for s in submodules:
                all_deps.update(s.all_dependencies)
                deps_with_conflict.update(s.dependencies_with_conflict)
                deps_with_version_mgmt.update(s.dependencies_with_version_management)

            total_unique_deps = len(all_deps)
            conflict_count = len(deps_with_conflict)
            version_mgmt_count = len(deps_with_version_mgmt)
            conflict_pct = (conflict_count / total_unique_deps * 100) if total_unique_deps else 0
            version_mgmt_pct = (version_mgmt_count / total_unique_deps * 100) if total_unique_deps else 0

            output_file = f"{project}_summary.csv"
            output_path = self.output_dir / output_file
            with open(output_path, 'w', newline='', encoding='utf-8') as csvfile:
                writer = csv.writer(csvfile)
                writer.writerow(["Metric", "Value"])
                writer.writerow(["Total Submodules Analyzed", total_submodules])
                writer.writerow(["Submodules with Conflict", len(submodules_with_conflict)])
                writer.writerow(["Submodules Using Version Management", len(submodules_with_version_mgmt)])
                writer.writerow(["Submodules with Both Conflicts and Version Management", len(submodules_with_both)])
                writer.writerow([])
                writer.writerow(["Total unique dependencies analyzed", total_unique_deps])
                writer.writerow(["Dependencies with omitted_for_conflict (count)", conflict_count])
                writer.writerow(["Dependencies with omitted_for_conflict (%)", f"{conflict_pct:.2f}"])
                writer.writerow(["Dependencies with version_managed (count)", version_mgmt_count])
                writer.writerow(["Dependencies with version_managed (%)", f"{version_mgmt_pct:.2f}"])
            print(f"Project summary exported to: {output_path}")

    def run(self):
        print("Starting submodule-level summary aggregation...")
        self.aggregate_all_submodules()
        self.export_and_print_summary()
        self.export_project_summaries()

def main():
    csv_files_dir = "output"
    output_dir = "output"
    if not os.path.exists(csv_files_dir):
        print(f"Error: Directory {csv_files_dir} not found!")
        return
    aggregator = ProjectSummaryAggregator(csv_files_dir, output_dir)
    aggregator.run()

if __name__ == "__main__":
    main()