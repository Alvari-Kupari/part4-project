import os
import re
from collections import defaultdict

input_dir = "/Users/tonyyin/Desktop/DependencyTrees"
summary_output_path = os.path.join(input_dir, "transitive_conflict_summary_structured.txt")

# Adjusted regex to handle scope and parentheses
conflict_line_re = re.compile(
    r"\(([^:]+):([^:]+):[^:]+:([^:]+):[^ ]+ - omitted for conflict with ([^)]+)\)"
)

def check_conflicts_in_file(file_path):
    conflict_map = defaultdict(lambda: {"used": None, "omitted": set()})

    with open(file_path, "r") as f:
        for line in f:
            if "omitted for conflict with" in line:
                match = conflict_line_re.search(line)
                if match:
                    group_id, artifact_id, omitted_version, used_version = match.groups()
                    key = f"{group_id}:{artifact_id}"
                    conflict_map[key]["used"] = used_version
                    conflict_map[key]["omitted"].add(omitted_version)
    return conflict_map

def main():
    total_conflicts = 0
    summary_lines = []

    for filename in os.listdir(input_dir):
        if filename.endswith("_dependency_tree.txt"):
            file_path = os.path.join(input_dir, filename)
            conflicts = check_conflicts_in_file(file_path)

            if conflicts:
                summary_lines.append(f"{filename}")
                for dep, versions in conflicts.items():
                    used = versions["used"]
                    omitted = ", ".join(sorted(versions["omitted"]))
                    summary_lines.append(f"{dep} → {used}")
                    for v in sorted(versions["omitted"]):
                        summary_lines.append(f"  - {v} (omitted)")
                    total_conflicts += len(versions["omitted"])
                summary_lines.append("")

    if total_conflicts > 0:
        print(f"\n=== Total Transitive Dependency Conflicts: {total_conflicts} ===")
        print("\n".join(summary_lines))

        with open(summary_output_path, "w") as summary_file:
            summary_file.write("\n".join(summary_lines))
        print(f"\nSummary saved to {summary_output_path}")
    else:
        print("No transitive dependency conflicts found in any files.")

if __name__ == "__main__":
    main()