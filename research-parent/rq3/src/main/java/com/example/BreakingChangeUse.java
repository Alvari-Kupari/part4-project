package com.example;

import com.example.breakingchange.BreakingChange;

/** One CSV row describing a breaking change and whether it is used by client code. */
public class BreakingChangeUse {
  private final BreakingChange change;
  private final boolean usedInClient;
  private final int uniqueSymbolsUsed;
  private final int affectedSymbols;
  private final String dependencyGA;
  private final String currentVersion;
  private final String latestMinorVersion;

  public BreakingChangeUse(
      BreakingChange change,
      boolean usedInClient,
      int uniqueSymbolsUsed,
      int affectedSymbols,
      String dependencyGA,
      String currentVersion,
      String latestMinorVersion) {
    this.change = change;
    this.usedInClient = usedInClient;
    this.uniqueSymbolsUsed = uniqueSymbolsUsed;
    this.affectedSymbols = affectedSymbols;
    this.dependencyGA = dependencyGA;
    this.currentVersion = currentVersion;
    this.latestMinorVersion = latestMinorVersion;
  }

  public BreakingChange getChange() {
    return change;
  }

  public boolean isUsedInClient() {
    return usedInClient;
  }

  public int getUniqueSymbolsUsed() {
    return uniqueSymbolsUsed;
  }

  public int getAffectedSymbols() {
    return affectedSymbols;
  }

  public String getDependencyGA() {
    return dependencyGA;
  }

  public String getCurrentVersion() {
    return currentVersion;
  }

  public String getLatestMinorVersion() {
    return latestMinorVersion;
  }
}
