package uni.auckland.dep.db.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "current_dep_notransitive", schema = "dependency_analysis", catalog = "")
public class CurrentDepNotransitive {
    private int currentDepNotransitiveId;
    private Integer mvnProjectDetailNotransitiveId;
    private String dependencyName;
    private String currentVersion;
    private String latestVersions;
    private String breakingVersion;
    private String dependencyScope;
    private String classifier;
    private String updateLevel;
    private String breakingLevel;
    private Integer updateNumber;
    private String cntDepDownloaded;
    private String lstDepDownloaded;
    private String cntDepName;
    private String lstDepName;
    private String depSrcDir;
    private String ranStaticAnalysis;
    private String testSuiteStatus;
    private String testSuiteStatus1;
    private String dependencyUpdateStaticAnalysisImpact;
    private String versionValidity;
    private Integer testFailingTimes;
    private Integer testFailingTimes1;
    private String comment;
    private String ranTillPassed;
    private String invalidRecord;
    private String rerunDepUpdate;
    private String breakingReason;
    private String breakingReasonBackup;
    private String breakingCategories;
    private String breakingCategoriesBackup;
    private String breakingCategoriesWithLines;
    private String ranFailingTestAllVersions;
    private String extractedTestLogs;
    private Timestamp createdTimestamp;
    private Timestamp updatedTimestamp;

    @Id
    @Column(name = "current_dep_notransitive_id")
    public int getCurrentDepNotransitiveId() {
        return currentDepNotransitiveId;
    }

    public void setCurrentDepNotransitiveId(int currentDepNotransitiveId) {
        this.currentDepNotransitiveId = currentDepNotransitiveId;
    }

    @Basic
    @Column(name = "mvn_project_detail_notransitive_id")
    public Integer getMvnProjectDetailNotransitiveId() {
        return mvnProjectDetailNotransitiveId;
    }

    public void setMvnProjectDetailNotransitiveId(Integer mvnProjectDetailNotransitiveId) {
        this.mvnProjectDetailNotransitiveId = mvnProjectDetailNotransitiveId;
    }

    @Basic
    @Column(name = "dependency_name")
    public String getDependencyName() {
        return dependencyName;
    }

    public void setDependencyName(String dependencyName) {
        this.dependencyName = dependencyName;
    }

    @Basic
    @Column(name = "current_version")
    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    @Basic
    @Column(name = "latest_versions")
    public String getLatestVersions() {
        return latestVersions;
    }

    public void setLatestVersions(String latestVersions) {
        this.latestVersions = latestVersions;
    }

    @Basic
    @Column(name = "breaking_version")
    public String getBreakingVersion() {
        return breakingVersion;
    }

    public void setBreakingVersion(String breakingVersion) {
        this.breakingVersion = breakingVersion;
    }

    @Basic
    @Column(name = "dependency_scope")
    public String getDependencyScope() {
        return dependencyScope;
    }

    public void setDependencyScope(String dependencyScope) {
        this.dependencyScope = dependencyScope;
    }

    @Basic
    @Column(name = "classifier")
    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    @Basic
    @Column(name = "update_level")
    public String getUpdateLevel() {
        return updateLevel;
    }

    public void setUpdateLevel(String updateLevel) {
        this.updateLevel = updateLevel;
    }

    @Basic
    @Column(name = "breaking_level")
    public String getBreakingLevel() {
        return breakingLevel;
    }

    public void setBreakingLevel(String breakingLevel) {
        this.breakingLevel = breakingLevel;
    }

    @Basic
    @Column(name = "update_number")
    public Integer getUpdateNumber() {
        return updateNumber;
    }

    public void setUpdateNumber(Integer updateNumber) {
        this.updateNumber = updateNumber;
    }

    @Basic
    @Column(name = "cnt_dep_downloaded")
    public String getCntDepDownloaded() {
        return cntDepDownloaded;
    }

    public void setCntDepDownloaded(String cntDepDownloaded) {
        this.cntDepDownloaded = cntDepDownloaded;
    }

    @Basic
    @Column(name = "lst_dep_downloaded")
    public String getLstDepDownloaded() {
        return lstDepDownloaded;
    }

    public void setLstDepDownloaded(String lstDepDownloaded) {
        this.lstDepDownloaded = lstDepDownloaded;
    }

    @Basic
    @Column(name = "cnt_dep_name")
    public String getCntDepName() {
        return cntDepName;
    }

    public void setCntDepName(String cntDepName) {
        this.cntDepName = cntDepName;
    }

    @Basic
    @Column(name = "lst_dep_name")
    public String getLstDepName() {
        return lstDepName;
    }

    public void setLstDepName(String lstDepName) {
        this.lstDepName = lstDepName;
    }

    @Basic
    @Column(name = "dep_src_dir")
    public String getDepSrcDir() {
        return depSrcDir;
    }

    public void setDepSrcDir(String depSrcDir) {
        this.depSrcDir = depSrcDir;
    }

    @Basic
    @Column(name = "ran_static_analysis")
    public String getRanStaticAnalysis() {
        return ranStaticAnalysis;
    }

    public void setRanStaticAnalysis(String ranStaticAnalysis) {
        this.ranStaticAnalysis = ranStaticAnalysis;
    }

    @Basic
    @Column(name = "test_suite_status")
    public String getTestSuiteStatus() {
        return testSuiteStatus;
    }

    public void setTestSuiteStatus(String testSuiteStatus) {
        this.testSuiteStatus = testSuiteStatus;
    }

    @Basic
    @Column(name = "test_suite_status1")
    public String getTestSuiteStatus1() {
        return testSuiteStatus1;
    }

    public void setTestSuiteStatus1(String testSuiteStatus1) {
        this.testSuiteStatus1 = testSuiteStatus1;
    }

    @Basic
    @Column(name = "dependency_update_static_analysis_impact")
    public String getDependencyUpdateStaticAnalysisImpact() {
        return dependencyUpdateStaticAnalysisImpact;
    }

    public void setDependencyUpdateStaticAnalysisImpact(String dependencyUpdateStaticAnalysisImpact) {
        this.dependencyUpdateStaticAnalysisImpact = dependencyUpdateStaticAnalysisImpact;
    }

    @Basic
    @Column(name = "version_validity")
    public String getVersionValidity() {
        return versionValidity;
    }

    public void setVersionValidity(String versionValidity) {
        this.versionValidity = versionValidity;
    }

    @Basic
    @Column(name = "test_failing_times")
    public Integer getTestFailingTimes() {
        return testFailingTimes;
    }

    public void setTestFailingTimes(Integer testFailingTimes) {
        this.testFailingTimes = testFailingTimes;
    }

    @Basic
    @Column(name = "test_failing_times1")
    public Integer getTestFailingTimes1() {
        return testFailingTimes1;
    }

    public void setTestFailingTimes1(Integer testFailingTimes1) {
        this.testFailingTimes1 = testFailingTimes1;
    }

    @Basic
    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "ran_till_passed")
    public String getRanTillPassed() {
        return ranTillPassed;
    }

    public void setRanTillPassed(String ranTillPassed) {
        this.ranTillPassed = ranTillPassed;
    }

    @Basic
    @Column(name = "invalid_record")
    public String getInvalidRecord() {
        return invalidRecord;
    }

    public void setInvalidRecord(String invalidRecord) {
        this.invalidRecord = invalidRecord;
    }

    @Basic
    @Column(name = "rerun_dep_update")
    public String getRerunDepUpdate() {
        return rerunDepUpdate;
    }

    public void setRerunDepUpdate(String rerunDepUpdate) {
        this.rerunDepUpdate = rerunDepUpdate;
    }

    @Basic
    @Column(name = "breaking_reason")
    public String getBreakingReason() {
        return breakingReason;
    }

    public void setBreakingReason(String breakingReason) {
        this.breakingReason = breakingReason;
    }

    @Basic
    @Column(name = "breaking_reason_backup")
    public String getBreakingReasonBackup() {
        return breakingReasonBackup;
    }

    public void setBreakingReasonBackup(String breakingReasonBackup) {
        this.breakingReasonBackup = breakingReasonBackup;
    }

    @Basic
    @Column(name = "breaking_categories")
    public String getBreakingCategories() {
        return breakingCategories;
    }

    public void setBreakingCategories(String breakingCategories) {
        this.breakingCategories = breakingCategories;
    }

    @Basic
    @Column(name = "breaking_categories_backup")
    public String getBreakingCategoriesBackup() {
        return breakingCategoriesBackup;
    }

    public void setBreakingCategoriesBackup(String breakingCategoriesBackup) {
        this.breakingCategoriesBackup = breakingCategoriesBackup;
    }

    @Basic
    @Column(name = "breaking_categories_with_lines")
    public String getBreakingCategoriesWithLines() {
        return breakingCategoriesWithLines;
    }

    public void setBreakingCategoriesWithLines(String breakingCategoriesWithLines) {
        this.breakingCategoriesWithLines = breakingCategoriesWithLines;
    }

    @Basic
    @Column(name = "ran_failing_test_all_versions")
    public String getRanFailingTestAllVersions() {
        return ranFailingTestAllVersions;
    }

    public void setRanFailingTestAllVersions(String ranFailingTestAllVersions) {
        this.ranFailingTestAllVersions = ranFailingTestAllVersions;
    }

    @Basic
    @Column(name = "extracted_test_logs")
    public String getExtractedTestLogs() {
        return extractedTestLogs;
    }

    public void setExtractedTestLogs(String extractedTestLogs) {
        this.extractedTestLogs = extractedTestLogs;
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
        CurrentDepNotransitive that = (CurrentDepNotransitive) o;
        return currentDepNotransitiveId == that.currentDepNotransitiveId && Objects.equals(mvnProjectDetailNotransitiveId, that.mvnProjectDetailNotransitiveId) && Objects.equals(dependencyName, that.dependencyName) && Objects.equals(currentVersion, that.currentVersion) && Objects.equals(latestVersions, that.latestVersions) && Objects.equals(breakingVersion, that.breakingVersion) && Objects.equals(dependencyScope, that.dependencyScope) && Objects.equals(classifier, that.classifier) && Objects.equals(updateLevel, that.updateLevel) && Objects.equals(breakingLevel, that.breakingLevel) && Objects.equals(updateNumber, that.updateNumber) && Objects.equals(cntDepDownloaded, that.cntDepDownloaded) && Objects.equals(lstDepDownloaded, that.lstDepDownloaded) && Objects.equals(cntDepName, that.cntDepName) && Objects.equals(lstDepName, that.lstDepName) && Objects.equals(depSrcDir, that.depSrcDir) && Objects.equals(ranStaticAnalysis, that.ranStaticAnalysis) && Objects.equals(testSuiteStatus, that.testSuiteStatus) && Objects.equals(testSuiteStatus1, that.testSuiteStatus1) && Objects.equals(dependencyUpdateStaticAnalysisImpact, that.dependencyUpdateStaticAnalysisImpact) && Objects.equals(versionValidity, that.versionValidity) && Objects.equals(testFailingTimes, that.testFailingTimes) && Objects.equals(testFailingTimes1, that.testFailingTimes1) && Objects.equals(comment, that.comment) && Objects.equals(ranTillPassed, that.ranTillPassed) && Objects.equals(invalidRecord, that.invalidRecord) && Objects.equals(rerunDepUpdate, that.rerunDepUpdate) && Objects.equals(breakingReason, that.breakingReason) && Objects.equals(breakingReasonBackup, that.breakingReasonBackup) && Objects.equals(breakingCategories, that.breakingCategories) && Objects.equals(breakingCategoriesBackup, that.breakingCategoriesBackup) && Objects.equals(breakingCategoriesWithLines, that.breakingCategoriesWithLines) && Objects.equals(ranFailingTestAllVersions, that.ranFailingTestAllVersions) && Objects.equals(extractedTestLogs, that.extractedTestLogs) && Objects.equals(createdTimestamp, that.createdTimestamp) && Objects.equals(updatedTimestamp, that.updatedTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentDepNotransitiveId, mvnProjectDetailNotransitiveId, dependencyName, currentVersion, latestVersions, breakingVersion, dependencyScope, classifier, updateLevel, breakingLevel, updateNumber, cntDepDownloaded, lstDepDownloaded, cntDepName, lstDepName, depSrcDir, ranStaticAnalysis, testSuiteStatus, testSuiteStatus1, dependencyUpdateStaticAnalysisImpact, versionValidity, testFailingTimes, testFailingTimes1, comment, ranTillPassed, invalidRecord, rerunDepUpdate, breakingReason, breakingReasonBackup, breakingCategories, breakingCategoriesBackup, breakingCategoriesWithLines, ranFailingTestAllVersions, extractedTestLogs, createdTimestamp, updatedTimestamp);
    }
}
