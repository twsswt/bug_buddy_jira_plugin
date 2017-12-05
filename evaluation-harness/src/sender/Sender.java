package sender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Sender {

    private static final String CURL_POST_PREFIX = "curl -D- -u admin:admin -X POST --data @";
    private static final String CURL_POST_MIDFIX = " -H Content-Type:application/json ";
    private static Logger logger = LogManager.getLogger(Sender.class);
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
        String returnedJSON = "";
        try {
            String curlCommand = CURL_POST_PREFIX + issueJSONLocation + filename + CURL_POST_MIDFIX + this.jiraAPILocation + apiSection;

            logger.info("Sending: " + curlCommand);
            Process p = Runtime.getRuntime().exec(curlCommand);
            p.waitFor();

            InputStream stdout = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            String line;
            while ((line = reader.readLine()) != null) {
                returnedJSON = line;
                logger.debug("Sending: " + line);
            }

            if (returnedJSON.charAt(2) == 'e') {
                logger.warn("Issue posting " + filename);
                logger.warn(returnedJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sendPostCommandExtractIssueID(String filename, String apiSection) {
        String successJSON = "";
        try {
            String curlCommand = CURL_POST_PREFIX + issueJSONLocation + filename + CURL_POST_MIDFIX + this.jiraAPILocation + apiSection;

            logger.info("Sending: " + curlCommand);
            Process p = Runtime.getRuntime().exec(curlCommand);
            p.waitFor();

            InputStream stdout = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            String line;
            while ((line = reader.readLine()) != null) {
                successJSON = line;
                logger.debug(line);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        String issueID = extractIssueIDFromSuccessJSON(successJSON);
        return issueID;
    }
}
