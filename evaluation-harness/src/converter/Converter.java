package converter;

import com.google.gson.Gson;
import evaluationStructures.FirefoxIssue;
import evaluationStructures.JiraIssue;
import evaluationStructures.JiraProject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Converter {
    private static Logger logger = LogManager.getLogger(Converter.class);

    private String projectKey;

    public Converter() {
        this("FRFX");
    }

    public Converter(String projectKey) {
        this.projectKey = projectKey;
        logger.info("Converter for project " + this.projectKey + " created");
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public JiraIssue convertFirefoxIssueToJiraIssue(FirefoxIssue firefoxIssue) {
        JiraIssue ji = new JiraIssue(String.valueOf(firefoxIssue.getBugID()));
        ji.setProjectKey(projectKey);
        ji.setAssigneeName(firefoxIssue.getAssigneeEmail30Days());
        ji.setIssueTypeID("10000");
        ji.setReporterName(firefoxIssue.getReporterEmail());

        return ji;
    }

    public String convertJiraIssueToJiraJSON(JiraIssue jiraIssue) {

        return "{\n" +
                "\"fields\":{\n" +
                "\"project\":{\n" +
                "\"key\":\"" + projectKey + "\"\n" +
                "},\n" +
                "\"summary\": \"" + jiraIssue.getSummary() + "\",\n" +
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
    }

    public String convertJiraProjectToJiraJSON(JiraProject jiraProject) {
        Gson gson = new Gson();
        return gson.toJson(jiraProject);
    }

    public String convertEmailAddressToJiraUser(String email) {
        return "{\n" +
                "\"name\":\"" + email + "\",\n" +
                "\"password\":\"" + email + "\",\n" +
                "\"emailAddress\":\"" + email + "\",\n" +
                "\"displayName\":\"" + email + "\"\n" +
                "}";
    }

    public String convertCommentToJiraJSON(String comment) {
        return "{\n" +
                "\"body\":\"" + comment + "\"\n" +
                "}";
    }
}
