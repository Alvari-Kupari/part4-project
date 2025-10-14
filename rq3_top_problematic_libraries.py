import os
import pandas as pd

base_dir = "data/rq3/csv"
output_dir = "data/rq3/figures"
os.makedirs(output_dir, exist_ok=True)

def collect_csv_files(suffix):
    """Collect all CSVs with the given suffix from project folders."""
    files = []
    for project_name in os.listdir(base_dir):
        project_path = os.path.join(base_dir, project_name)
        if os.path.isdir(project_path):
            file_path = os.path.join(project_path, f"{project_name}-{suffix}.csv")
            if os.path.exists(file_path):
                files.append(file_path)
    return files

def summarize_breaking_changes(files, output_filename, filter_transitive=None):
    """Summarize breaking changes by library."""
    dfs = [pd.read_csv(f) for f in files]
    data = pd.concat(dfs, ignore_index=True)

    # Optional filter
    if filter_transitive is not None and "Is_Transitive" in data.columns:
        data = data[data["Is_Transitive"] == filter_transitive]

    # Aggregate
    counts = data["Library_Name"].value_counts().reset_index()
    counts.columns = ["Library_Name", "Count"]
    counts["Percentage"] = (counts["Count"] / counts["Count"].sum()) * 100

    # Print summary
    total = counts["Count"].sum()
    print(f"\n=== {output_filename.replace('.csv', '')} ===")
    print(f"Total breaking changes: {total}")

    for n in [5, 10]:
        topn = counts.head(n)
        topn_sum = topn["Count"].sum()
        pct = (topn_sum / total) * 100
        print(f"Top {n} contribute: {topn_sum} ({pct:.2f}%)")

    # Save results
    out_path = os.path.join(output_dir, output_filename)
    counts.to_csv(out_path, index=False)
    print(f"Saved → {out_path}")

# --- 1. Overall breaking changes ---
all_breaking_files = collect_csv_files("all-breaking-changes")

summarize_breaking_changes(all_breaking_files, "overall_breaking_changes_by_library.csv")
summarize_breaking_changes(all_breaking_files, "transitive_breaking_changes_by_library.csv", filter_transitive=True)

# --- 2. USED breaking changes ---
used_breaking_files = collect_csv_files("used-breaking-changes")

summarize_breaking_changes(used_breaking_files, "used_overall_breaking_changes_by_library.csv")
summarize_breaking_changes(used_breaking_files, "used_transitive_breaking_changes_by_library.csv", filter_transitive=True)

print("\n✅ All summaries generated successfully.")
