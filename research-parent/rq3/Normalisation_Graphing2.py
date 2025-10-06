import pandas as pd
import os
import matplotlib.pyplot as plt

# --- paths ---
base_folder = r"C:\Users\tyin363\Documents\part4-project\data\rq3"
norm_folder = os.path.join(base_folder, "normalisation")
figures_folder = os.path.join(base_folder, "figures")

os.makedirs(figures_folder, exist_ok=True)

# --- load all results ---
all_results = []
for file in os.listdir(norm_folder):
    if file.endswith(".csv"):
        path = os.path.join(norm_folder, file)
        try:
            df = pd.read_csv(path)
            df["source_file"] = file
            all_results.append(df)
        except Exception as e:
            print(f"Skipping {file}: {e}")

if not all_results:
    print("No normalisation CSVs found.")
    exit()

data = pd.concat(all_results, ignore_index=True)

# --- split direct / transitive (all) ---
direct_all = data[data["Is_Transitive"] == False]["normalised_score"]
trans_all = data[data["Is_Transitive"] == True]["normalised_score"]

# --- split direct / transitive (no zeros) ---
direct_nozero = direct_all[direct_all != 0]
trans_nozero = trans_all[trans_all != 0]

# --- print key stats ---
def describe(label, series):
    desc = series.describe(percentiles=[.25, .5, .75])
    print(f"\n{label} normalised scores:")
    print(desc)
    print(f"Median: {desc['50%']:.3f}, Mean: {series.mean():.3f}, Std: {series.std():.3f}")

print("=== Including zeros ===")
describe("Direct (all)", direct_all)
describe("Transitive (all)", trans_all)

print("\n=== Excluding zeros ===")
describe("Direct (no zeros)", direct_nozero)
describe("Transitive (no zeros)", trans_nozero)

# --- clip outliers (> 3) ---
direct_clipped = direct_nozero[direct_nozero <= 3]
trans_clipped = trans_nozero[trans_nozero <= 3]

# --- plot boxplots ---
fig, axes = plt.subplots(3, 1, figsize=(8, 14), sharex=True)

# Plot 1: all data
axes[0].boxplot(
    [direct_all.dropna(), trans_all.dropna()],
    labels=["Direct", "Transitive"],
    patch_artist=True,
    boxprops=dict(facecolor="lightblue"),
    medianprops=dict(color="red")
)
axes[0].set_title("Including Zero Values")
axes[0].set_ylabel("Normalised Score")
axes[0].grid(axis="y", linestyle="--", alpha=0.7)

# Plot 2: filtered (no zeros)
axes[1].boxplot(
    [direct_nozero.dropna(), trans_nozero.dropna()],
    labels=["Direct", "Transitive"],
    patch_artist=True,
    boxprops=dict(facecolor="lightgreen"),
    medianprops=dict(color="red")
)
axes[1].set_title("Excluding Zero Values")
axes[1].set_ylabel("Normalised Score")
axes[1].grid(axis="y", linestyle="--", alpha=0.7)

# ✅ Plot 3: clipped (no zeros, no outliers >3)
axes[2].boxplot(
    [direct_clipped.dropna(), trans_clipped.dropna()],
    labels=["Direct", "Transitive"],
    patch_artist=True,
    boxprops=dict(facecolor="lightcoral"),
    medianprops=dict(color="red")
)
axes[2].set_title("Excluding Zero Values & Outliers (Normalised Score ≤ 3)")
axes[2].set_ylabel("Normalised Score")
axes[2].grid(axis="y", linestyle="--", alpha=0.7)

fig.suptitle("Distribution of Normalised Scores: Direct vs Transitive", fontsize=14)
fig.tight_layout(rect=[0, 0, 1, 0.96])

# --- save figure ---
fig_path = os.path.join(figures_folder, "normalised_boxplots_with_clipped.png")
plt.savefig(fig_path, dpi=300, bbox_inches="tight")
print(f"\nSaved figure to: {fig_path}")
