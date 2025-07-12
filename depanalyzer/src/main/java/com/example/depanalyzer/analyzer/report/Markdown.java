package com.example.depanalyzer.analyzer.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Markdown {
  private final BufferedWriter writer;

  public Markdown(File markdownFile) throws IOException {
    this.writer = new BufferedWriter(new FileWriter(markdownFile));
  }

  public void header(String text) throws IOException {
    writer.write("# " + text + "\n\n");
  }

  public void subHeader(String text) throws IOException {
    writer.write("## " + text + "\n\n");
  }

  public void write(String line) throws IOException {
    writer.write(line + "\n\n");
  }

  public void codeBlock(String lineText, int start, int end) throws IOException {
    writer.write("```java\n");
    writer.write(lineText + "\n");
    if (end >= start) {
      int startIdx = Math.max(0, start - 1);
      int len = Math.max(1, end - start + 1);
      writer.write(" ".repeat(startIdx) + "^".repeat(len) + "\n");
    } else {
      writer.write("// [multi-line expression — squiggle skipped]\n");
    }
    writer.write("```\n\n");
  }

  public void close() throws IOException {
    writer.close();
  }
}
