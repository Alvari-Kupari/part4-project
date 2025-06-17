import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import time
import numpy as np

# Load the CSV
csv_path = "output/dependency_conflict_stats_summary.csv"
df = pd.read_csv(csv_path)

# Ensure the column is numeric
df['omitted_conflict_percentage'] = pd.to_numeric(df['omitted_conflict_percentage'], errors='coerce')

# --- PIE CHART ---
# Modules with 0 omitted_conflict_percentage vs >0
zero_conflict = (df['omitted_conflict_percentage'] == 0).sum()
nonzero_conflict = (df['omitted_conflict_percentage'] > 0).sum()

plt.figure(figsize=(6, 6))
plt.pie(
    [zero_conflict, nonzero_conflict],
    labels=['No Version Conflict', 'Has Version Conflict'],
    autopct='%1.1f%%',
    colors=['#8fd9b6', '#ffb347'],
    startangle=90
)
plt.title('Modules: No Version Conflict vs. Has Version Conflict')
plt.tight_layout()
plt.savefig('pie_conflict.png')
# plt.show()

# --- BOX AND WHISKER PLOT ---
print("Starting box plot generation...")
start_time = time.time()

# Filter out rows with NaN values for cleaner visualization
df_clean = df.dropna(subset=['omitted_conflict_percentage'])
print(f"Data shape after cleaning: {df_clean.shape}")

# Calculate main box plot statistics
data = df_clean['omitted_conflict_percentage']
stats = {
    'minimum': data.min(),
    'lower_quartile_q1': data.quantile(0.25),
    'median_q2': data.median(),
    'upper_quartile_q3': data.quantile(0.75),
    'maximum': data.max()
}

# Print main statistics
print("\n=== BOX PLOT STATISTICS ===")
print(f"Minimum: {stats['minimum']:.2f}%")
print(f"Lower Quartile (Q1): {stats['lower_quartile_q1']:.2f}%")
print(f"Median (Q2): {stats['median_q2']:.2f}%")
print(f"Upper Quartile (Q3): {stats['upper_quartile_q3']:.2f}%")
print(f"Maximum: {stats['maximum']:.2f}%")

# Save main statistics to CSV
stats_df = pd.DataFrame([stats])
stats_df.to_csv('boxplot_main_statistics.csv', index=False)
print("Main statistics saved to boxplot_main_statistics.csv")

# Create horizontal box plot
plt.figure(figsize=(10, 6))
plt.boxplot(data, 
           vert=False,  # This makes it horizontal
           patch_artist=True,
           boxprops=dict(facecolor='lightblue', alpha=0.7),
           medianprops=dict(color='red', linewidth=2),
           flierprops=dict(marker='o', markersize=4, alpha=0.5))

plt.xlabel('Conflict Percentage (%)')
plt.title('Distribution of Conflict Percentage Across Modules (Horizontal)')
plt.grid(True, alpha=0.3)
plt.tight_layout()

print(f"Plot created in {time.time() - start_time:.2f} seconds")

plt.savefig('boxplot_conflict_percentage_horizontal.png', dpi=150, bbox_inches='tight')
print("Horizontal box plot saved successfully")

# Comment out plt.show() to avoid display issues
# plt.show()
print("Box plot generation completed")