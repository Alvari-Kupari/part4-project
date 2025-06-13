package com.example.depanalyzer.analyzer.report;

import java.io.File;

public class Usage {
  private int lineLocation;
  private File fileLocation;
  private String msg;

  public Usage(String msg) {
    this.msg = msg;
  }

  public String getMsg() {
    return msg;
  }
}
