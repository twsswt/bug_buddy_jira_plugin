package scraper;

import main.FirefoxIssue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Scraper {

    public String getIssueXML(FirefoxIssue issue) {
        String issueURL = "https://bugzilla.mozilla.org/show_bug.cgi?ctype=xml&id=" + issue.getBugID();
        System.out.println(issueURL);

        String xmlDocument = "";

        try {
            String line;
            URL url = new URL(issueURL);
            InputStream stream = url.openStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            while ((line = br.readLine()) != null) {
                xmlDocument += line;
                xmlDocument += "\n";
            }

            System.out.println(xmlDocument);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return xmlDocument;
    }

    public void extractIssueComments(String xmlDocument) {

    }


}
