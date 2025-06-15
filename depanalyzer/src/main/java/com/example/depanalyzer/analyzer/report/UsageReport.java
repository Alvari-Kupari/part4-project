package com.example.depanalyzer.analyzer.report;

import java.util.ArrayList;
import java.util.List;

public class UsageReport {
  private List<Usage> usages;

  public UsageReport() {
    this.usages = new ArrayList<>();
  }

  public void addUsage(Usage usage) {
    System.out.println("Adding usage");
    usages.add(usage);
  }

  public void print() {
    System.out.println(usages);
  }
}
