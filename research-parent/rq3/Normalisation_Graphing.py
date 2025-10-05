import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import os
import numpy as np

# --- load data ---
results_file = r"C:\Users\Alvari\Documents\part4-project\data\rq3\csv\normalized_results.csv"
df = pd.read_csv(results_file)

print(f"Total submodules analysed: {len(df)}")
nonzero_repos = df[(df['normalised_direct_deps'] > 0) | (df['normalised_transitive_deps'] > 0)]
print(f"Submodules with any used breaking changes: {len(nonzero_repos)}\n")

# --- descriptive stats ---
print("Descriptive statistics for normalised_direct_deps:")
print(df['normalised_direct_deps'].describe(), "\n")
print("Descriptive statistics for normalised_transitive_deps:")
print(df['normalised_transitive_deps'].describe(), "\n")



# --- ensure figures folder exists ---
figures_folder = r"C:\Users\Alvari\Documents\part4-project\data\rq3\figures"
os.makedirs(figures_folder, exist_ok=True)

# --- clip extreme outliers for plotting ---
clip_upper = df[['normalised_direct_deps', 'normalised_transitive_deps']].quantile(0.99)
df_clipped = df[['normalised_direct_deps', 'normalised_transitive_deps']].clip(upper=clip_upper, axis=1)

# --- add tiny offset to avoid log(0) if needed ---
offset = 1e-5
df_clipped += offset

# --- existing boxplot ---
plt.figure(figsize=(8,6))
sns.boxplot(data=df_clipped)
plt.title('Distribution of Normalised Direct vs Transitive Dependencies (clipped top 1%)')
plt.ylabel('Normalised Value')
plt.savefig(os.path.join(figures_folder, 'boxplot_normalised_clipped.png'))
plt.close()

# --- existing scatter plot ---
plt.figure(figsize=(8,6))
sns.scatterplot(
    x=df_clipped['normalised_direct_deps'],
    y=df_clipped['normalised_transitive_deps']
)
plt.xscale('log')
plt.yscale('log')
plt.title('Direct vs Transitive Normalised Dependencies per Submodule (log scale, clipped top 1%)')
plt.xlabel('Normalised Direct')
plt.ylabel('Normalised Transitive')
plt.savefig(os.path.join(figures_folder, 'scatter_normalised_clipped.png'))
plt.close()

# --- create zero-removed dataset ---
df_zero_removed = df_clipped[(df_clipped['normalised_direct_deps'] > offset) | 
                             (df_clipped['normalised_transitive_deps'] > offset)]

# --- boxplot (zero-removed) ---
plt.figure(figsize=(8,6))
sns.boxplot(data=df_zero_removed)
plt.title('Distribution of Normalised Direct vs Transitive Dependencies (zero-removed)')
plt.ylabel('Normalised Value')
plt.savefig(os.path.join(figures_folder, 'boxplot_normalised_zero_removed.png'))
plt.close()

# --- scatter plot (zero-removed) ---
plt.figure(figsize=(8,6))
sns.scatterplot(
    x=df_zero_removed['normalised_direct_deps'],
    y=df_zero_removed['normalised_transitive_deps']
)
plt.xscale('log')
plt.yscale('log')
plt.title('Direct vs Transitive Normalised Dependencies per Submodule (zero-removed)')
plt.xlabel('Normalised Direct')
plt.ylabel('Normalised Transitive')
plt.savefig(os.path.join(figures_folder, 'scatter_normalised_zero_removed.png'))
plt.close()

print("=== Zero-removed dataset summary ===")
print(f"Total submodules after removing all-zero entries: {len(df_zero_removed)}")
print("Descriptive statistics for normalised_direct_deps (zero-removed):")
print(df_zero_removed['normalised_direct_deps'].describe(), "\n")
print("Descriptive statistics for normalised_transitive_deps (zero-removed):")
print(df_zero_removed['normalised_transitive_deps'].describe(), "\n")

