package scraper;

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
import java.util.ArrayList;

public class Scraper {
    private static Logger logger = LogManager.getLogger(Scraper.class);

    /**
     * getIssueXML will download the XML version of the specified issue, and save
     * to a file called bugID.xml
     *
     * @param issue The issue which we want to get an XML version of
     * @return whether we needed to download the XML or not
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
     * extractIssueComments will extract the comments of an issue
     * from an XML document
     *
     * @param issue the issue for which we wish to extract comments
     * @return an ArrayList containing each comment on the issue
     */
    public ArrayList<String> extractIssueComments(FirefoxIssue issue) {

        ArrayList<String> comments = new ArrayList<>();
        String issueFilename = "../project-issue-data/bugreport.mozilla.firefox/issueXML/" + issue.getBugID() + ".xml";

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(issueFilename);

            // Get rid of extra line breaks in XML
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("thetext");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node n = nodeList.item(i);
                comments.add(n.getTextContent());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Extracted " + comments.size() + " comments for issue " + issue.getBugID());
        return comments;
    }


}
