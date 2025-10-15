#!/usr/bin/env python3
"""
RQ1 Simple Depth Analysis: Bar Chart of Conflicts vs Depth

This script creates a simple bar chart showing the number of conflicts
at each dependency depth to answer: Do more conflicts happen at lower depths?

Author: Research Team
Date: October 2025
"""

import pandas as pd
import matplotlib.pyplot as plt
import glob
import os
from pathlib import Path
from collections import Counter

def load_and_analyze_conflicts(csv_folder_path):
    """
    Load all CSV files and analyze conflict depths.
    
    Args:
        csv_folder_path (str): Path to folder containing CSV files
        
    Returns:
        dict: Depth counts for conflicts
    """
    print("Loading CSV files...")
    csv_files = glob.glob(str(Path(csv_folder_path) / "*_version_conflicts.csv"))
    
    if not csv_files:
        raise FileNotFoundError(f"No CSV files found in {csv_folder_path}")
    
    print(f"Found {len(csv_files)} CSV files")
    
    all_depths = []
    total_conflicts = 0
    
    for csv_file in csv_files:
        try:
            df = pd.read_csv(csv_file)
            # Add both selected and conflicting depths to get complete picture
            all_depths.extend(df['depth_selected'].tolist())
            all_depths.extend(df['depth_conflicting'].tolist())
            total_conflicts += len(df)
        except Exception as e:
            print(f"Error reading {csv_file}: {e}")
            continue
    
    print(f"Loaded {total_conflicts} conflict records from {len(csv_files)} projects")
    
    # Count occurrences at each depth
    depth_counts = Counter(all_depths)
    
    return depth_counts, total_conflicts

def create_depth_bar_chart(depth_counts, total_conflicts, output_file="rq1_conflicts_by_depth.pdf"):
    """
    Create a bar chart showing conflicts by depth.
    
    Args:
        depth_counts (Counter): Count of conflicts at each depth
        total_conflicts (int): Total number of conflicts
        output_file (str): Output filename
    """
    # Prepare data - filter out any NaN values
    valid_depths = {k: v for k, v in depth_counts.items() if pd.notna(k)}
    depths = sorted(valid_depths.keys())
    counts = [valid_depths[depth] for depth in depths]
    
    if not depths:
        print("No valid depth data found!")
        return
    
    # Create the plot
    plt.figure(figsize=(12, 8))
    
    # Create bar chart
    bars = plt.bar(depths, counts, color='steelblue', alpha=0.7, edgecolor='black', linewidth=0.5)
    
    # Customize the plot
    # No title, larger labels and tick sizes
    plt.xlabel('Dependency Depth', fontsize=18)
    plt.ylabel('Number of Conflicts', fontsize=18)
    plt.xticks(fontsize=16)
    plt.yticks(fontsize=16)

    
    # Add value labels on top of bars 
    max_count = max(counts)
    for bar, count in zip(bars, counts):
        height = bar.get_height()
        # Show labels for all bars with count > 0, but use smaller font for very small bars
        if count > 0:
            font_size = 9 if height > max_count * 0.02 else 7
            plt.text(bar.get_x() + bar.get_width()/2., height + max_count*0.005,
                    f'{count}', ha='center', va='bottom', fontsize=font_size)
    
    # Add grid for easier reading
    plt.grid(True, alpha=0.6, axis='y', color='gray')
    
    # Add statistics text box
    peak_depth = depths[counts.index(max(counts))]
    stats_text = f'Total Conflicts: {total_conflicts:,}\n'
    stats_text += f'Depth Range: {min(depths)} - {max(depths)}\n'
    stats_text += f'Most Conflicts at Depth: {peak_depth}\n'
    stats_text += f'Peak Conflicts: {max(counts):,}'
    
    # plt.text(0.02, 0.98, stats_text, transform=plt.gca().transAxes, 
    #          fontsize=10, verticalalignment='top',
    #          bbox=dict(boxstyle='round', facecolor='wheat', alpha=0.8))
    # Print stats instead of showing on chart
    print("\n" + stats_text)

    
    # Set x-axis to show all depths clearly
    plt.xticks(depths, [int(d + 1) for d in depths])

    
    # If there are many depths, rotate labels for better readability
    if len(depths) > 15:
        plt.xticks(rotation=45)
    
    # Adjust layout and save
    plt.tight_layout()
    plt.savefig(output_file, dpi=300, bbox_inches='tight')
    plt.show()
    
    print(f"Bar chart saved to: {output_file}")
    
    # Print summary statistics
    print(f"\nSummary Statistics:")
    print(f"- Total conflicts: {total_conflicts:,}")
    print(f"- Depth range: {min(depths)} to {max(depths)}")
    print(f"- Most conflicts at depth {peak_depth}: {max(counts):,} conflicts")
    
    # Calculate percentages for common depths
    total_valid_conflicts = sum(counts)
    if 0 in valid_depths:
        print(f"- Percentage of conflicts at depth 0: {valid_depths[0]/total_valid_conflicts*100:.1f}%")
    if 1 in valid_depths and 2 in valid_depths:
        depth_1_2 = valid_depths.get(1, 0) + valid_depths.get(2, 0)
        print(f"- Percentage of conflicts at depths 1-2: {depth_1_2/total_valid_conflicts*100:.1f}%")

def main():
    """
    Main execution function.
    """
    # Set up path to CSV folder
    csv_folder = Path(__file__).parent / "data" / "rq1" / "csv"
    
    print("RQ1: Analyzing Conflict Depth Patterns")
    print("=" * 40)
    
    # Load and analyze data
    depth_counts, total_conflicts = load_and_analyze_conflicts(csv_folder)
    
    # Create visualization
    create_depth_bar_chart(depth_counts, total_conflicts)
    
    print("\nAnalysis complete!")

if __name__ == "__main__":
    main()