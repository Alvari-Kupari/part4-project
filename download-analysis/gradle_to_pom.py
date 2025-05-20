import os
import subprocess

def convert_gradle_to_pom(project_dir):
    project_dir = os.path.abspath(project_dir)

    pom_path = os.path.join(project_dir, "pom.xml")
    gradle_file = os.path.join(project_dir, "build.gradle")
    gradle_kts_file = os.path.join(project_dir, "build.gradle.kts")

    if os.path.exists(pom_path):
        print(f"[SKIP] pom.xml already exists in: {project_dir}")
        return

    if not (os.path.exists(gradle_file) or os.path.exists(gradle_kts_file)):
        print(f"[SKIP] No Gradle build file in: {project_dir}")
        return

    print(f"[INFO] Converting to pom.xml in: {project_dir}")
    try:
        subprocess.run(
           ["cmd", "/c", "gradle init --type pom"],
            cwd=project_dir,
            check=True
        )
        print(f"[DONE] Generated pom.xml in: {project_dir}")
    except subprocess.CalledProcessError as e:
        print(f"[ERROR] Failed to generate pom.xml in {project_dir}: {e}")

# Example usage
convert_gradle_to_pom("D:/test/baritone")
