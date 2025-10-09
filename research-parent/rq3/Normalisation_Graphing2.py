from pathlib import Path
import pandas as pd
import os
import matplotlib.pyplot as plt

# --- paths ---
base_folder = Path(__file__).parent.parent.parent / "data" / "rq3"
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

# --- function for consistent boxplot styling ---
def make_boxplot(data_list, labels, output_name):
    plt.figure(figsize=(8, 6))
    plt.boxplot(
        data_list,
        labels=labels,
        patch_artist=True,
        boxprops=dict(facecolor="lightblue", alpha=0.9),
        medianprops=dict(color="red", linewidth=2),
        flierprops=dict(marker='o', markersize=4, alpha=0.9),
        widths=0.4
    )
    plt.xlabel("Type", fontsize=14)
    plt.ylabel("Normalised Score", fontsize=14)
    plt.xticks(fontsize=12)
    plt.yticks(fontsize=12)
    plt.grid(True, alpha=0.6)
    plt.tight_layout()

    output_path = os.path.join(figures_folder, f"{output_name}.pdf")
    plt.savefig(output_path, dpi=300, bbox_inches="tight")
    plt.close()
    print(f"Saved: {output_path}")

# --- generate individual plots ---
make_boxplot([direct_all.dropna(), trans_all.dropna()], ["Direct", "Transitive"], "boxplot_all_including_zeros")
make_boxplot([direct_nozero.dropna(), trans_nozero.dropna()], ["Direct", "Transitive"], "boxplot_no_zeros")
make_boxplot([direct_clipped.dropna(), trans_clipped.dropna()], ["Direct", "Transitive"], "boxplot_no_zeros_clipped")

print("\nAll boxplots generated successfully.")
