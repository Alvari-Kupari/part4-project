import requests
import csv
import time
from datetime import datetime, timedelta

GITHUB_TOKEN = ''  # Optional: add your GitHub token
HEADERS = {'Authorization': f'token {GITHUB_TOKEN}'} if GITHUB_TOKEN else {}

def has_recent_commit(repo_full_name):
    branches_url = f"https://api.github.com/repos/{repo_full_name}/branches"
    branches_resp = requests.get(branches_url, headers=HEADERS)
    if branches_resp.status_code != 200:
        print(f"Failed to get branches for {repo_full_name}")
        return False

    branches = branches_resp.json()
    cutoff = datetime.utcnow() - timedelta(days=60)

    for branch in branches:
        commit_url = f"https://api.github.com/repos/{repo_full_name}/commits/{branch['name']}"
        commit_resp = requests.get(commit_url, headers=HEADERS)
        if commit_resp.status_code != 200:
            continue
        commit_data = commit_resp.json()
        commit_date_str = commit_data["commit"]["committer"]["date"]
        commit_date = datetime.strptime(commit_date_str, "%Y-%m-%dT%H:%M:%SZ")
        if commit_date > cutoff:
            return True

    return False

def fetch_repos_page(page):
    url = (
        "https://api.github.com/search/repositories"
        "?q=language:java&sort=stars&order=desc&per_page=100&page=" + str(page)
    )
    resp = requests.get(url, headers=HEADERS)
    if resp.status_code != 200:
        print(f"GitHub API error: {resp.status_code}")
        return []
    data = resp.json()
    return data.get("items", [])

def main():
    with open("repo_check.csv", mode="w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(["full_name", "stars", "recent_commit"])

    page = 1
    total_checked = 0

    while True:
        repos = fetch_repos_page(page)
        if not repos:
            break
        for repo in repos:
            full_name = repo["full_name"]
            stars = repo["stargazers_count"]
            print(f"Checking: {full_name} ({stars} stars)")
            recent = has_recent_commit(full_name)
            with open("repo_check.csv", mode="a", newline="", encoding="utf-8") as f:
                writer = csv.writer(f)
                writer.writerow([full_name, stars, recent])
            total_checked += 1
            time.sleep(1)  # avoid rate limits
        page += 1

if __name__ == "__main__":
    main()
