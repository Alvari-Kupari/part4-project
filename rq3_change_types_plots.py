import os
import pandas as pd
import matplotlib.pyplot as plt

base_dir = "data/rq3/csv"

# Collect used-breaking-changes CSVs
used_breaking_files = []
for project_name in os.listdir(base_dir):
    project_path = os.path.join(base_dir, project_name)
    if os.path.isdir(project_path):
        used_file = os.path.join(project_path, f"{project_name}-used-breaking-changes.csv")
        if os.path.exists(used_file):
            used_breaking_files.append(used_file)

# Combine all data
dfs = [pd.read_csv(f) for f in used_breaking_files]
data = pd.concat(dfs, ignore_index=True)

def make_pie_chart(labels, filename, title):
    counts = labels.value_counts()
    total = counts.sum()
    colors = plt.cm.tab20.colors[:len(counts)]

    # Print slice info
    print(f"\n=== {title} ===")
    for label, count in counts.items():
        pct = (count / total) * 100
        print(f"{label}: {count} ({pct:.2f}%)")

    fig, ax = plt.subplots(figsize=(8, 8))
    wedges, _ = ax.pie(counts, labels=None, startangle=90, colors=colors)

    ax.legend(
        wedges,
        [f"{label} ({count}, {count/total:.1%})" for label, count in zip(counts.index, counts)],
        title="Category",
        loc="center left",
        bbox_to_anchor=(1, 0, 0.5, 1),
        fontsize=9,
    )
    fig.subplots_adjust(left=0.1, right=0.65)
    plt.tight_layout()
    plt.savefig(filename, bbox_inches="tight")
    plt.close()

# Pie 1: Change_Type
make_pie_chart(
    data["Change_Type"],
    "used_breaking_changes_by_change_type.pdf",
    "Used Breaking Changes by Change Type"
)

# Pie 2: Description (group rare ones as 'Other')
desc_counts = data["Description"].value_counts()
threshold = 0.02 * len(data)
common_descs = desc_counts[desc_counts >= threshold].index
data["Desc_Grouped"] = data["Description"].where(data["Description"].isin(common_descs), "Other")

make_pie_chart(
    data["Desc_Grouped"],
    "used_breaking_changes_by_description.pdf",
    "Used Breaking Changes by Description"
)

print("\n✅ Generated: used_breaking_changes_by_change_type.pdf and used_breaking_changes_by_description.pdf")
