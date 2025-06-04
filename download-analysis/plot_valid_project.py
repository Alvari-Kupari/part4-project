import matplotlib.pyplot as plt
import re
from collections import Counter
import numpy as np

# === CONFIG ===
LOG_FILE_PATH = r"C:\Users\akup390\Documents\tony-alvari-part4\part4-project\data\download_analysis\filter-log-2025-05-20_22-46-34.822762300.txt"

def extract_valid_project_counts(filepath):
    counts = []
    in_accepted_section = False

    with open(filepath, "r", encoding="utf-8") as f:
        for line in f:
            if line.strip().startswith("Accepted repos"):
                in_accepted_section = True
                continue
            if in_accepted_section:
                match = re.match(r"- .+: (\d+)", line.strip())
                if match:
                    counts.append(int(match.group(1)))
    return counts

def plot_boxplot(counts):
    upper_quartile = np.percentile(counts, 75)
    print("Upper Quartile (Q3):", upper_quartile)

    plt.figure(figsize=(8, 5))
    plt.boxplot(counts, vert=True)
    plt.ylabel("Valid Projects in Repo")
    plt.title("Boxplot of Valid Project Counts per Repository")
    plt.grid(axis='y')
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    counts = extract_valid_project_counts(LOG_FILE_PATH)
    plot_boxplot(counts)
