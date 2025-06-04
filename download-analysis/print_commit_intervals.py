import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("D:/repo-logs/logs-1000-repos.csv")


commits30 = df["Has Commit 30"].sum()
commits60 = df["Has Commit 60"].sum()
commits90 = df["Has Commit 90"].sum()
commits180 = df["Has Commit 180"].sum()
commits365 = df["Has Commit 365"].sum()

print(f"Has commit in last 30 days: {commits30}")
print(f"Has commit in last 60 days: {commits60}")
print(f"Has commit in last 90 days: {commits90}")
print(f"Has commit in last 180 days: {commits180}")
print(f"Has commit in last 365 days: {commits365}")

counts = {
    "30 days": df["Has Commit 30"].sum(),
    "60 days": df["Has Commit 60"].sum(),
    "90 days": df["Has Commit 90"].sum(),
    "180 days": df["Has Commit 180"].sum(),
    "365 days": df["Has Commit 365"].sum()
}

# Plot
plt.figure(figsize=(8, 5))
plt.bar(counts.keys(), counts.values())
plt.ylabel("Number of Repositories")
plt.title("Repositories with Commits in Last N Days (out of 1000)")
plt.ylim(0, 1000)
plt.grid(axis='y')
plt.show()
