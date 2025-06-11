import csv
import os
from pathlib import Path
from collections import defaultdict

def analyze_conflict_csv(csv_path):
    project = csv_path.stem.replace('_version_conflicts', '')
    submodule_stats = {}
    omitted_conflict_deps = defaultdict(set)
    version_managed_deps = defaultdict(set)
    all_deps = defaultdict(set)

    with open(csv_path, newline='', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        rows = list(reader)

    # Find the submodule summary section
    summary_start = None
    for i, row in enumerate(rows):
        if row['project'] == 'SUBMODULE SUMMARY':
            summary_start = i + 1
            break

    # Parse submodule summary
    if summary_start is not None:
        i = summary_start
        while i < len(rows) and rows[i]['project']:
            submodule = rows[i]['project']
            total_unique = int(rows[i]['submodule'])
            submodule_stats[submodule] = {
                'total_unique_dependencies': total_unique,
                'omitted_conflict_deps': set(),
                'version_managed_deps': set()
            }
            i += 1

    # Parse conflict rows
    for row in rows:
        submodule = row.get('submodule')
        library = row.get('library_name')
        conflict_type = row.get('conflict_type')
        if not submodule or not library or not conflict_type:
            continue
        if submodule not in submodule_stats:
            continue
        if conflict_type == 'omitted_conflict':
            submodule_stats[submodule]['omitted_conflict_deps'].add(library)
        elif conflict_type == 'version_managed':
            submodule_stats[submodule]['version_managed_deps'].add(library)

    # Prepare final stats
    results = []
    for submodule, stats in submodule_stats.items():
        total = stats['total_unique_dependencies']
        omitted_count = len(stats['omitted_conflict_deps'])
        omitted_pct = round(omitted_count / total * 100, 2) if total else 0
        managed_count = len(stats['version_managed_deps'])
        managed_pct = round(managed_count / total * 100, 2) if total else 0
        results.append({
            'project': project,
            'submodule': submodule,
            'total_unique_dependencies': total,
            'omitted_conflict_count': omitted_count,
            'omitted_conflict_percentage': omitted_pct,
            'version_managed_count': managed_count,
            'version_managed_percentage': managed_pct
        })
    return results

def main():
    output_dir = Path('output')
    summary_csv = output_dir / 'dependency_conflict_stats_summary.csv'
    all_results = []

    for csv_file in output_dir.glob('*_version_conflicts.csv'):
        all_results.extend(analyze_conflict_csv(csv_file))

    fieldnames = [
        'project',
        'submodule',
        'total_unique_dependencies',
        'omitted_conflict_count',
        'omitted_conflict_percentage',
        'version_managed_count',
        'version_managed_percentage'
    ]
    with open(summary_csv, 'w', newline='', encoding='utf-8') as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        for row in all_results:
            writer.writerow(row)
    print(f"Summary written to {summary_csv}")

if __name__ == '__main__':
    main()