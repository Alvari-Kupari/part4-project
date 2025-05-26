import os
import subprocess
from get_valid_projects import find_valid_projects

def generate_dot_file(repo_name, repo_path, dot_output_location, error_log_path):
    print(f"Generating dot file for repo: {repo_name}")

    projects = find_valid_projects(repo_path)
    successful_dots = []

    for i, project_path in enumerate(projects):
        module_name = os.path.basename(project_path)
        temp_dot = os.path.join(dot_output_location, f"{module_name}_temp_{i}.dot")

        success = run_maven_dependency_tree(project_path, temp_dot)
        if success:
            print(f"✔ Module succeeded: {module_name}")
            successful_dots.append((temp_dot, module_name))
        else:
            print(f"✖ Module failed: {module_name}")
            if os.path.exists(temp_dot):
                os.remove(temp_dot)

    if successful_dots:
        combined_path = os.path.join(dot_output_location, f"{repo_name}.dot")
        combine_dot_files(successful_dots, combined_path)
        print(f"Combined dot saved for repo: {repo_name}")
    else:
        print(f"No successful modules for repo: {repo_name}")
        with open(error_log_path, 'a') as log:
            log.write(f"{repo_name}: all modules failed\n")

    # Cleanup temp files
    for temp_file, _ in successful_dots:
        if os.path.exists(temp_file):
            os.remove(temp_file)

def run_maven_dependency_tree(project_path, output_file):
    cmd = [
        "cmd", "/c", "mvn", "dependency:tree",
        "-Dverbose=true", "-DoutputType=dot", f"-DoutputFile={output_file}"
    ]
    result = subprocess.run(cmd, cwd=project_path, capture_output=True, text=True)

    if result.returncode != 0:
        print(f"Error running dependency:tree at {project_path}")
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
repos_dir = "D:/failed_repos"
dot_output_location = "C:/Users/Alvari/Documents/UNI/softeng_700/part4-project/data/failed_repos"
error_log_path = os.path.join(dot_output_location, "failed_repos.log")
os.makedirs(dot_output_location, exist_ok=True)

for repo in os.listdir(repos_dir):
    repo_path = os.path.join(repos_dir, repo)
    generate_dot_file(repo, repo_path, dot_output_location, error_log_path)
