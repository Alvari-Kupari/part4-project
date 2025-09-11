# read the submodule_stats.csv file which has 4 columns: repo, submodule, LOC and Deps. then output a boxplot of the distributions
import os
import csv
from pathlib import Path
import matplotlib.pyplot as plt

def output_LOC_boxplot(csv_path):
    loc_data = []
    with open(csv_path, "r") as csvfile:
        csv_reader = csv.DictReader(csvfile)
        for row in csv_reader:
            loc_data.append(int(row["LOC"]))
    plt.boxplot(loc_data)
    plt.title("LOC Distribution")
    plt.ylabel("Lines of Code (LOC)")
    plt.show()


def output_Deps_boxplot(csv_path):
    deps_data = []
    with open(csv_path, "r") as csvfile:
        csv_reader = csv.DictReader(csvfile)
        for row in csv_reader:
            deps_data.append(int(row["Dependencies"]))
    plt.boxplot(deps_data)
    plt.title("Dependencies Distribution")
    plt.ylabel("Number of Dependencies")
    plt.show()


if __name__ == "__main__":
    csv_path = Path(r"C:\Users\tyin363\Documents\part4-project\research-parent\rq3\submodule_stats.csv")
    output_LOC_boxplot(csv_path)
    output_Deps_boxplot(csv_path)
