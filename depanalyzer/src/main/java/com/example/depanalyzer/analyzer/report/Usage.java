package com.example.depanalyzer.analyzer.report;

import java.io.File;

public class Usage {
  private int lineLocation;
  private File fileLocation;
  private String msg;

  public Usage(int lineLocation, File fileLocation, String msg) {
    this.msg = msg;
    this.fileLocation = fileLocation;
    this.lineLocation = lineLocation;
  }

  @Override
  public String toString() {
    return msg + " at file: " + fileLocation.toString() + ":" + lineLocation;
  }
}
