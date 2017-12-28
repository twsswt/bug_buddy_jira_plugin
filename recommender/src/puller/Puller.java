package puller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
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

    public ArrayList<JiraIssue> getAllIssues() {
        ArrayList<JiraIssue> issues = new ArrayList<>();

        int startAt = 0;
        boolean pulledNewIssues;

        do {
            ArrayList<JiraIssue> newIssues = getIssueBlock(startAt);
            pulledNewIssues = newIssues.size() > 0;
            startAt += 100;
            issues.addAll(newIssues);
        } while (pulledNewIssues);

        return issues;
    }

    /**
     * getIssueBlock will return a chunk of 100 issues from JIRA
     *
     *
     * This function is required, as JIRA only allows 100 results per search
     * See: https://jira.atlassian.com/browse/JRACLOUD-67570
     * @param startAt the index to start at - 0 for first batch, 100 for second, etc
     * @return An arrayList containing 100 jira issues
     */
    public ArrayList<JiraIssue> getIssueBlock(int startAt) {

        // This is returned when there are no issues left
        // {
        //   "startAt": 0,
        //   "maxResults": 50,
        //   "total": 100,
        //   "issues": []
        // }
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

            for (JsonElement jsonIssue : jsonIssues) {
                issues.add(convertJsonIssueToJiraIssue(jsonIssue.getAsJsonObject()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return issues;
    }

    public JiraIssue convertJsonIssueToJiraIssue(JsonObject jsonIssue) {
        JiraIssue jiraIssue = new JiraIssue();

        return jiraIssue;
    }

}
