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
     * @param issue The issue which we want to get an XML version of
     * @return A string containing the XML of the specified issue
     */
    public String getIssueXML(FirefoxIssue issue) {
        String issueURL = "https://bugzilla.mozilla.org/show_bug.cgi?ctype=xml&id=" + issue.getBugID();

        StringBuilder xmlDocumentBuffer = new StringBuilder();

        try {

            // Download the contents of the webpage
            URL url = new URL(issueURL);
            InputStream stream = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            String line;
            while ((line = br.readLine()) != null) {
                xmlDocumentBuffer.append(line);
                xmlDocumentBuffer.append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String xmlDocument = xmlDocumentBuffer.toString();

        String outputFilename = "/home/stephen/bug_buddy_jira_plugin/project-issue-data/bugreport.mozilla.firefox/issueXML/" + issue.getBugID() + ".xml";

        File output = new File(outputFilename);
        if (!output.exists()) {
            try {
                PrintWriter out = new PrintWriter(outputFilename);
                out.print(xmlDocumentBuffer);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return xmlDocument;
    }

    /**
     * extractIssueComments will extract the comments of an issue
     * from a specified XML document
     * @param issue the issue for which we wish to extract comments
     * @return an ArrayList containing each comment on the issue
     */
    public ArrayList<String> extractIssueComments(FirefoxIssue issue) {
        long downloadTime = 0;

        long functionTimeStart = System.currentTimeMillis();
        ArrayList<String> comments = new ArrayList<>();
        String issueURL = "https://bugzilla.mozilla.org/show_bug.cgi?ctype=xml&id=" + issue.getBugID();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            long downloadTimeStart = System.currentTimeMillis();
            Document doc = dBuilder.parse(issueURL);
            long downloadTimeFinish = System.currentTimeMillis();

            downloadTime = downloadTimeFinish - downloadTimeStart;

            // Get rid of extra line breaks in downloaded XML
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("thetext");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node n = nodeList.item(i);
                comments.add(n.getTextContent());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        long functionTimeEnd = System.currentTimeMillis();

        long functionTime = functionTimeEnd - functionTimeStart;

        double finalPercent = (downloadTime*100.0f)/functionTime;

        System.out.println("Function Time: " + functionTime);
        System.out.println("Download Time: " + downloadTime);
        System.out.printf("Percent: %.2f%%\n" ,finalPercent );
        return comments;
    }


}
