import os
import csv
from pathlib import Path
import matplotlib.pyplot as plt
import numpy as np


def print_summary_stats(data, label):
    q1 = np.percentile(data, 25)
    q3 = np.percentile(data, 75)
    iqr = q3 - q1
    extreme_threshold = q3 + 3 * iqr
    outlier_threshold = q3 + 1.5 * iqr

    print(f"\n📊 {label} Summary Statistics")
    print(f"  Count             : {len(data)}")
    print(f"  Min               : {np.min(data)}")
    print(f"  LQ (Q1)           : {q1}")
    print(f"  Median            : {np.median(data)}")
    print(f"  UQ (Q3)           : {q3}")
    print(f"  Max               : {np.max(data)}")
    print(f"  Outlier Threshold : > {outlier_threshold:.2f} (Q3 + 1.5×IQR)")
    print(f"  Extreme Threshold : > {extreme_threshold:.2f} (Q3 + 3×IQR)")



def output_LOC_boxplot(csv_path):
    loc_data = []
    with open(csv_path, "r") as csvfile:
        csv_reader = csv.DictReader(csvfile)
        for row in csv_reader:
            try:
                loc_data.append(int(row["LOC"]))
            except ValueError:
                continue  # Skip invalid rows

    # Print stats to terminal
    print_summary_stats(loc_data, "LOC")

    # Plot boxplot
    plt.boxplot(loc_data)
    plt.title("LOC Distribution")
    plt.ylabel("Lines of Code (LOC)")
    plt.show()


def output_Deps_boxplot(csv_path):
    deps_data = []
    with open(csv_path, "r") as csvfile:
        csv_reader = csv.DictReader(csvfile)
        for row in csv_reader:
            try:
                deps_data.append(int(row["Dependencies"]))
            except ValueError:
                continue  # Skip invalid rows

    # Print stats to terminal
    print_summary_stats(deps_data, "Dependencies")

    # Plot boxplot
    plt.boxplot(deps_data)
    plt.title("Dependencies Distribution")
    plt.ylabel("Number of Dependencies")
    plt.show()


if __name__ == "__main__":
    csv_path = Path(r"C:\Users\tyin363\Documents\part4-project\research-parent\rq3\submodule_stats.csv")

    if not csv_path.exists():
        print(f"[ERROR] CSV file not found: {csv_path}")
        exit(1)

    output_LOC_boxplot(csv_path)
    output_Deps_boxplot(csv_path)
