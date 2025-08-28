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
        subprocess.run(
            ["cmd", "/c", "mvn", "help:effective-pom", "-Doutput=effective-pom.xml"],
            cwd=submodule_path,
            check=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        print(f"Generated effective POM for {submodule_path}")
        return True
    except subprocess.CalledProcessError as e:
        print(f"Failed to generate effective POM for {submodule_path}")
        print(e.stderr)
        return False


def install_repo_root(repo_path):
    try:
        subprocess.run(
            ["cmd", "/c", "mvn", "install", "-DskipTests"],
            cwd=repo_path,
            check=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        print(f"✅ Installed repo at {repo_path}")
        return True
    except subprocess.CalledProcessError as e:
        print(f"❌ Failed to install repo at {repo_path}")
        print(e.stderr)
        return False



def process_repos(root_dir):
    repo_success = 0
    total_repos = 0

    for repo in Path(root_dir).iterdir():
        if repo.is_dir():
            total_repos += 1
            print(f"Processing repo: {repo.name}")
            success = install_repo_root(repo)
            if not success:
                print(f"❌ Skipping submodules for {repo.name} due to install failure.\n")
                continue

            repo_success += 1
            submodules = find_submodules(repo)

            success_count = 0
            for submodule in submodules:
                print(f"  Found submodule: {submodule}")
                if generate_effective_pom(submodule):
                    success_count += 1

            print(f"{success_count}/{len(submodules)} submodules in repo {repo.name} ran successfully\n")

    print(f"\n✅ {repo_success}/{total_repos} repos installed successfully")




if __name__ == "__main__":
    ROOT_DIR = r"C:\Users\tyin363\Documents\repos"  # Replace with actual path
    process_repos(ROOT_DIR)
