package scraper;

import evaluationStructures.FirefoxComment;
import evaluationStructures.FirefoxIssue;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ScraperTest {

    // We want this function to run very quickly if we don't have to redownload the xml file
    @Test(timeout = 100)
    public void ensureGetIssueXMLSkipsAlreadyExistingXML() throws Exception {
        Scraper s = new Scraper();
        FirefoxIssue testIssue = new FirefoxIssue();
        testIssue.setBugID(212779);

        boolean downloaded = s.getIssueXML(testIssue, "tests/test-xml-files/");
        assertFalse(downloaded);
    }

    @Test
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

    @Test
    public void ensureSaveXMLToFileDoesntThrowException() throws Exception {
        Scraper s = new Scraper();
        s.saveDataToFile("<head></head>", new File("tests/test-xml-files/212778.xml"));
    }

    @Test
    public void ensureExtractIssueCommentsFromXMLExtractsAllComments() throws Exception {
        Scraper s = new Scraper();
        s.setIssueXMLDataLocation("tests/test-xml-files/");

        FirefoxIssue testIssue = new FirefoxIssue();
        testIssue.setBugID(212779);

        ArrayList<FirefoxComment> comments = s.extractIssueCommentsFromXML(testIssue);
        int expectedNumComments = 87; // Manual counting is a pain
        assertEquals(expectedNumComments, comments.size());
    }

    @Test
    public void ensureExtractIssueCommentsFromJsonExtractsAllComments() throws Exception {
        Scraper s = new Scraper();
        s.setIssueJSONDataLocation("tests/test-xml-files/");

        FirefoxIssue testIssue = new FirefoxIssue();
        testIssue.setBugID(212779);

        ArrayList<FirefoxComment> comments = s.extractIssueCommentsFromJSON(testIssue);
        int expectedNumComments = 87; // Manual counting
        assertEquals(expectedNumComments, comments.size());
    }


    @After
    public void tearDown() throws Exception {
        // Delete test download file if it exists
        File xmlFile = new File("tests/test-xml-files/212778.xml");
        Files.deleteIfExists(xmlFile.toPath());
    }
}