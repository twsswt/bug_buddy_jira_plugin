package sender;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    public String getIssueJSONLocation() {
        return issueJSONLocation;
    }

    public void setIssueJSONLocation(String issueJSONLocation) {
        this.issueJSONLocation = issueJSONLocation;
    }

    public String extractIssueIDFromSuccessJSON(String successJSON) {
        String[] JSONComponents = successJSON.split(",");
        String[] idComponents = JSONComponents[0].split(":");
        String id = idComponents[1];
        id = id.replace("\"", "");
        return id;
    }

    public void updateJiraAPILocation() {
        this.jiraAPILocation = "http://" + this.jiraIP + ":" + jiraPort + "/jira/rest/api/2/";
    }

    public String getJiraAPILocation() {
        return jiraAPILocation;
    }

    /**
     * sendPostCommand will send a POST command containing the specified json document, to the specified API section
     *
     * @param filename   the JSON document we wish to POST
     * @param apiSection The API section we wish to POST to
     */
    public void sendPostCommand(String filename, String apiSection) {

        String curlCommand = CURL_POST_PREFIX + issueJSONLocation + filename + CURL_POST_MIDFIX + this.jiraAPILocation + apiSection;
        String returnedJsonString = sendCurlCommand(curlCommand);

        JsonObject returnedJson = new JsonParser().parse(returnedJsonString).getAsJsonObject();
        if (returnedJson.has("errors")) {
            logger.warn("issue posting " + filename);
            logger.warn(returnedJsonString);
        }

    }

    /**
     * This function works the same as sendPostCommand, but also returns the issue ID
     * which is created by JIRA, when an issue is posted to it
     * <p>
     * The issue ID is required when POSTing comments
     *
     * @param filename   the JSON issue we wish to post
     * @param apiSection the API section we wish to post to
     * @return The newly created Issue ID
     */
    public String sendPostCommandExtractIssueID(String filename, String apiSection) {

        String curlCommand = CURL_POST_PREFIX + issueJSONLocation + filename + CURL_POST_MIDFIX + this.jiraAPILocation + apiSection;
        String successJSON = sendCurlCommand(curlCommand);

        return extractIssueIDFromSuccessJSON(successJSON);
    }

    /**
     * This function sends a curl command, and returns the JSON returned by curl
     *
     * @param curlCommand the command to send
     * @return the json returned by curl
     */
    private String sendCurlCommand(String curlCommand) {
        String returnedJSON = "";

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedJSON;
    }
}
