import os
import subprocess
from get_valid_projects import find_valid_projects

def generate_dot_file(repo_name, repo_path, dot_output_location, error_log_path):
    print(f"Generating dot file for repo: {repo_name}")

    projects = find_valid_projects(repo_path)
    temp_dot_files = []

    for i, project_path in enumerate(projects):
        module_name = os.path.basename(project_path)
        temp_dot = os.path.join(dot_output_location, f"{module_name}_temp_{i}.dot")

        success = run_maven_dependency_tree(project_path, temp_dot)
        if success:
            temp_dot_files.append((temp_dot, module_name))
        else:
            print(f"Skipping module {module_name} due to failure.")
            with open(error_log_path, 'a') as log:
                log.write(f"{repo_name}/{module_name}: Maven failed\n")

    if temp_dot_files:
        combined_path = os.path.join(dot_output_location, f"{repo_name}.dot")
        combine_dot_files(temp_dot_files, combined_path)
        print(f"Combined dot saved for repo: {repo_name}")
    else:
        print(f"No successful modules for repo: {repo_name}")
        with open(error_log_path, 'a') as log:
            log.write(f"{repo_name}: All modules failed\n")

    for temp_file, _ in temp_dot_files:
        if os.path.exists(temp_file):
            os.remove(temp_file)


def run_maven_dependency_tree(project_path, output_file):
    cmd = [
        "cmd", "/c", "mvn", "dependency:tree",
        "-Dverbose=true", "-DoutputType=dot", f"-DoutputFile={output_file}"
    ]
    result = subprocess.run(cmd, cwd=project_path, capture_output=True, text=True)

    if result.returncode != 0:
        print(f"Error for project at {project_path}:")
        print(result.stderr)
        return False
    return True

def combine_dot_files(input_entries, output_path):
    with open(output_path, 'w') as outfile:
        for path, module_name in input_entries:
            with open(path, 'r') as infile:
                for line in infile:
                    if line.strip().startswith("digraph"):
                        outfile.write(f"digraph {module_name} {{\n")
                    elif line.strip().startswith("graph"):
                        outfile.write(f"graph {module_name} {{\n")
                    elif line.strip() == "}":
                        outfile.write("}\n")
                    else:
                        outfile.write(line)

# Script
repos_dir = r"C:\Users\akup390\Documents\repos"
dot_output_location = r"C:\Users\akup390\Documents\dot_files"
error_log_path = os.path.join(dot_output_location, "failed_repos.log")
os.makedirs(dot_output_location, exist_ok=True)

repos = sorted(
    os.listdir(repos_dir),
    key=lambda r: os.path.getctime(os.path.join(repos_dir, r))
)

start_from = "dougdonohoe__ddpoker"
start_index = repos.index(start_from) + 1

for repo in repos[start_index:]:
    repo_path = os.path.join(repos_dir, repo)
    generate_dot_file(repo, repo_path, dot_output_location, error_log_path)

