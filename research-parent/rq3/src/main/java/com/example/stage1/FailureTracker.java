package com.example.stage1;

import org.eclipse.aether.graph.Dependency;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tracks failed dependency comparisons and writes them to a log file.
 */
public class FailureTracker {
    private final Path logFile;
    private final AtomicInteger totalComparisons = new AtomicInteger(0);
    private final AtomicInteger failedComparisons = new AtomicInteger(0);
    private final PrintWriter writer;
    
    public FailureTracker(Path csvFolder) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        this.logFile = csvFolder.resolve("comparison_failures_" + timestamp + ".log");
        
        Files.createDirectories(csvFolder);
        this.writer = new PrintWriter(Files.newBufferedWriter(logFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND));
        
        // Write header
        writer.println("=== DEPENDENCY COMPARISON FAILURE LOG ===");
        writer.println("Started at: " + LocalDateTime.now());
        writer.println("Format: [TIMESTAMP] DEPENDENCY_OLD -> DEPENDENCY_NEW : ERROR_MESSAGE");
        writer.println("========================================");
        writer.flush();
    }
    
    /**
     * Record a successful comparison
     */
    public void recordSuccess() {
        totalComparisons.incrementAndGet();
    }
    
    /**
     * Record a failed comparison
     */
    public void recordFailure(Dependency oldDep, Dependency newDep, Exception error) {
        totalComparisons.incrementAndGet();
        failedComparisons.incrementAndGet();
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String oldDepStr = formatDependency(oldDep);
        String newDepStr = formatDependency(newDep);
        
        writer.printf("[%s] %s -> %s : %s%n", 
            timestamp, oldDepStr, newDepStr, error.getMessage());
        writer.flush();
    }
    
    /**
     * Get total number of comparisons attempted
     */
    public int getTotalComparisons() {
        return totalComparisons.get();
    }
    
    /**
     * Get number of failed comparisons
     */
    public int getFailedComparisons() {
        return failedComparisons.get();
    }
    
    /**
     * Get success rate as percentage
     */
    public double getSuccessRate() {
        int total = totalComparisons.get();
        if (total == 0) return 0.0;
        return ((double)(total - failedComparisons.get()) / total) * 100.0;
    }
    
    /**
     * Write summary statistics and close the log file
     */
    public void close() {
        writer.println();
        writer.println("=== SUMMARY STATISTICS ===");
        writer.printf("Total comparisons attempted: %d%n", getTotalComparisons());
        writer.printf("Failed comparisons: %d%n", getFailedComparisons());
        writer.printf("Success rate: %.2f%%%n", getSuccessRate());
        writer.println("Completed at: " + LocalDateTime.now());
        writer.println("============================");
        writer.close();
    }
    
    private String formatDependency(Dependency dep) {
        return String.format("%s:%s:%s", 
            dep.getArtifact().getGroupId(),
            dep.getArtifact().getArtifactId(),
            dep.getArtifact().getVersion());
    }
}
