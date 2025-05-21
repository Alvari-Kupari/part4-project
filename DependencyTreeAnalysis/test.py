import os

def find_standalone_gradle_projects(base_dir):
    for entry in os.listdir(base_dir):
        repo_path = os.path.join(base_dir, entry)
        if not os.path.isdir(repo_path):
            continue

        has_gradle = any(
            os.path.isfile(os.path.join(repo_path, fname))
            for fname in ["build.gradle", "build.gradle.kts"]
        )

        has_srcmainjava = os.path.isdir(os.path.join(repo_path, "src", "main", "java"))
        

        if has_gradle and has_srcmainjava:
            print(entry)

# Example usage
find_standalone_gradle_projects("D:/test/")
