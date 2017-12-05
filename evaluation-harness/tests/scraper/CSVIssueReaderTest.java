package scraper;

import evaluationStructures.FirefoxIssue;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CSVIssueReaderTest {
    @Test
    public void ensureReadIssuesFromCSVReadsSingleIssueCorrectly() throws Exception {
        CSVIssueReader reader = new CSVIssueReader();

        ArrayList<FirefoxIssue> issues = reader.readIssuesFromCSV("tests/test-xml-files/firefox-1.csv");
        FirefoxIssue expectedIssue = new FirefoxIssue();
        expectedIssue.setBugID(249601);
        expectedIssue.setComponent("Location Bar and Autocomplete");
        expectedIssue.setReporterEmail("twalker@mozilla.com");
        expectedIssue.setAssigneeEmail("bugs@bengoodger.com");
        expectedIssue.setAssigneeEmail30Days("bugs@bengoodger.com");

        assertEquals(expectedIssue.toString(), issues.get(0).toString());
    }

    @Test(expected = FileNotFoundException.class)
    public void ensureReadIssuesFromCSVThrowsExceptionWhenFileDoesntExist() throws Exception {
        CSVIssueReader reader = new CSVIssueReader();

        reader.readIssuesFromCSV("this-file-doesnt-exist.csv");
    }
}