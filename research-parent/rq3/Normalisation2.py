import pandas as pd
import os

def normalize(bcs_csv, symbols_csv):
    bcs = pd.read_csv(bcs_csv, on_bad_lines='skip')
    syms = pd.read_csv(symbols_csv, on_bad_lines='skip')

    bcs.columns = bcs.columns.str.strip()
    syms.columns = syms.columns.str.strip()

    def clean_df(df):
        mask = ~df["Library_Name"].str.contains("jdk|client", case=False, na=False)
        return df[mask]

    bcs = clean_df(bcs)
    syms = clean_df(syms)

    # required_syms = {"Library_Name", "Class_Name", "Symbol_Name", "Symbol_Type", "Is_Transitive"}
    # required_bcs = {"Library_Name", "Class_Name", "Member_Name", "Usage_Type", "Is_Transitive"}

    # missing_syms = required_syms - set(syms.columns)
    # missing_bcs = required_bcs - set(bcs.columns)

    # if missing_syms:
    #     print(f"  Skipping: missing symbol columns: {missing_syms}")
    #     return pd.DataFrame()
    # if missing_bcs:
    #     print(f"  Skipping: missing BC columns: {missing_bcs}")
    #     return pd.DataFrame()

    syms["key"] = syms["Library_Name"] + "|" + syms["Class_Name"] + "|" + syms["Symbol_Name"] + "|" + syms["Symbol_Type"]
    bcs["key"] = bcs["Library_Name"] + "|" + bcs["Class_Name"] + "|" + bcs["Member_Name"] + "|" + bcs["Usage_Type"]

    bc_counts = bcs.groupby(["Library_Name", "Is_Transitive"])["key"].count().reset_index(name="num_bcs")
    symbol_counts = syms.groupby(["Library_Name", "Is_Transitive"])["key"].nunique().reset_index(name="num_unique_symbols")

    merged = pd.merge(bc_counts, symbol_counts, on=["Library_Name", "Is_Transitive"], how="outer").fillna(0)
    merged["normalised_score"] = merged.apply(
        lambda r: r["num_bcs"] / r["num_unique_symbols"] if r["num_unique_symbols"] else 0,
        axis=1
    )

    return merged[["Library_Name", "Is_Transitive", "normalised_score", "num_bcs", "num_unique_symbols"]]



if __name__ == "__main__":
    base_dir = r"C:\Users\tyin363\Documents\part4-project\data\rq3\csv"
    out_dir = os.path.join(r"C:\Users\tyin363\Documents\part4-project\data\rq3", "normalisation")
    os.makedirs(out_dir, exist_ok=True)

    for submodule in os.listdir(base_dir):
        repo_path = os.path.join(base_dir, submodule)
        if not os.path.isdir(repo_path):
            continue

        print(f"Processing {submodule}...")

        bcs_path = os.path.join(repo_path, f"{submodule}-used-breaking-changes.csv")
        syms_path = os.path.join(repo_path, f"{submodule}-client-symbol-uses.csv")

        if not (os.path.exists(bcs_path) and os.path.exists(syms_path)):
            print(f"  Skipping {submodule}: missing input files")
            continue

        result = normalize(bcs_path, syms_path)
        if result.empty:
            print(f"  No valid data for {submodule}")
            continue

        out_path = os.path.join(out_dir, f"{submodule}-normalized.csv")
        result.to_csv(out_path, index=False)
        print(f"  Saved: {out_path}")
