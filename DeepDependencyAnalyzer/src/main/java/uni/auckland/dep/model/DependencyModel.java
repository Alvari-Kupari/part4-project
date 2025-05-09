package uni.auckland.dep.model;

public class DependencyModel {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String classifier;

    public DependencyModel(String groupId, String artifactId, String version, String classifier){
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.classifier = classifier;
    }
    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getClassifier() {
        return classifier;
    }

    public String getDependencyName() {
        String dependencyName;
        if (classifier != null) {
            dependencyName = this.groupId + ":" + this.artifactId + ":" + this.classifier;
        } else {
            dependencyName = this.groupId + ":" + this.artifactId;
        }
        return dependencyName;
    }
}
