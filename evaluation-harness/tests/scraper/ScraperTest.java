package scraper;

import main.FirefoxIssue;
import org.junit.After;


import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class ScraperTest {

    // We want this function to run very quickly if we don't have to redownload the xml file
    @org.junit.Test(timeout=100)
    public void ensureGetIssueXMLSkipsAlreadyExistingXML() throws Exception {
        Scraper s = new Scraper();
        FirefoxIssue testIssue = new FirefoxIssue();
        testIssue.setBugID(212779);

        boolean downloaded = s.getIssueXML(testIssue, "tests/test-xml-files/");
        assertFalse(downloaded);
    }

    @org.junit.Test
    public void ensureGetIssueXMLDownloadsNotExistingXML() throws Exception {
        Scraper s = new Scraper();
        FirefoxIssue testIssue = new FirefoxIssue();
        testIssue.setBugID(212778);

        // Delete test download file if it exists
        File xmlFile = new File("tests/test-xml-files/212778.xml");
        Files.deleteIfExists(xmlFile.toPath());

        boolean downloaded = s.getIssueXML(testIssue, "tests/test-xml-files/");
        assertTrue(downloaded);

    }

    @org.junit.Test
    public void extractIssueComments() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        // Delete test download file if it exists
        File xmlFile = new File("tests/test-xml-files/212778.xml");
        Files.deleteIfExists(xmlFile.toPath());
    }
}