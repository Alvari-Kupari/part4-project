import os
import pandas as pd

max_repo_size = 500000  # Maximum repository size in KB

def log_repo_size_filtering(csvPath):

    df = pd.read_csv(csvPath)
    
    # Log the number of repositories before filtering
    print(f"Number of repositories before filtering: {len(df)}")
    
    # Filter out repositories larger than max size
    if 'Repo Size (KB)' not in df.columns:
        raise ValueError("The DataFrame does not contain 'Repo Size (KB)' column.")
    

    df_filtered = df[df['Repo Size (KB)'] <= max_repo_size]

    # find the % of repos that were filtered out
    filtered_out_count = len(df) - len(df_filtered)
    filtered_out_percentage = (filtered_out_count / len(df)) * 100 if len(df) > 0 else 0
    print(f"Filtered out {filtered_out_count} repositories ({filtered_out_percentage:.2f}%) larger than {max_repo_size} KB.")

    
    
    # Log the number of repositories after filtering
    print(f"Number of repositories after filtering: {len(df_filtered)}")
    
    # Save the filtered DataFrame back to CSV
    df_filtered.to_csv(csvPath, index=False)

csvPath = r"C:\Users\akup390\Documents\tony-alvari-part4\part4-project\data\rq1\filtering-results\repo-sizes.csv"
if __name__ == "__main__":
    log_repo_size_filtering(csvPath)