import os
import subprocess

repos_dir = "/Users/tonyyin/Desktop/Projects/Repos/hutool"
output_dir = "/Users/tonyyin/Desktop/DependencyTrees"

# Ensure the output directory exists
os.makedirs(output_dir, exist_ok=True)

def run_maven_dependency_tree(repo_path, repo_name):
    cmd = ["mvn", "dependency:tree", "-Dverbose=true", "-DoutputType=text"]
    result = subprocess.run(cmd, cwd=repo_path, capture_output=True, text=True)

    output_file_path = os.path.join(output_dir, f"{repo_name}_dependency_tree.txt")

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

        # Print to console
        print(f"\nDependency tree for {repo_path}:")
        print("\n".join(tree_lines))

        # Write to file
        with open(output_file_path, "w") as f:
            f.write("\n".join(tree_lines))
        print(f"Saved to {output_file_path}")

    else:
        print(f"Error in {repo_path}:")
        print(result.stderr)
        with open(output_file_path, "w") as f:
            f.write("ERROR:\n" + result.stderr)

# Loop through repos
for repo in os.listdir(repos_dir):
    repo_path = os.path.join(repos_dir, repo)
    if os.path.isdir(repo_path):
        run_maven_dependency_tree(repo_path, repo)
