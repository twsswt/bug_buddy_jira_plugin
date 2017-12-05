package converter;

import com.google.gson.Gson;
import evaluationStructures.FirefoxComment;
import evaluationStructures.FirefoxIssue;
import evaluationStructures.JiraIssue;
import evaluationStructures.JiraProject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class contains functions for converting from one type of Jira / Firefox object to another
 *
 * This could be from an Object to a different Object, or from an Object to a JSON string
 */
public class Converter {
    private static final Logger logger = LogManager.getLogger(Converter.class);
    private static final String DEFAULT_PROJECT_KEY = "FRFX";

    private String projectKey;

    /**
     * Creates a new converter with the default project key
     */
    public Converter() {
        this(DEFAULT_PROJECT_KEY);
    }

    /**
     * Creates a new converter with the specified project key
     * @param projectKey
     */
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

    /**
     * Converts a Firefox Issue to a Jira Issue
     * @param firefoxIssue the firefox issue to be converted
     * @return a jira issue, converted from the firefox issue
     */
    public JiraIssue convertFirefoxIssueToJiraIssue(FirefoxIssue firefoxIssue) {
        JiraIssue ji = new JiraIssue(String.valueOf(firefoxIssue.getBugID()));
        ji.setProjectKey(projectKey);
        ji.setAssigneeName(firefoxIssue.getAssigneeEmail30Days());
        ji.setIssueTypeID("10000");
        ji.setReporterName(firefoxIssue.getReporterEmail());

        return ji;
    }

    /**
     * Converts a Jira Issue to a JSON string, suitable for POSTing to Jira
     * REST API
     * @param jiraIssue the jira issue to be converted
     * @return a json string, representing the jira issue
     */
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

    /**
     * Converts a Jira Project to a JSON string, suitable for POSTing to the Jira
     * REST API
     * @param jiraProject the jira project to be converted
     * @return a json string, representing the jira project
     */
    public String convertJiraProjectToJiraJSON(JiraProject jiraProject) {
        Gson gson = new Gson();
        return gson.toJson(jiraProject);
    }

    /**
     * Converts a user to a JSON string, suitable for POSTing to the Jira
     * REST API
     * @param userEmail the user email address to be converted
     * @return a json string, representing the user
     */
    public String convertUserToJiraJSON(String userEmail) {
        return "{\n" + 
                "\"name\":\"" + userEmail + "\",\n" +
                "\"password\":\"" + userEmail + "\",\n" +
                "\"emailAddress\":\"" + userEmail + "\",\n" +
                "\"displayName\":\"" + userEmail + "\"\n" +
                "}";
    }

    /**
     * Converts a comment to a JSON string, suitable for POSTing to the Jira
     * REST API
     * @param comment the comment to be converted
     * @return a json string, representing the comment
     */
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
