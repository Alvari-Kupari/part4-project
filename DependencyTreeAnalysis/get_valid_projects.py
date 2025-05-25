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
                build_files = ["pom.xml"]
                has_build_file = any(bf in files for bf in build_files)

                if has_build_file:
                    valid_projects.append(os.path.normpath(root))


        # prevent descending into subfolders of submodules
        #dirs.clear()

    return valid_projects



# Example usage
projects = find_valid_projects("D:/test/Activiti")
print(projects)


