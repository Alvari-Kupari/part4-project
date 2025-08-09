package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.aether.graph.Dependency;

/** Tracks failed dependency comparisons and writes them to a log file. */
public class FailureTracker {
  private final Path logFile;
  private final AtomicInteger totalComparisons = new AtomicInteger(0);
  private final AtomicInteger failedComparisons = new AtomicInteger(0);
  private final PrintWriter writer;

  public FailureTracker(Path csvFolder) throws IOException {
    Files.createDirectories(csvFolder);
    this.logFile = csvFolder.resolve("comparison-failures.log");
    this.writer =
        new PrintWriter(
            Files.newBufferedWriter(
                logFile,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND,
                StandardOpenOption.WRITE));
    writer.println("=== Comparison Failure Log ===");
    writer.flush();
  }

  /** Record a successful comparison */
  public void recordSuccess() {
    totalComparisons.incrementAndGet();
  }

  /** Record a failed comparison */
  public void recordFailure(Dependency oldDep, Dependency newDep, Exception error) {
    totalComparisons.incrementAndGet();
    failedComparisons.incrementAndGet();

    String timestamp =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    String oldDepStr = formatDependency(oldDep);
    String newDepStr = formatDependency(newDep);

    writer.printf("[%s] %s -> %s : %s%n", timestamp, oldDepStr, newDepStr, error.getMessage());
    writer.flush();
  }

  /** Get total number of comparisons attempted */
  public int getTotalComparisons() {
    return totalComparisons.get();
  }

  /** Get number of failed comparisons */
  public int getFailedComparisons() {
    return failedComparisons.get();
  }

  /** Get success rate as percentage */
  public double getSuccessRate() {
    int total = totalComparisons.get();
    if (total == 0) return 100.0;
    return ((double) (total - failedComparisons.get()) / total) * 100.0;
  }

  /** Write summary statistics and close the log file */
  public void close() {
    try {
      writer.println();
      writer.println("=== Summary ===");
      writer.printf("Total comparisons: %d%n", getTotalComparisons());
      writer.printf("Failures: %d%n", getFailedComparisons());
      writer.printf("Success rate: %.2f%%%n", getSuccessRate());
    } finally {
      writer.flush();
      writer.close();
    }
  }

  private String formatDependency(Dependency dep) {
    if (dep == null || dep.getArtifact() == null) return "null";
    return dep.getArtifact().getGroupId()
        + ":"
        + dep.getArtifact().getArtifactId()
        + ":"
        + dep.getArtifact().getVersion();
  }
}
