import pandas as pd
import os

import pandas as pd



def normalize(bcs_csv, symbols_csv, submodule):
    # --- clean irrelevant rows ---
    def clean_df(df):
        mask = ~df["Library_Name"].str.contains("jdk|client", case=False, na=False)
        return df[mask]

    bcs = clean_df(bcs_csv.copy())
    syms = clean_df(symbols_csv.copy())

    # --- compute numerators (keep all rows) ---
    direct_bcs = bcs[~bcs["Is_Transitive"]].shape[0]
    trans_bcs = bcs[bcs["Is_Transitive"]].shape[0]

    # --- compute denominators (unique symbols by Class_Name, Symbol_Name, Symbol_Type) ---
    direct_symbols = (
        syms[~syms["Is_Transitive"]][["Class_Name", "Symbol_Name", "Symbol_Type"]]
        .drop_duplicates()
        .shape[0]
    )
    trans_symbols = (
        syms[syms["Is_Transitive"]][["Class_Name", "Symbol_Name", "Symbol_Type"]]
        .drop_duplicates()
        .shape[0]
    )


    # --- compute ratios ---
    norm_direct = direct_bcs / direct_symbols if direct_symbols else 0
    norm_trans = trans_bcs / trans_symbols if trans_symbols else 0

    return pd.DataFrame([{
        "submodule": submodule,
        "normalised_direct_deps": norm_direct,
        "normalised_transitive_deps": norm_trans,
    }])




# --- main processing (unchanged structure) ---
results_folder = r"C:\Users\Alvari\Documents\part4-project\data\rq3\csv"
output_file = os.path.join(results_folder, "normalized_results.csv")

if not os.path.exists(output_file):
    pd.DataFrame(columns=["submodule", "normalised_direct_deps", "normalised_transitive_deps"]).to_csv(output_file, index=False)

for submodule_result in os.listdir(results_folder):
    repo_path = os.path.join(results_folder, submodule_result)
    if not os.path.isdir(repo_path):
        continue

    all_bcs_path = os.path.join(repo_path, f"{submodule_result}-used-breaking-changes.csv")
    client_symbols_path = os.path.join(repo_path, f"{submodule_result}-client-symbol-uses.csv")

    if not (os.path.exists(all_bcs_path) and os.path.exists(client_symbols_path)):
        continue

    bcs_csv = pd.read_csv(all_bcs_path, on_bad_lines='skip')
    symbols_csv = pd.read_csv(client_symbols_path, on_bad_lines='skip')

    results_csv = normalize(bcs_csv, symbols_csv, submodule_result)

    if not results_csv.empty:
        results_csv.to_csv(output_file, mode="a", header=False, index=False)
