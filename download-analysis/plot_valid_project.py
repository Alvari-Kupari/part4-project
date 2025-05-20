import matplotlib.pyplot as plt
import re
from collections import Counter

# === CONFIG ===
LOG_FILE_PATH = "D:/repo-logs/filter-log-2025-05-20_22-46-34.822762300.txt" 

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

def plot_distribution(counts):
    distribution = Counter(counts)
    x = sorted(distribution)
    y = [distribution[v] for v in x]

    plt.figure(figsize=(10, 5))
    plt.bar(x, y)
    plt.xlabel("Valid Projects in Repo")
    plt.ylabel("Number of Repositories")
    plt.title("Distribution of Valid Project Counts per Repository")
    plt.grid(axis='y')
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    counts = extract_valid_project_counts(LOG_FILE_PATH)
    plot_distribution(counts)
