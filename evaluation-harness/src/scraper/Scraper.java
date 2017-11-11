package scraper;

import main.FirefoxIssue;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class Scraper {

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

        StringBuilder xmlDocumentBuffer = new StringBuilder();

        File issueXMLFile = new File(outputFilename);

        if (!issueXMLFile.exists()) {
            downloadWebpageContents(issueURL, xmlDocumentBuffer);
            saveXMLToFile(xmlDocumentBuffer, issueXMLFile);
            return true;
        } else {
            return false;
        }
    }

    private void saveXMLToFile(StringBuilder xmlDocumentBuffer, File issueXMLFile) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(issueXMLFile));
            writer.write(xmlDocumentBuffer.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadWebpageContents(String issueURL, StringBuilder xmlDocumentBuffer) {
        try {
            URL url = new URL(issueURL);
            InputStream stream = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            String line;
            while ((line = br.readLine()) != null) {
                xmlDocumentBuffer.append(line);
                xmlDocumentBuffer.append("\n");
            }
            br.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        String issueFilename = "/home/stephen/bug_buddy_jira_plugin/project-issue-data/bugreport.mozilla.firefox/issueXML/" + issue.getBugID() + ".xml";

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

        return comments;
    }


}
