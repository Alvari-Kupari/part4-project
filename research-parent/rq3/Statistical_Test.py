from pathlib import Path
import pandas as pd
import os
from scipy.stats import mannwhitneyu

# --- paths ---
base_folder = Path(__file__).parent.parent.parent / "data" / "rq3"
norm_folder = os.path.join(base_folder, "normalisation")

# --- load all results ---
print("Loading results...")
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


def run_test(data_list, labels, test_name):
    print(f"\nRunning Mann-Whitney U test ({test_name})")
    x, y = data_list
    stat, p = mannwhitneyu(x, y, alternative='two-sided')
    print(f"U statistic = {stat:.3f}, p-value = {p:.6f}")

    if p < 0.05:
        print(f"→ Significant difference between {labels[0]} and {labels[1]}")
    else:
        print(f"→ No significant difference between {labels[0]} and {labels[1]}")


# --- run statistical tests ---
run_test([direct_all.dropna(), trans_all.dropna()], ["Direct", "Transitive"], "all_including_zeros")
run_test([direct_nozero.dropna(), trans_nozero.dropna()], ["Direct", "Transitive"], "no_zeros")


print("\nAll tests generated successfully.")
