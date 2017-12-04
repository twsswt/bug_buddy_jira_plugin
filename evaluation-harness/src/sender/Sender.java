package sender;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Sender {

    public static final String DEFAULT_JIRA_IP = "localhost";
    public static final String DEFAULT_JIRA_PORT = "2990";

    private static final String CURL_POST_PREFIX = "curl -D- -u admin:admin -X POST --data @";
    private static final String CURL_POST_MIDFIX = " -H Content-Type:application/json http://localhost:2990/jira/rest/api/2/";

    private String jiraIP;
    private String jiraPort;
    private String jiraAPILocation;
    private String issueJSONLocation;

    public Sender() {
        this(DEFAULT_JIRA_IP, DEFAULT_JIRA_PORT);
    }

    public Sender(String jiraIP, String jiraPort) {
        this.jiraIP = jiraIP;
        this.jiraPort = jiraPort;
        this.jiraAPILocation = "http://" + jiraIP + ":" + jiraPort + "/jira/rest/api/2/";
    }

    public void setIssueJSONLocation(String issueJSONLocation) {
        this.issueJSONLocation = issueJSONLocation;
    }

    public void sendPostCommand(String filename, String apiSection) {
        try {
            String curlCommand = CURL_POST_PREFIX + issueJSONLocation + filename + CURL_POST_MIDFIX + apiSection;

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
}
