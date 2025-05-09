package uni.auckland.dep.db.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "mvn_project_details_notransitive", schema = "dependency_analysis", catalog = "")
public class MvnProjectDetailsNotransitive {
    private int mvnProjectDetailNotransitiveId;
    private String projectName;
    private String directory;
    private String buildStatus;
    private String javaVersion;
    private String parentProject;
    private String folderName;
    private String repositoryUrl;
    private String hasSrcDir;
    private String hasTestSuite;
    private String hasTestSuite1;
    private Short staticAnalysis;
    private Short staticAnalysis1;
    private Timestamp createdTimestamp;
    private Timestamp updatedTimestamp;

    @Id
    @Column(name = "mvn_project_detail_notransitive_id")
    public int getMvnProjectDetailNotransitiveId() {
        return mvnProjectDetailNotransitiveId;
    }

    public void setMvnProjectDetailNotransitiveId(int mvnProjectDetailNotransitiveId) {
        this.mvnProjectDetailNotransitiveId = mvnProjectDetailNotransitiveId;
    }

    @Basic
    @Column(name = "project_name")
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Basic
    @Column(name = "directory")
    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    @Basic
    @Column(name = "build_status")
    public String getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(String buildStatus) {
        this.buildStatus = buildStatus;
    }

    @Basic
    @Column(name = "java_version")
    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    @Basic
    @Column(name = "parent_project")
    public String getParentProject() {
        return parentProject;
    }

    public void setParentProject(String parentProject) {
        this.parentProject = parentProject;
    }

    @Basic
    @Column(name = "folder_name")
    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Basic
    @Column(name = "repository_url")
    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    @Basic
    @Column(name = "has_src_dir")
    public String getHasSrcDir() {
        return hasSrcDir;
    }

    public void setHasSrcDir(String hasSrcDir) {
        this.hasSrcDir = hasSrcDir;
    }

    @Basic
    @Column(name = "has_test_suite")
    public String getHasTestSuite() {
        return hasTestSuite;
    }

    public void setHasTestSuite(String hasTestSuite) {
        this.hasTestSuite = hasTestSuite;
    }

    @Basic
    @Column(name = "has_test_suite1")
    public String getHasTestSuite1() {
        return hasTestSuite1;
    }

    public void setHasTestSuite1(String hasTestSuite1) {
        this.hasTestSuite1 = hasTestSuite1;
    }

    @Basic
    @Column(name = "static_analysis")
    public Short getStaticAnalysis() {
        return staticAnalysis;
    }

    public void setStaticAnalysis(Short staticAnalysis) {
        this.staticAnalysis = staticAnalysis;
    }

    @Basic
    @Column(name = "static_analysis1")
    public Short getStaticAnalysis1() {
        return staticAnalysis1;
    }

    public void setStaticAnalysis1(Short staticAnalysis1) {
        this.staticAnalysis1 = staticAnalysis1;
    }

    @Basic
    @Column(name = "created_timestamp")
    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @Basic
    @Column(name = "updated_timestamp")
    public Timestamp getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(Timestamp updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MvnProjectDetailsNotransitive that = (MvnProjectDetailsNotransitive) o;
        return mvnProjectDetailNotransitiveId == that.mvnProjectDetailNotransitiveId && Objects.equals(projectName, that.projectName) && Objects.equals(directory, that.directory) && Objects.equals(buildStatus, that.buildStatus) && Objects.equals(javaVersion, that.javaVersion) && Objects.equals(parentProject, that.parentProject) && Objects.equals(folderName, that.folderName) && Objects.equals(repositoryUrl, that.repositoryUrl) && Objects.equals(hasSrcDir, that.hasSrcDir) && Objects.equals(hasTestSuite, that.hasTestSuite) && Objects.equals(hasTestSuite1, that.hasTestSuite1) && Objects.equals(staticAnalysis, that.staticAnalysis) && Objects.equals(staticAnalysis1, that.staticAnalysis1) && Objects.equals(createdTimestamp, that.createdTimestamp) && Objects.equals(updatedTimestamp, that.updatedTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mvnProjectDetailNotransitiveId, projectName, directory, buildStatus, javaVersion, parentProject, folderName, repositoryUrl, hasSrcDir, hasTestSuite, hasTestSuite1, staticAnalysis, staticAnalysis1, createdTimestamp, updatedTimestamp);
    }
}
