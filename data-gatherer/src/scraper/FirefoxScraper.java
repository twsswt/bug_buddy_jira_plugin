package scraper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import evaluationStructures.FirefoxComment;
import evaluationStructures.FirefoxIssue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class is responsible for downloading data from a specified URL, and feeding it
 * into an appropriate data structure (see package evaluationStructures)
 */
public class FirefoxScraper {

    private static final String DEFAULT_FIREFOX_ISSUE_JSON_LOCATION = "project-issue-data/bugreport.mozilla.firefox/FirefoxIssueJSON/";
    private static final Logger logger = LogManager.getLogger(FirefoxScraper.class);
    private String issueJSONDataLocation;

    /**
     * Creates a scraper using default locations for the issue XML and JSON locations
     */
    public FirefoxScraper() {
        issueJSONDataLocation = DEFAULT_FIREFOX_ISSUE_JSON_LOCATION;
    }

    public String getIssueJSONDataLocation() {
        return issueJSONDataLocation;
    }

    public void setIssueJSONDataLocation(String issueJSONDataLocation) {
        this.issueJSONDataLocation = issueJSONDataLocation;
    }

    /**
     * getIssueJSON will download the JSON version of the specified issue, and save
     * it to a file called bugID.json
     *
     * @param issue The issue which we want to get an XML version of
     */
    public boolean getIssueJSON(FirefoxIssue issue) {

        String outputFilename = issueJSONDataLocation + issue.getBugID() + ".json";
        File issueJSONFile = new File(outputFilename);

        if (issueJSONFile.exists()) {

            logger.info("Skipped Downloading JSON for issue " + issue.getBugID());
            return false;

        } else {

            String issueURL = "https://bugzilla.mozilla.org/rest/bug/" + issue.getBugID() + "/comment";

            try {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            logger.info("Downloaded JSON for issue " + issue.getBugID());
            return true;
        }
    }

    /**
     * saveDataToFile will save the contents of a string to the specified file
     *
     * @param data     the data we wish to save
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

    /**
     * extractIssueCommentsFromJSON will extract the comments of an issue
     * from a JSON document
     *
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
