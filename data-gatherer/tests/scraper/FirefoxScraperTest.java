package scraper;

import evaluationStructures.FirefoxComment;
import evaluationStructures.FirefoxIssue;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class FirefoxScraperTest {

    // We want this function to run very quickly if we don't have to redownload the xml file
    @Test(timeout = 100)
    public void ensureGetIssueJsonSkipsAlreadyExistingJson() {
        FirefoxScraper s = new FirefoxScraper();
        s.setIssueJSONDataLocation("tests/test-files/");
        FirefoxIssue testIssue = new FirefoxIssue();
        testIssue.setBugID(212779);

        boolean downloaded = s.getIssueJSON(testIssue);
        assertFalse(downloaded);
    }

    @Test
    public void ensureGetIssueJsonDownloadsNotExistingJson() throws Exception {
        FirefoxScraper s = new FirefoxScraper();
        s.setIssueJSONDataLocation("tests/test-files/");
        FirefoxIssue testIssue = new FirefoxIssue();
        testIssue.setBugID(212778);

        // Delete test download file if it exists
        File jsonFile = new File("tests/test-xml-files/212778.json");
        Files.deleteIfExists(jsonFile.toPath());

        boolean downloaded = s.getIssueJSON(testIssue);
        assertTrue(downloaded);

    }

    @Test
    public void ensureSaveXMLToFileDoesntThrowException() {
        FirefoxScraper s = new FirefoxScraper();
        s.saveDataToFile("<head></head>", new File("tests/test-files/212778.xml"));
    }

    @Test
    public void ensureExtractIssueCommentsFromJsonExtractsAllComments() {
        FirefoxScraper s = new FirefoxScraper();
        s.setIssueJSONDataLocation("tests/test-files/");

        FirefoxIssue testIssue = new FirefoxIssue();
        testIssue.setBugID(212779);

        ArrayList<FirefoxComment> comments = s.extractIssueCommentsFromJSON(testIssue);
        int expectedNumComments = 87; // Manual counting
        assertEquals(expectedNumComments, comments.size());
    }

    @Test
    public void testSetIssueJsonLocation() {
        FirefoxScraper scraper = new FirefoxScraper();
        String expectedIssueJsonDataLocation = "/home/stephen/json";
        scraper.setIssueJSONDataLocation(expectedIssueJsonDataLocation);

        String actualIssueJsonDataLocation = scraper.getIssueJSONDataLocation();

        assertEquals(expectedIssueJsonDataLocation, actualIssueJsonDataLocation);
    }

    @After
    public void tearDown() throws Exception {
        File jsonFile = new File("tests/test-files/212778.json");
        Files.deleteIfExists(jsonFile.toPath());
    }
}