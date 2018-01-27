package scraper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import evaluationStructures.FirefoxComment;
import evaluationStructures.FirefoxIssue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class is responsible for downloading data from a specified URL, and feeding it
 * into an appropriate data structure (see package evaluationStructures)
 */
public class FirefoxScraper {
    private static final String DEFAULT_FIREFOX_ISSUE_XML_LOCATION = "../project-issue-data/bugreport.mozilla.firefox/FirefoxIssueXML/";
    private static final String DEFAULT_FIREFOX_ISSUE_JSON_LOCATION = "../project-issue-data/bugreport.mozilla.firefox/FirefoxIssueJSON/";
    private static final Logger logger = LogManager.getLogger(FirefoxScraper.class);
    private String issueXMLDataLocation;
    private String issueJSONDataLocation;

    /**
     * Creates a scraper using default locations for the issue XML and JSON locations
     */
    public FirefoxScraper() {
        issueXMLDataLocation = DEFAULT_FIREFOX_ISSUE_XML_LOCATION;
        issueJSONDataLocation = DEFAULT_FIREFOX_ISSUE_JSON_LOCATION;
    }

    public String getIssueXMLDataLocation() {
        return issueXMLDataLocation;
    }

    public void setIssueXMLDataLocation(String issueXMLDataLocation) {
        this.issueXMLDataLocation = issueXMLDataLocation;
    }

    public String getIssueJSONDataLocation() {
        return issueJSONDataLocation;
    }

    public void setIssueJSONDataLocation(String issueJSONDataLocation) {
        this.issueJSONDataLocation = issueJSONDataLocation;
    }

    /**
     * getIssueXML will download the XML version of the specified issue, and save
     * to a file called bugID.xml
     *
     * @param issue The issue which we want to get an XML version of
     * @param xmlRootFolder where we want to store the downloaded xml
     * @return whether we needed to download the XML or not //TODO investigate if this is necessary...
     */
    public boolean getIssueXML(FirefoxIssue issue, String xmlRootFolder) {
        String issueURL = "https://bugzilla.mozilla.org/show_bug.cgi?ctype=xml&id=" + issue.getBugID();
        String outputFilename = xmlRootFolder + issue.getBugID() + ".xml";

        File issueXMLFile = new File(outputFilename);

        if (!issueXMLFile.exists()) {
            StringBuilder xmlDocumentBuffer = new StringBuilder();
            String xmlDocument = downloadPageContents(issueURL, xmlDocumentBuffer);
            saveDataToFile(xmlDocument, issueXMLFile);
            logger.info("Downloaded XML for issue " + issue.getBugID());
            return true;
        } else {
            logger.info("Skipped downloading XML for issue " + issue.getBugID());
            return false;
        }
    }

    /**
     * getIssueJSON will download the JSON version of the specified issue, and save
     * it to a file called bugID.json
     *
     * @param issue The issue which we want to get an XML version of
     * @param jsonRootFolder where we want to store the downloaded json
     */
    public void getIssueJSON(FirefoxIssue issue, String jsonRootFolder) {
        try {

            String outputFilename = jsonRootFolder + issue.getBugID() + ".json";
            File issueJSONFile = new File(outputFilename);

            if (!issueJSONFile.exists()) {
                String issueURL = "https://bugzilla.mozilla.org/rest/bug/" + issue.getBugID() + "/comment";


                Process p = Runtime.getRuntime().exec("curl " + issueURL);

                InputStream stdout = p.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
                StringBuilder jsonDocument = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonDocument.append(line);
                }

                p.waitFor();
                saveDataToFile(jsonDocument.toString(), issueJSONFile);
                logger.info("Downloaded JSON for issue " + issue.getBugID());
            } else {
                logger.info("Skipped Downloading JSON for issue " + issue.getBugID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * saveDataToFile will save the contents of a string to the specified file
     * @param data the data we wish to save
     * @param filename the file we wish to save to
     */
    public void saveDataToFile(String data, File filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String downloadPageContents(String issueURL, StringBuilder xmlDocumentBuffer) {
        try {
            // Open the page for reading
            URL url = new URL(issueURL);
            InputStream stream = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            // Read the page line by line
            String line;
            while ((line = br.readLine()) != null) {
                xmlDocumentBuffer.append(line);
                xmlDocumentBuffer.append("\n");
            }

            // Close resources
            br.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return xmlDocumentBuffer.toString();
    }

    /**
     * extractIssueCommentsFromXML will extract the comments of an issue
     * from an XML document
     *
     * @param issue the issue for which we wish to extract comments
     * @return an ArrayList containing each comment on the issue
     */
    public ArrayList<FirefoxComment> extractIssueCommentsFromXML(FirefoxIssue issue) {

        ArrayList<FirefoxComment> comments = new ArrayList<>();
        String issueFilename = issueXMLDataLocation + issue.getBugID() + ".xml";

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(issueFilename);

            // Get rid of extra line breaks in XML
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("thetext");
            for (int i = 0; i < nodeList.getLength(); i++) {
                FirefoxComment comment = new FirefoxComment();
                Node n = nodeList.item(i);
                comment.setCommentText(n.getTextContent());
                comments.add(comment);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Extracted " + comments.size() + " comments for issue " + issue.getBugID());
        return comments;
    }

    /**
     * extractIssueCommentsFromJSON will extract the comments of an issue
     * from a JSON document
     * @param issue the issue for which we wish to extract comments
     * @return A list containing each comment on the issue
     */
    public ArrayList<FirefoxComment> extractIssueCommentsFromJSON(FirefoxIssue issue) {
        ArrayList<FirefoxComment> comments = new ArrayList<>();
        String issueFilename = issueJSONDataLocation + issue.getBugID() + ".json";

        String bugIDString = String.valueOf(issue.getBugID());

        try {
            // Read json from file
            String rawJsonContent = new String(Files.readAllBytes(Paths.get(issueFilename)));
            JsonObject jsonContent = new JsonParser().parse(rawJsonContent).getAsJsonObject();

            JsonObject jsonBug = jsonContent.get("bugs").getAsJsonObject();
            JsonObject jsonBugID = jsonBug.get(bugIDString).getAsJsonObject();
            JsonArray jsonComments = jsonBugID.get("comments").getAsJsonArray();


            for (int i = 0; i < jsonComments.size(); i++) {
                FirefoxComment firefoxComment = new FirefoxComment();
                JsonObject jsonComment = jsonComments.get(i).getAsJsonObject();
                firefoxComment.setCommentText(jsonComment.get("raw_text").getAsString());
                firefoxComment.setAuthorEmail(jsonComment.get("author").getAsString());
                firefoxComment.setCreationTime(jsonComment.get("creation_time").getAsString());
                comments.add(firefoxComment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Extracted " + comments.size() + " comments for issue " + issue.getBugID());
        return comments;
    }


}
