import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
import re

# === CONFIG ===
LOG_FILE_PATH = "D:/repo-logs/repo-sizes-2025-05-20_22-46-34.853769800.txt"

def extract_repo_sizes(filepath):
    sizes = []
    with open(filepath, "r", encoding="utf-8") as f:
        for line in f:
            match = re.match(r"[^:]+: (\d+)", line.strip())
            if match:
                sizes.append(int(match.group(1))/1000)
    return sizes

def plot_repo_size_distribution(sizes):
    plt.figure(figsize=(10, 5))
    plt.hist(sizes, bins=50, edgecolor='black')
    plt.xlabel("Repository Size (MB)")
    plt.ylabel("Number of Repositories")
    plt.title("Distribution of Repository Sizes")
    plt.grid(axis='y')

    # Force plain number formatting on x-axis
    plt.gca().xaxis.set_major_formatter(ticker.FuncFormatter(lambda x, _: f"{int(x):,}"))

    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    sizes = extract_repo_sizes(LOG_FILE_PATH)
    plot_repo_size_distribution(sizes)
