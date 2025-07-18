package com.example.depanalyzer.analyzer.report;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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

  public void writeToMarkDown(Path outputPath) throws IOException {
    File markdownFile = outputPath.toFile();
    Markdown markdown = new Markdown(markdownFile);

    if (usages.isEmpty()) {
      markdown.write("No usages found.\n");
      markdown.close();
      return;
    }

    markdown.header("Usage Report");

    for (int i = 0; i < usages.size(); i++) {
      Usage usage = usages.get(i);

      markdown.subHeader((i + 1) + ". " + usage.getType().getMessage());
      markdown.write(
          "**Location:** `"
              + usage.getFileLocation().getPath()
              + ":"
              + usage.getLineNumber()
              + "`  ");
      markdown.write("**Library:** `" + usage.getLibraryName() + "`");

      markdown.codeBlock(usage.getLineText(), usage.getColumnStart(), usage.getColumnEnd());
    }

    markdown.write("\n**Total usages:** " + usages.size());
    markdown.close();
  }
}
