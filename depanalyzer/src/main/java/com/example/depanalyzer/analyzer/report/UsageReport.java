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
    for (Usage usage : usages) {
      System.out.println(usage.getMsg());
    }
  }
}
