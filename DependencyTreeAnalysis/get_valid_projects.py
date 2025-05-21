import os
import subprocess

def find_valid_projects(repo_path):
    valid_projects = []

    for root, dirs, files in os.walk(repo_path):
        # Skip nested folders (only consider root of potential modules)
        if 'src' in dirs:
            src_main_java = os.path.join(root, 'src', 'main', 'java')
            if os.path.isdir(src_main_java):
                has_java_file = any(
                    f.endswith(".java")
                    for dirpath, _, filenames in os.walk(src_main_java)
                    for f in filenames
                )

                if not has_java_file:
                    continue

                # Check for build file in the root
                build_files = ["pom.xml", "build.gradle", "build.gradle.kts"]
                has_build_file = any(bf in files for bf in build_files)

                if has_build_file:
                    valid_projects.append(os.path.normpath(root))


        # prevent descending into subfolders of submodules
        #dirs.clear()

    return valid_projects

def run_maven_dependency_tree(project_dir):
    project_dir = os.path.abspath(project_dir)
    pom_path = os.path.join(project_dir, "pom.xml")

    if not os.path.isfile(pom_path):
        print(f"[SKIP] No pom.xml found in: {project_dir}")
        return

    print(f"[INFO] Running Maven dependency:tree in {project_dir}")
    try:
        result = subprocess.run(
            ["cmd", "/c", "mvn dependency:tree -Dverbose=true -DoutputType=text"],

            cwd=project_dir,
            capture_output=True,
            text=True,
            check=True
        )
        print(result.stdout)  # or write to file if needed
    except subprocess.CalledProcessError as e:
        print(f"[ERROR] Maven failed in {project_dir}:\n{e.stderr.strip()}")

# Example usage
projects = find_valid_projects("D:/test/cryptomator")
print(projects)

for project in projects :
    run_maven_dependency_tree(project)



