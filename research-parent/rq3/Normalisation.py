import pandas as pd
import os


def normalize(bcs_csv, symbols_csv):
    # TODO: implement actual normalization logic
    print()
    # return a DataFrame with columns: submodule, normalised_direct_deps, normalised_transitive_deps
    return pd.DataFrame(columns=["submodule", "normalised_direct_deps", "normalised_transitive_deps"])


results_folder = r"C:\Users\Alvari\Documents\UNI\softeng_700\test-results"
output_file = os.path.join(results_folder, "normalized_results.csv")

# create output file with header if it doesn't exist
if not os.path.exists(output_file):
    pd.DataFrame(columns=["submodule", "normalised_direct_deps", "normalised_transitive_deps"]).to_csv(output_file, index=False)

# open csv files in each repo folder
for repo_result in os.listdir(results_folder):
    repo_path = os.path.join(results_folder, repo_result)
    if not os.path.isdir(repo_path):
        continue

    all_bcs_path = os.path.join(repo_path, f"{repo_result}-used-breaking-changes.csv")
    client_symbols_path = os.path.join(repo_path, f"{repo_result}-client-symbol-uses.csv")

    if not (os.path.exists(all_bcs_path) and os.path.exists(client_symbols_path)):
        continue

    bcs_csv = pd.read_csv(all_bcs_path)
    symbols_csv = pd.read_csv(client_symbols_path)

    results_csv = normalize(bcs_csv, symbols_csv)

    # append result to csv
    if not results_csv.empty:
        results_csv.to_csv(output_file, mode="a", header=False, index=False)
