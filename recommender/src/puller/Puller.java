package puller;

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

        // Get all issues REST URL
        //
        // http://localhost:2990/jira/rest/api/2/search?jql=project%3D%22FRFX%22&startAt=49&maxResults=50
        //
        // This is returned when there are no issues left
        // {
        //   "startAt": 0,
        //   "maxResults": 50,
        //   "total": 100,
        //   "issues": []
        // }
        String curlPrefix = "curl -i -u admin:admin -H \"Accept: application/json\" -H \"Content-Type: application/json\" -X GET ";
        String issueSearchQuery = "/search?jql=project%3D%22FRFX%22&startAt=0&maxResults=50";
        String curlCommand = curlPrefix + jiraAPILocation + issueSearchQuery;

        try {
            Process p = Runtime.getRuntime().exec(curlCommand);
        
            InputStream stdout = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            String line, returnedJSON = "";
            while ((line = reader.readLine()) != null) {
                returnedJSON += line;
                returnedJSON += "\n";
            }

            System.out.println(returnedJSON);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return issues;
    }

}
