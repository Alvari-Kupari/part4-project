package com.example.depanalyzer.analyzer.report;

import java.util.ArrayList;
import java.util.List;

public class UsageReport {
  private List<Usage> usages;

  public UsageReport() {
    this.usages = new ArrayList<>();
  }

  public void addUsage(Usage usage) {
    usages.add(usage);
  }

  public void print() {
    System.out.println("\n===============================");
    System.out.println("         USAGE REPORT          ");
    System.out.println("===============================\n");

    if (usages.isEmpty()) {
      System.out.println("No usages found.\n");
      return;
    }

    for (int i = 0; i < usages.size(); i++) {
      System.out.println((i + 1) + ". " + usages.get(i));
    }

    System.out.println("\nTotal usages: " + usages.size() + "\n");
  }
}
