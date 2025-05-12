import os
import subprocess

repos_dir = "C:\\Users\\Alvari\\Documents\\UNI\\archive\\2023 SEM 1\\SOFTENG_281\\SE281_GitRepo\\Assignment-3"


def run_maven_dependency_tree(repo_path):
    cmd = ["mvn.cmd", "dependency:tree", "-Dverbose=true", "-DoutputType=text"]
    result = subprocess.run(cmd, cwd=repo_path, capture_output=True, text=True)

    if result.returncode == 0:
        lines = result.stdout.splitlines()
        tree_lines = []
        capture = False
        for line in lines:
            if line.strip().startswith("[INFO] --- dependency:"):
                capture = True
                continue
            elif capture and line.startswith("[INFO] ---"):  # in case another plugin block starts
                break
            elif capture and line.startswith("[INFO]"):
                content = line[7:]  # strip "[INFO] "
                tree_lines.append(content)
        print(f"\nDependency tree for {repo_path}:")
        print("\n".join(tree_lines))
    else:
        print(f"Error in {repo_path}:")
        print(result.stderr)

for repo in os.listdir(repos_dir):
    repo_path = os.path.join(repos_dir, repo)
    if os.path.isdir(repo_path):
        run_maven_dependency_tree(repo_path)

