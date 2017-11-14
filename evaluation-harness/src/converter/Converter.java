package converter;

import com.google.gson.Gson;
import main.FirefoxIssue;
import main.JiraIssue;
import main.JiraProject;

public class Converter {
    private String projectKey;

    public Converter() {
        this.projectKey = "FRFX";
    }

    public Converter(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public JiraIssue convertFirefoxIssueToJiraIssue(FirefoxIssue firefoxIssue) {
        JiraIssue ji = new JiraIssue();
        ji.setProjectKey(projectKey);
        ji.setAssigneeName(firefoxIssue.getAssigneeEmail30Days());
        ji.setIssueTypeID("10000");
        ji.setReporterName(firefoxIssue.getReporterEmail());

        return ji;
    }

    public String convertJiraIssueToJiraJSON(JiraIssue jiraIssue) {
        String s = "{\n" +
                "\"fields\":{\n" +
                "\"project\":{\n" +
                "\"key\":\"" + projectKey + "\"\n" +
                "},\n" +
                "\"issuetype\":{\n" +
                "\"id\":\"" + jiraIssue.getIssueTypeID() + "\"\n" +
                "},\n" +
                "\"assignee\":{\n" +
                "\"name\":\"" + jiraIssue.getAssigneeName() + "\"\n" +
                "},\n" +
                "\"reporter\":{\n" +
                "\"name\":\"" + jiraIssue.getReporterName() + "\"\n" +
                "}\n" +
                "}\n" +
                "}";

        return s;
    }

    public String convertJiraProjectToJiraJSON(JiraProject jiraProject) {
        Gson gson = new Gson();
        return gson.toJson(jiraProject);
    }

    public String convertEmailAddressToJiraUser(String email) {
        String s = "{\n" +
                "\"name\":\"" + email + "\"\n" +
                "\"password\":\"" + email + "\"\n" +
                "\"emailAddress\":\"" + email + "\"\n" +
                "\"displayName\":\"" + email + "\"\n" +
                "}";
        return s;
    }
}
