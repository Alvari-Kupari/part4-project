import os
from pathlib import Path
import subprocess

def find_submodules(repo_path):
    submodules = []
    for root, dirs, files in os.walk(repo_path):
        root_path = Path(root)
        if "pom.xml" in files and (root_path / "src/main/java").is_dir():
            submodules.append(root_path)
    return submodules

def generate_effective_pom(submodule_path):
    try:
        result = subprocess.run(
            ["mvn", "help:effective-pom", "-Doutput=effective-pom.xml"],
            cwd=submodule_path,
            check=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        print(f"Generated effective POM for {submodule_path}")
    except subprocess.CalledProcessError as e:
        print(f"Failed to generate effective POM for {submodule_path}")
        print(e.stderr)

def install_repo_root(repo_path):
    try:
        result = subprocess.run(
            ["mvn", "install", "-DskipTests"],
            cwd=repo_path,
            check=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        print(f"Installed repo at {repo_path}")
    except subprocess.CalledProcessError as e:
        print(f"Failed to install repo at {repo_path}")
        print(e.stderr)


def process_repos(root_dir):
    for repo in Path(root_dir).iterdir():
        if repo.is_dir():
            print(f"Processing repo: {repo.name}")
            install_repo_root(repo)
            submodules = find_submodules(repo)
            for submodule in submodules:
                print(f"  Found submodule: {submodule}")
                generate_effective_pom(submodule)


if __name__ == "__main__":
    ROOT_DIR = "path/to/repos"  # Replace with actual path
    process_repos(ROOT_DIR)
