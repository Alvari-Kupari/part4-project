import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("D:/repo-logs/logs-1000-repos.csv")
repo_sizes_mb = df["Repo Size (KB)"] / 1024

q1 = repo_sizes_mb.quantile(0.25)
q2 = repo_sizes_mb.quantile(0.50)
q3 = repo_sizes_mb.quantile(0.75)
iqr = q3 - q1
upper_whisker = repo_sizes_mb[repo_sizes_mb <= q3 + 1.5 * iqr].max()

print(f"Q1 (25%): {q1:.2f} MB")
print(f"Q2 (Median): {q2:.2f} MB")
print(f"Q3 (75%): {q3:.2f} MB")
print(f"Rightmost whisker value: {upper_whisker:.2f} MB")

plt.figure(figsize=(10, 4))
box = plt.boxplot(repo_sizes_mb, vert=False, patch_artist=False)

# Set outline color to red
for element in ['boxes', 'whiskers', 'caps', 'medians']:
    for item in box[element]:
        item.set_color('blue')

plt.xlabel("Repo Size (MB)")
plt.title("Boxplot of Repository Sizes")
#plt.xlim(0, 500)
plt.grid(True)
plt.tick_params(axis='y', left=False, labelleft=False)

plt.show()
