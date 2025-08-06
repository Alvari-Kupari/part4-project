package com.example.stage1.breakingchange;

/** Represents a breaking change detected by JAPICMP between two versions of a dependency. */
public class BreakingChange {
  private final String className;
  private final String memberName;
  private final String changeType;
  private final String description;
  private final String libraryName;
  private final String oldVersion;
  private final String newVersion;
  private final boolean isBinaryCompatible;
  private final boolean isSourceCompatible;

  private BreakingChange(Builder builder) {
    this.className = builder.className;
    this.memberName = builder.memberName;
    this.changeType = builder.changeType;
    this.description = builder.description;
    this.libraryName = builder.libraryName;
    this.oldVersion = builder.oldVersion;
    this.newVersion = builder.newVersion;
    this.isBinaryCompatible = builder.isBinaryCompatible;
    this.isSourceCompatible = builder.isSourceCompatible;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String className;
    private String memberName;
    private String changeType;
    private String description;
    private String libraryName;
    private String oldVersion;
    private String newVersion;
    private boolean isBinaryCompatible;
    private boolean isSourceCompatible;

    public Builder className(String className) {
      this.className = className;
      return this;
    }

    public Builder memberName(String memberName) {
      this.memberName = memberName;
      return this;
    }

    public Builder changeType(String changeType) {
      this.changeType = changeType;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder libraryName(String libraryName) {
      this.libraryName = libraryName;
      return this;
    }

    public Builder oldVersion(String oldVersion) {
      this.oldVersion = oldVersion;
      return this;
    }

    public Builder newVersion(String newVersion) {
      this.newVersion = newVersion;
      return this;
    }

    public Builder isBinaryCompatible(boolean isBinaryCompatible) {
      this.isBinaryCompatible = isBinaryCompatible;
      return this;
    }

    public Builder isSourceCompatible(boolean isSourceCompatible) {
      this.isSourceCompatible = isSourceCompatible;
      return this;
    }

    public BreakingChange build() {
      return new BreakingChange(this);
    }
  }

  // Getters
  public String getClassName() {
    return className;
  }

  public String getMemberName() {
    return memberName;
  }

  public String getChangeType() {
    return changeType;
  }

  public String getDescription() {
    return description;
  }

  public String getLibraryName() {
    return libraryName;
  }

  public String getOldVersion() {
    return oldVersion;
  }

  public String getNewVersion() {
    return newVersion;
  }

  public boolean isBinaryCompatible() {
    return isBinaryCompatible;
  }

  public boolean isSourceCompatible() {
    return isSourceCompatible;
  }

  @Override
  public String toString() {
    return String.format(
        "BreakingChange{library='%s', %s->%s, class='%s', member='%s', type='%s',"
            + " compatible=binary:%s/source:%s}",
        libraryName,
        oldVersion,
        newVersion,
        className,
        memberName,
        changeType,
        isBinaryCompatible,
        isSourceCompatible);
  }
}
