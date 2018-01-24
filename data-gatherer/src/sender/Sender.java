package sender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Sender is responsible for POSTing JSON documents to the JIRA instance
 */
public class Sender {

    private static final String CURL_POST_PREFIX = "curl -D- -u admin:admin -X POST --data @";
    private static final String CURL_POST_MIDFIX = " -H Content-Type:application/json ";
    private static final Logger logger = LogManager.getLogger(Sender.class);
    private final String jiraIP;
    private final String jiraPort;
    private String jiraAPILocation;
    private String issueJSONLocation;

    /**
     * Create a sender which sends to the specified IP and Port
     *
     * @param jiraIP   The IP address of the JIRA instance
     * @param jiraPort the port of the JIRA instance
     */
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

    /**
     * sendPostCommand will send a POST command containing the specified json document, to the specified API section
     * @param filename the JSON document we wish to POST
     * @param apiSection The API section we wish to POST to
     */
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

    /**
     * This function works the same as sendPostCommand, but also returns the issue ID
     * which is created by JIRA, when an issue is posted to it
     *
     * The issue ID is required when POSTing comments
     * @param filename the JSON issue we wish to post
     * @param apiSection the API section we wish to post to
     * @return The newly created Issue ID
     */
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
        return extractIssueIDFromSuccessJSON(successJSON);
    }
}
