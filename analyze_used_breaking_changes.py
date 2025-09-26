#!/usr/bin/env python3
"""
Analyze RQ3 results to find which modules have used breaking changes.
This script checks each module folder to see if the used-breaking-changes CSV file
contains any actual breaking changes (i.e., is not empty or has more than just headers).
"""

import os
import csv
from pathlib import Path
from typing import List, Tuple, Dict

def has_used_breaking_changes(csv_file_path: Path) -> Tuple[bool, int]:
    """
    Check if a used-breaking-changes CSV file has actual used breaking changes.
    
    Args:
        csv_file_path: Path to the used-breaking-changes CSV file
    
    Returns:
        Tuple of (has_changes, row_count)
        - has_changes: True if file has breaking changes beyond the header
        - row_count: Number of data rows (excluding header)
    """
    if not csv_file_path.exists():
        return False, 0
    
    try:
        with open(csv_file_path, 'r', encoding='utf-8') as file:
            # Count total lines first
            content = file.read().strip()
            if not content:
                return False, 0
            
            # Reset file pointer and use CSV reader
            file.seek(0)
            csv_reader = csv.reader(file)
            rows = list(csv_reader)
            
            # Check if we have more than just header row
            if len(rows) <= 1:
                return False, 0
            
            # Filter out empty rows
            data_rows = [row for row in rows[1:] if any(cell.strip() for cell in row)]
            
            return len(data_rows) > 0, len(data_rows)
    
    except Exception as e:
        print(f"Error reading {csv_file_path}: {e}")
        return False, 0

def analyze_rq3_results(rq3_csv_path: Path) -> Dict[str, any]:
    """
    Analyze all module folders in the RQ3 CSV directory.
    
    Args:
        rq3_csv_path: Path to the RQ3 CSV directory
    
    Returns:
        Dictionary with analysis results
    """
    if not rq3_csv_path.exists() or not rq3_csv_path.is_dir():
        print(f"Error: Directory {rq3_csv_path} does not exist or is not a directory")
        return {}
    
    results = {
        'modules_with_used_breaking_changes': [],
        'modules_without_used_breaking_changes': [],
        'modules_with_missing_files': [],
        'total_modules': 0,
        'total_used_breaking_changes': 0
    }
    
    # Get all subdirectories
    module_dirs = [d for d in rq3_csv_path.iterdir() if d.is_dir()]
    results['total_modules'] = len(module_dirs)
    
    print(f"Analyzing {len(module_dirs)} module directories...")
    
    for module_dir in sorted(module_dirs):
        module_name = module_dir.name
        
        # Look for the used-breaking-changes CSV file
        used_bc_pattern = f"{module_name}-used-breaking-changes.csv"
        used_bc_file = module_dir / used_bc_pattern
        
        if not used_bc_file.exists():
            results['modules_with_missing_files'].append(module_name)
            continue
        
        has_changes, change_count = has_used_breaking_changes(used_bc_file)
        
        if has_changes:
            results['modules_with_used_breaking_changes'].append({
                'module': module_name,
                'count': change_count,
                'file': str(used_bc_file)
            })
            results['total_used_breaking_changes'] += change_count
        else:
            results['modules_without_used_breaking_changes'].append(module_name)
    
    return results

def print_summary(results: Dict[str, any]):
    """Print a summary of the analysis results."""
    print("\n" + "="*80)
    print("RQ3 USED BREAKING CHANGES ANALYSIS SUMMARY")
    print("="*80)
    
    print(f"\nTotal modules analyzed: {results['total_modules']}")
    print(f"Modules with used breaking changes: {len(results['modules_with_used_breaking_changes'])}")
    print(f"Modules without used breaking changes: {len(results['modules_without_used_breaking_changes'])}")
    print(f"Modules with missing files: {len(results['modules_with_missing_files'])}")
    print(f"Total used breaking changes found: {results['total_used_breaking_changes']}")
    
    # Calculate percentages
    if results['total_modules'] > 0:
        with_changes_pct = (len(results['modules_with_used_breaking_changes']) / results['total_modules']) * 100
        without_changes_pct = (len(results['modules_without_used_breaking_changes']) / results['total_modules']) * 100
        
        print(f"\nPercentages:")
        print(f"Modules WITH used breaking changes: {with_changes_pct:.1f}%")
        print(f"Modules WITHOUT used breaking changes: {without_changes_pct:.1f}%")
    
    # Show top modules with most used breaking changes
    if results['modules_with_used_breaking_changes']:
        print(f"\nTop 10 modules with most used breaking changes:")
        sorted_modules = sorted(results['modules_with_used_breaking_changes'], 
                               key=lambda x: x['count'], reverse=True)
        for i, module_info in enumerate(sorted_modules[:10], 1):
            print(f"{i:2d}. {module_info['module']}: {module_info['count']} used breaking changes")
    
    # Show modules with missing files if any
    if results['modules_with_missing_files']:
        print(f"\nModules with missing used-breaking-changes files ({len(results['modules_with_missing_files'])}):")
        for module in results['modules_with_missing_files'][:10]:  # Show first 10
            print(f"  - {module}")
        if len(results['modules_with_missing_files']) > 10:
            print(f"  ... and {len(results['modules_with_missing_files']) - 10} more")

def save_detailed_results(results: Dict[str, any], output_file: Path):
    """Save detailed results to a CSV file."""
    with open(output_file, 'w', newline='', encoding='utf-8') as file:
        writer = csv.writer(file)
        
        # Write header
        writer.writerow(['Module', 'Has_Used_Breaking_Changes', 'Count'])
        
        # Write modules with used breaking changes
        for module_info in results['modules_with_used_breaking_changes']:
            writer.writerow([module_info['module'], 'Yes', module_info['count']])
        
        # Write modules without used breaking changes
        for module in results['modules_without_used_breaking_changes']:
            writer.writerow([module, 'No', 0])
        
        # Write modules with missing files
        for module in results['modules_with_missing_files']:
            writer.writerow([module, 'Missing_File', 'N/A'])
    
    print(f"\nDetailed results saved to: {output_file}")

def main():
    """Main function to run the analysis."""
    # Set up paths
    current_dir = Path(__file__).parent
    rq3_csv_path = current_dir / "data" / "rq3" / "csv"
    output_file = current_dir / "rq3_used_breaking_changes_analysis.csv"
    
    print("Starting RQ3 Used Breaking Changes Analysis...")
    print(f"Analyzing directory: {rq3_csv_path}")
    
    # Run the analysis
    results = analyze_rq3_results(rq3_csv_path)
    
    if not results:
        print("Analysis failed or no results found.")
        return
    
    # Print summary
    print_summary(results)
    
    # Save detailed results
    save_detailed_results(results, output_file)
    
    print(f"\nAnalysis complete!")

if __name__ == "__main__":
    main()