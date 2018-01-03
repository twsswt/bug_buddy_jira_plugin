package puller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Puller {
    private final String jiraIP;
    private final String jiraPort;
    private String jiraAPILocation;

    public Puller(String jiraIP, String jiraPort) {
        this.jiraIP = jiraIP;
        this.jiraPort = jiraPort;
        updateJiraAPILocation();
    }

    private void updateJiraAPILocation() {
        this.jiraAPILocation = "http://" + jiraIP + ":" + jiraPort + "/jira/rest/api/2";
    }

    /**
     * Gets all issues from the current jira instance
     *
     * @return an array list containing all jira issues
     */
    public ArrayList<JiraIssue> getAllIssues() {
        ArrayList<JiraIssue> issues = new ArrayList<>();

        int startAt = 0;
        boolean pulledNewIssues;

        do {
            System.out.println("Pulling block starting at: " + startAt);
            ArrayList<JiraIssue> newIssues = getIssueBlock(startAt);
            pulledNewIssues = newIssues.size() > 0;
            startAt += 100;
            issues.addAll(newIssues);
        } while (pulledNewIssues);

        return issues;
    }

    /**
     * getIssueBlock will return a chunk of 100 issues from JIRA
     * <p>
     * <p>
     * This function is required, as JIRA only allows 100 results per search
     * See: https://jira.atlassian.com/browse/JRACLOUD-67570
     *
     * @param startAt the index to start at - 0 for first batch, 100 for second, etc
     * @return An arrayList containing 100 jira issues
     */
    public ArrayList<JiraIssue> getIssueBlock(int startAt) {


        ArrayList<JiraIssue> issues = new ArrayList<>();

        String curlPrefix = "curl -i -u admin:admin -H \"Accept: application/json\" -H \"Content-Type: application/json\" -X GET ";
        String issueSearchQuery = "/search?jql=project%3D%22FRFX%22&startAt=" + String.valueOf(startAt) + "&maxResults=100";
        String curlCommand = curlPrefix + jiraAPILocation + issueSearchQuery;

        try {
            Process p = Runtime.getRuntime().exec(curlCommand);

            InputStream stdout = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            String line, returnedJSON = "";
            while ((line = reader.readLine()) != null) {
                returnedJSON = line;
            }

            JsonObject jsonContent = new JsonParser().parse(returnedJSON).getAsJsonObject();
            JsonArray jsonIssues = jsonContent.get("issues").getAsJsonArray();

            int counter = 0;
            for (JsonElement jsonIssue : jsonIssues) {
                System.out.println("Pulling issue: " + (++counter));
                issues.add(convertJsonIssueToJiraIssue(jsonIssue.getAsJsonObject()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return issues;
    }

    /**
     * Converts an issue downloaded via the REST API into an internal JiraIssue type
     */
    public JiraIssue convertJsonIssueToJiraIssue(JsonObject jsonIssue) {
        JiraIssue jiraIssue = new JiraIssue();
        JsonObject jsonFields = jsonIssue.get("fields").getAsJsonObject();

        // Get ID
        jiraIssue.id = jsonIssue.get("id").getAsString();

        // Get reporter
        JsonObject jsonReporter = jsonFields.get("reporter").getAsJsonObject();
        jiraIssue.reporter = jsonReporter.get("name").getAsString();

        // Get assignee
        JsonObject jsonAssignee = jsonFields.get("assignee").getAsJsonObject();
        jiraIssue.assignee = jsonAssignee.get("name").getAsString();


        jiraIssue = addCommentsToIssue(jiraIssue);
        return jiraIssue;
    }

    /**
     * Downloads each of the comments for an issue, and adds them to the internal representation
     */
    public JiraIssue addCommentsToIssue(JiraIssue issue) {
        JiraIssue newIssue = issue;

        String curlPrefix = "curl -i -u admin:admin -H \"Accept: application/json\" -H \"Content-Type: application/json\" -X GET ";
        String issueSearchQuery = "/issue/" + issue.id;
        String curlCommand = curlPrefix + jiraAPILocation + issueSearchQuery;

        try {
            Process p = Runtime.getRuntime().exec(curlCommand);

            InputStream stdout = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            String line, returnedJSON = "";
            while ((line = reader.readLine()) != null) {
                returnedJSON = line;
            }

            JsonObject jsonContent = new JsonParser().parse(returnedJSON).getAsJsonObject();

            JsonObject jsonFields = jsonContent.get("fields").getAsJsonObject();
            JsonObject jsonComment = jsonFields.get("comment").getAsJsonObject();
            JsonArray jsonComments = jsonComment.get("comments").getAsJsonArray();

            for (JsonElement comment : jsonComments) {
                JiraComment newComment = new JiraComment();

                String fullCommentBody = comment.getAsJsonObject().get("body").getAsString();

                String[] commentContents = fullCommentBody.split("\n", 4);
                String author = commentContents[0].replaceFirst("author: ", "");
                String date = commentContents[1].replaceFirst("created: ", "");
                String body = commentContents[3];

                newComment.author = author;
                newComment.date = date;
                newComment.body = body;

                newIssue.comments.add(newComment);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return newIssue;
    }

}
