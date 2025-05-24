import os
from get_valid_projects import find_valid_projects

repos_path = "D:/test"
count = 0

for repo in os.listdir(repos_path):
    full_path = os.path.join(repos_path, repo)
    projects = find_valid_projects(full_path)

    if len(projects) == 1:
        count += 1
        print(f"{repo} is a standalone maven project")

print(count)
