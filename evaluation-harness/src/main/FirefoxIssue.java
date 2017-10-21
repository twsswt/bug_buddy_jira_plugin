package main;

public class FirefoxIssue {
    private long bugID;
    private String component;
    private String reporterEmail;
    private String assigneeEmail;
    private String assigneeEmail30Days;

    public long getBugID() {
        return bugID;
    }

    public void setBugID(long bugID) {
        this.bugID = bugID;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public void setAssigneeEmail(String assigneeEmail) {
        this.assigneeEmail = assigneeEmail;
    }

    public String getAssigneeEmail30Days() {
        return assigneeEmail30Days;
    }

    public void setAssigneeEmail30Days(String assigneeEmail30Days) {
        this.assigneeEmail30Days = assigneeEmail30Days;
    }

    @Override
    public String toString() {
        return "FirefoxIssue{" +
                "bugID=" + bugID +
                ", component='" + component + '\'' +
                ", reporterEmail='" + reporterEmail + '\'' +
                ", assigneeEmail='" + assigneeEmail + '\'' +
                ", assigneeEmail30Days='" + assigneeEmail30Days + '\'' +
                '}';
    }

}
