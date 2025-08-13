package com.example.breakingchange;

/** Custom exception for breaking change analysis failures. */
public class BreakingChangeAnalysisException extends Exception {
  public BreakingChangeAnalysisException(String message) {
    super(message);
  }
  
  public BreakingChangeAnalysisException(String message, Throwable cause) {
    super(message, cause);
  }
}
