package sender;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Sender {

    private static final String CURL_POST_PREFIX = "curl -D- -u admin:admin -X POST --data @";
    private static final String CURL_POST_MIDFIX = " -H Content-Type:application/json ";

    private final String jiraIP;
    private final String jiraPort;
    private String jiraAPILocation;
    private String issueJSONLocation;

    public Sender(String jiraIP, String jiraPort) {
        this.jiraIP = jiraIP;
        this.jiraPort = jiraPort;
        updateJiraAPILocation();
    }

    private static String extractIssueIDFromSuccessJSON(String successJSON) {
        System.out.println(successJSON);
        String[] JSONComponents = successJSON.split(",");
        String[] idComponents = JSONComponents[0].split(":");
        String id = idComponents[1];
        id = id.replace("\"", "");

        return id;
    }

    private void updateJiraAPILocation() {
        this.jiraAPILocation = "http://" + this.jiraIP + ":" + jiraPort + "/jira/rest/api/2/";
    }

    public void setIssueJSONLocation(String issueJSONLocation) {
        this.issueJSONLocation = issueJSONLocation;
    }

    public void sendPostCommand(String filename, String apiSection) {
        try {
            String curlCommand = CURL_POST_PREFIX + issueJSONLocation + filename + CURL_POST_MIDFIX + this.jiraAPILocation + apiSection;

            System.out.println(curlCommand);
            Process p = Runtime.getRuntime().exec(curlCommand);
            p.waitFor();

            InputStream stdout = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sendPostCommandExtractIssueID(String filename, String apiSection) {
        String successJSON = "";
        try {
            String curlCommand = CURL_POST_PREFIX + issueJSONLocation + filename + CURL_POST_MIDFIX + this.jiraAPILocation + apiSection;

            System.out.println(curlCommand);
            Process p = Runtime.getRuntime().exec(curlCommand);
            p.waitFor();

            InputStream stdout = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            String line;
            while ((line = reader.readLine()) != null) {
                successJSON = line;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        String issueID = extractIssueIDFromSuccessJSON(successJSON);
        System.out.println("IssueID is:" + issueID);
        return issueID;
    }
}
