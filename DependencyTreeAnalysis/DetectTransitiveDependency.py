import os
import re
import csv
from collections import defaultdict

input_dir = "/Users/tonyyin/Desktop/DependencyTrees"

# Matches lines like "(group:artifact:...:version - omitted for conflict with usedVersion)"
conflict_re = re.compile(
    r"\(([^:]+):([^:]+):[^:]*:([^:]+):[^ ]+ - omitted for conflict with ([^)]+)\)"
)

# Matches non‑conflict lines where Maven prints a dependency
used_version_re = re.compile(
    r"^\s*[+\-\\| ]*([^:]+):([^:]+):[^:]*:([^:]+):"
)

def depth_of(line: str) -> int:
    """Count all '|' in the line, and add 1."""
    return line.count('|') + 1

def parse_file_with_depth(file_path: str):
    # Map "group:artifact:version" → depth
    used_versions = {}
    conflicts = []

    with open(file_path, "r") as f:
        lines = f.readlines()

    # First pass: collect all used versions with their depths
    for line in lines:
        d = depth_of(line)
        m_used = used_version_re.search(line)
        if m_used and "omitted for" not in line:
            g, a, v = m_used.groups()
            used_versions[f"{g}:{a}:{v}"] = d

    # Second pass: process conflicts
    for line in lines:
        d = depth_of(line)
        m_conf = conflict_re.search(line)
        if m_conf:
            g, a, omitted_v, used_v = m_conf.groups()
            lib = f"{g}:{a}"
            used_key = f"{g}:{a}:{used_v}"
            depth_used = used_versions.get(used_key)  # Look up the actual depth
            if depth_used is not None:  # Only add if the used version was found
                depth_omitted = d
                conflicts.append([
                    lib,
                    used_v,
                    omitted_v,
                    depth_used,
                    depth_omitted
                ])

    # Sort conflicts by library name
    conflicts.sort(key=lambda x: x[0])
    return conflicts

def main():
    for fn in os.listdir(input_dir):
        if fn.endswith("_dependency_tree.txt"):
            path = os.path.join(input_dir, fn)
            conflicts = parse_file_with_depth(path)

            # Generate a CSV file for each dependency tree
            csv_output_path = os.path.join(input_dir, f"{os.path.splitext(fn)[0]}_conflicts.csv")
            with open(csv_output_path, "w", newline="") as csvfile:
                w = csv.writer(csvfile)
                w.writerow([
                    "Library name",
                    "Version selected",
                    "Conflicting version",
                    "Depth of version selected",
                    "Depth of conflicting version"
                ])
                w.writerows(conflicts)

            print(f"CSV summary saved to {csv_output_path}")

if __name__ == "__main__":
    main()