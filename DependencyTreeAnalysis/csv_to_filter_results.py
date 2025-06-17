import csv
from collections import Counter
import os

def generate_filter_summary(csv_file_path, output_file_path=None):
    """
    Generate a summary report from the repo filtering CSV file.
    
    Args:
        csv_file_path (str): Path to the CSV file containing repo filtering results
        output_file_path (str): Optional path to save the summary as a text file
    """
    
    # Read the CSV file
    repos = []
    try:
        with open(csv_file_path, 'r', encoding='utf-8') as file:
            reader = csv.DictReader(file)
            for row in reader:
                repos.append(row)
    except FileNotFoundError:
        print(f"Error: CSV file not found at {csv_file_path}")
        return
    except Exception as e:
        print(f"Error reading CSV file: {e}")
        return
    
    # Calculate statistics
    total_considered = len(repos)
    
    # Count repos by status
    status_counts = Counter(repo['Reason For Rejection'] for repo in repos)
    
    # Calculate kept repos (ALREADY_DOWNLOADED + PASSED_ALL_CHECKS)
    kept_repos = status_counts['ALREADY_DOWNLOADED'] + status_counts['PASSED_ALL_CHECKS']
    
    # Calculate rejected repos (everything else)
    rejected_repos = total_considered - kept_repos
    
    # Generate the summary report content
    report_lines = [
        "--- Repo Filtering Summary ---",
        f"Total considered: {total_considered}",
        f"Total kept: {kept_repos} ({kept_repos/total_considered*100:.1f}%)",
        f"Total rejected: {rejected_repos} ({rejected_repos/total_considered*100:.1f}%)",
        "",
        "Breakdown by reason:"
    ]
    
    # Sort reasons by count (descending) and add to report
    sorted_reasons = sorted(status_counts.items(), key=lambda x: x[1], reverse=True)
    
    for reason, count in sorted_reasons:
        percentage = count / total_considered * 100
        report_lines.append(f"  {reason}: {count} ({percentage:.1f}%)")
    
    # Print to console
    for line in report_lines:
        print(line)
    
    # Save to file if output path is provided
    if output_file_path:
        try:
            # Create directory if it doesn't exist
            os.makedirs(os.path.dirname(output_file_path), exist_ok=True)
            
            with open(output_file_path, 'w', encoding='utf-8') as file:
                file.write('\n'.join(report_lines))
            
            print(f"\nSummary saved to: {output_file_path}")
        except Exception as e:
            print(f"Error saving to file: {e}")

if __name__ == "__main__":
    # Path to the CSV file
    csv_file_path = r"c:\Users\akup390\Documents\logs\repo-filter.csv"
    
    # Path for the output text file
    output_file_path = r"c:\Users\akup390\Documents\logs\repo-filter-summary.txt"
    
    # Generate the summary (will print to console AND save to file)
    generate_filter_summary(csv_file_path, output_file_path)