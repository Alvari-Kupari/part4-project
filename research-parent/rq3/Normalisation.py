import pandas as pd
import os

import pandas as pd

def _find_col(df, candidates):
    for c in candidates:
        if c in df.columns:
            return c
    lc = {col.lower(): col for col in df.columns}
    for c in candidates:
        if c.lower() in lc:
            return lc[c.lower()]
    return None

def _bool_series(df, col):
    if col is None or col not in df.columns:
        return pd.Series([False] * len(df), index=df.index)
    s = df[col].astype(str).str.strip().str.lower()
    return s.isin({"true", "1", "yes", "y", "t"})

def _filter_blacklist(df, lib_col, prefixes=None, exacts=None):
    if lib_col is None or lib_col not in df.columns:
        return df.copy()
    s = df[lib_col].astype(str).fillna("").str.strip()
    lower = s.str.lower()
    keep = pd.Series(True, index=df.index)
    if prefixes:
        for p in prefixes:
            keep &= ~lower.str.startswith(p)
    if exacts:
        keep &= ~lower.isin(exacts)
    # drop rows where library name is empty / obviously invalid
    keep &= ~lower.isin({"", "nan", "none"})
    return df[keep].copy()

def normalize(bcs_csv, symbols_csv):
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

    # --- debug print ---
    print(f"Direct: BCs={direct_bcs}, UniqueSyms={direct_symbols}")
    print(f"Trans:  BCs={trans_bcs}, UniqueSyms={trans_symbols}")

    # --- compute ratios ---
    norm_direct = direct_bcs / direct_symbols if direct_symbols else 0
    norm_trans = trans_bcs / trans_symbols if trans_symbols else 0

    return pd.DataFrame([{
        "submodule": (
            bcs["submodule"].iloc[0]
            if "submodule" in bcs.columns and not bcs.empty
            else None
        ),
        "normalised_direct_deps": norm_direct,
        "normalised_transitive_deps": norm_trans,
    }])




# --- main processing (unchanged structure) ---
results_folder = r"C:\Users\Alvari\Documents\UNI\softeng_700\test-results"
output_file = os.path.join(results_folder, "normalized_results.csv")

if not os.path.exists(output_file):
    pd.DataFrame(columns=["submodule", "normalised_direct_deps", "normalised_transitive_deps"]).to_csv(output_file, index=False)

for repo_result in os.listdir(results_folder):
    repo_path = os.path.join(results_folder, repo_result)
    if not os.path.isdir(repo_path):
        continue

    all_bcs_path = os.path.join(repo_path, f"{repo_result}-used-breaking-changes.csv")
    client_symbols_path = os.path.join(repo_path, f"{repo_result}-client-symbol-uses.csv")

    if not (os.path.exists(all_bcs_path) and os.path.exists(client_symbols_path)):
        continue

    bcs_csv = pd.read_csv(all_bcs_path, on_bad_lines='skip')
    symbols_csv = pd.read_csv(client_symbols_path, on_bad_lines='skip')

    results_csv = normalize(bcs_csv, symbols_csv)

    if not results_csv.empty:
        results_csv.to_csv(output_file, mode="a", header=False, index=False)
