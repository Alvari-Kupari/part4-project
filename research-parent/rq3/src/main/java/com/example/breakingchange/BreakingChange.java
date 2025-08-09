package com.example.breakingchange;

import org.eclipse.aether.graph.Dependency;

/** Represents a breaking change detected by JAPICMP between two versions of a dependency. */
public class BreakingChange {
  private final String className;
  private final String memberName;
  private final String changeType;
  private final String description;

  // Replaced string fields with Dependency objects
  private final Dependency oldDependency;
  private final Dependency newDependency;

  // Additional metadata
  private final int depth; // 1 = direct, >1 = transitive
  private final Dependency directParentDependency; // direct dep that introduced the transitive one

  private final boolean isBinaryCompatible;
  private final boolean isSourceCompatible;
  private final boolean isTransitive;

  private BreakingChange(Builder builder) {
    this.className = builder.className;
    this.memberName = builder.memberName;
    this.changeType = builder.changeType;
    this.description = builder.description;
    this.oldDependency = builder.oldDependency;
    this.newDependency = builder.newDependency;
    this.depth = builder.depth;
    this.directParentDependency = builder.directParentDependency;
    this.isBinaryCompatible = builder.isBinaryCompatible;
    this.isSourceCompatible = builder.isSourceCompatible;
    this.isTransitive = builder.isTransitive;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String className;
    private String memberName;
    private String changeType;
    private String description;

    private Dependency oldDependency;
    private Dependency newDependency;

    private int depth = 1; // default to direct
    private Dependency directParentDependency;

    private boolean isBinaryCompatible;
    private boolean isSourceCompatible;
    private boolean isTransitive;

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

    public Builder oldDependency(Dependency oldDependency) {
      this.oldDependency = oldDependency;
      return this;
    }

    public Builder newDependency(Dependency newDependency) {
      this.newDependency = newDependency;
      return this;
    }

    public Builder depth(int depth) {
      this.depth = depth;
      return this;
    }

    public Builder directParentDependency(Dependency directParentDependency) {
      this.directParentDependency = directParentDependency;
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

    public Builder isTransitive(boolean isTransitive) {
      this.isTransitive = isTransitive;
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

  public Dependency getOldDependency() {
    return oldDependency;
  }

  public Dependency getNewDependency() {
    return newDependency;
  }

  public int getDepth() {
    return depth;
  }

  public Dependency getDirectParentDependency() {
    return directParentDependency;
  }

  public boolean isBinaryCompatible() {
    return isBinaryCompatible;
  }

  public boolean isSourceCompatible() {
    return isSourceCompatible;
  }

  public boolean isTransitive() {
    return isTransitive;
  }

  @Override
  public String toString() {
    String oldCoords = oldDependency != null ? oldDependency.getArtifact().toString() : "null";
    String newCoords = newDependency != null ? newDependency.getArtifact().toString() : "null";
    String parent =
        directParentDependency != null ? directParentDependency.getArtifact().toString() : "null";
    return String.format(
        "BreakingChange{old='%s', new='%s', class='%s', member='%s', type='%s',"
            + " compatible=binary:%s/source:%s, transitive=%s, depth=%d, parent='%s'}",
        oldCoords,
        newCoords,
        className,
        memberName,
        changeType,
        isBinaryCompatible,
        isSourceCompatible,
        isTransitive,
        depth,
        parent);
  }
}
