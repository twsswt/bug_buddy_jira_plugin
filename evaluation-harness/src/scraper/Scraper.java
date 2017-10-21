package scraper;

import main.FirefoxIssue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Scraper {

    /**
     * getIssueXML will download the XML version of the specified issue
     * @param issue The issue which we want to get an XML version of
     * @return A string containing the XML of the specified issue
     */
    public String getIssueXML(FirefoxIssue issue) {
        String issueURL = "https://bugzilla.mozilla.org/show_bug.cgi?ctype=xml&id=" + issue.getBugID();

        StringBuilder xmlDocument = new StringBuilder();

        try {

            // Download the contents of the webpage
            URL url = new URL(issueURL);
            InputStream stream = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            String line;
            while ((line = br.readLine()) != null) {
                xmlDocument.append(line);
                xmlDocument.append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return xmlDocument.toString();
    }

    /**
     * extractIssueComments will extract the comments of an issue
     * from a specified XML document
     * @param xmlDocument the XML document from which we wish to extract comments
     * @return an ArrayList containing each comment on the issue
     */
    public ArrayList<String> extractIssueComments(String xmlDocument) {
        ArrayList<String> comments = new ArrayList<String>();
        comments.add(xmlDocument);
        return comments;
    }


}
