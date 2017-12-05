package converter;

import com.google.gson.Gson;
import evaluationStructures.FirefoxComment;
import evaluationStructures.FirefoxIssue;
import evaluationStructures.JiraIssue;
import evaluationStructures.JiraProject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Converter {
    private static final Logger logger = LogManager.getLogger(Converter.class);

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

    public String convertCommentToJiraJSON(FirefoxComment comment) {

        // Escape all quote characters in comment ( replace all " with \" )
        comment.setCommentText(comment.getCommentText().replaceAll("\"", "\\\\\""));

        // Escape all tab characters in comment
        comment.setCommentText(comment.getCommentText().replaceAll("\t", "    "));

        // Escape all newline characters in comment
        comment.setCommentText(comment.getCommentText().replaceAll("\n", "\\\\n"));

        String authorStatement = "author: " + comment.getAuthorEmail() + "\\n";
        String createdStatement = "created: " + comment.getCreationTime() + "\\n\\n";

        // Add author email and timestamp to the comment
        String jiraComment = authorStatement +
                createdStatement +
                comment.getCommentText();

        return "{\n" +
                "\"body\":\"" + jiraComment + "\"\n" +
                "}";
    }
}
