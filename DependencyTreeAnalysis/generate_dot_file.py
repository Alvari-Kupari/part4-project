import os
import subprocess
from get_valid_projects import find_valid_projects



def generate_dot_file(repo_name, repo_path, dot_output_location):
    print(f"Generating dot file for repo: {repo_name}")
    run_maven_dependency_tree(repo_name, repo_path, dot_output_location)
    #projects = find_valid_projects(repo_path)

  #  for project in projects:



def run_maven_dependency_tree(repo_name, repo_path, dot_output_location):
    output_location = os.path.join(dot_output_location, f"{repo_name}.dot")
    cmd = ["cmd", "/c", "mvn", "dependency:tree", "-Dverbose=true", "-DoutputType=dot", f"-DoutputFile={output_location}"]

    result = subprocess.run(cmd, cwd=repo_path, capture_output=True, text=True)

    if result.returncode == 0:

        print(f"Dot saved for repo: {repo_name}")

    else:
        print(f"Error for repo: {repo_name}:")
        print(result.stderr)


#script
repos_dir = "D:/standalone_repos"
dot_output_location = "C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\data\dot_files"

for repo in os.listdir(repos_dir):
    repo_path = os.path.join(repos_dir, repo)
    generate_dot_file(repo, repo_path, dot_output_location)