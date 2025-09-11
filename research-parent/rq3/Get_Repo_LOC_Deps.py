# loop through repos folder, get all submodules and then print the LOC and dep counts in the pom distribution
import os
from pathlib import Path
import csv

def get_submodule_loc(submodule_path):
    loc = 0
    for root, dirs, files in os.walk(submodule_path):
        for file in files:
            if file.endswith(".java"):
                file_path = os.path.join(root, file)
                try:
                    with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
                        loc += sum(1 for line in f if line.strip())
                except FileNotFoundError:
                    print(f"[WARN] File not found (skipped): {file_path}")
                except Exception as e:
                    print(f"[ERROR] Couldn't read {file_path}: {e}")
    return loc


def get_submodule_deps(submodule_path):
    deps = 0
    pom_file = os.path.join(submodule_path, "effective-pom.xml")
    try:
        if os.path.exists(pom_file):
            with open(pom_file, 'r', encoding='utf-8', errors='ignore') as f:
                for line in f:
                    if "<dependency>" in line:
                        deps += 1
    except Exception as e:
        print(f"[ERROR] Couldn't read POM file: {pom_file} - {e}")
    return deps



def find_submodules(repo_path):
    submodules = []
    for root, dirs, files in os.walk(repo_path):
        root_path = Path(root)
        if "effective-pom.xml" in files and (root_path / "src/main/java").is_dir():
            submodules.append(root_path)
    return submodules

def get_submodule_stats(submodule):
    loc = get_submodule_loc(submodule)
    deps = get_submodule_deps(submodule)
    return loc, deps


if __name__ == "__main__":
    repos_folder = Path(r"C:\Users\tyin363\Documents\repos")

    if not repos_folder.is_dir():
        print(f"The path {repos_folder} is not a valid directory.")
        exit(1)

    output_file = "submodule_stats.csv"

    # Open CSV once in write mode and add header
    with open(output_file, "w", newline="") as csvfile:
        csv_writer = csv.writer(csvfile)
        csv_writer.writerow(["Repo", "Submodule", "LOC", "Dependencies"])

        for repo in repos_folder.iterdir():
            if repo.is_dir():
                submodules = find_submodules(repo)

                if not submodules:
                    continue

                for submodule in submodules:
                    loc, deps = get_submodule_stats(submodule)
                    csv_writer.writerow([repo.name, submodule, loc, deps])

