package puller;

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
        return issues;
    }

}
