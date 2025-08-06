package com.example.stage1.dependencyupdate;

import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.version.InvalidVersionSpecificationException;

public class VersionResolutionException extends Exception {
  public VersionResolutionException(InvalidVersionSpecificationException e) {
    super(e);
  }

  public VersionResolutionException(VersionRangeResolutionException e) {
    super(e);
  }

  public VersionResolutionException(String string) {
    super(string);
  }
}
