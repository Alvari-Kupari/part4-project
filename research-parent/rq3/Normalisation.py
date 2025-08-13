import pandas as pd
import os

def normalise_bc_usage(folder_path):
    # Load CSVs
    used_bc_df = pd.read_csv(os.path.join(folder_path, "depanalyzer_depanalyzer-used-breaking-changes.csv"))
    client_symbols_df = pd.read_csv(os.path.join(folder_path, "depanalyzer_depanalyzer-client-symbol-uses.csv"))

    # Count total symbols per library
    # We'll need a library mapping for each symbol (in client_symbols_df we don't have Library_Name, so we can't match exactly)
    # Assuming Class_Name in client_symbols_df matches Class_Name in used_bc_df to get Library_Name
    class_to_lib = used_bc_df[['Class_Name', 'Library_Name']].drop_duplicates()
    client_symbols_df = client_symbols_df.merge(class_to_lib, on='Class_Name', how='left')

    # Drop rows where we couldn't find a library
    client_symbols_df = client_symbols_df.dropna(subset=['Library_Name'])

    total_symbols_per_lib = client_symbols_df.groupby('Library_Name').size().reset_index(name='Total_Symbols')

    # Count breaking changes by library and transitive/direct
    bc_counts = used_bc_df.groupby(['Library_Name', 'Is_Transitive']).size().reset_index(name='BC_Count')

    # Merge counts with total symbols
    merged = bc_counts.merge(total_symbols_per_lib, on='Library_Name', how='left')

    # Compute normalised values
    merged['Normalised'] = merged['BC_Count'] / merged['Total_Symbols']

    return merged

# Run function on uploaded CSV folder
folder_path = r"C:\Users\Alvari\Documents\UNI\softeng_700\part4-project\data\rq3\depanalyzer_depanalyzer"
df_results = normalise_bc_usage(folder_path)
print(df_results)
