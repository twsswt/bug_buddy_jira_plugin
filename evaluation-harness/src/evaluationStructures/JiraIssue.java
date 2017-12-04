package evaluationStructures;

public class JiraIssue {
    private String projectKey;
    private String summary;
    private String description;
    private String issueTypeID;
    // Need to create these users in Jira, if they don't already exist
    private String reporterName;
    private String assigneeName;

    public JiraIssue(String firefoxBugID) {
        projectKey = "";
        summary = firefoxBugID;
        description = "";
        issueTypeID = "";
        reporterName = "";
        assigneeName = "";
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIssueTypeID() {
        return issueTypeID;
    }

    public void setIssueTypeID(String issueTypeID) {
        this.issueTypeID = issueTypeID;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

}
