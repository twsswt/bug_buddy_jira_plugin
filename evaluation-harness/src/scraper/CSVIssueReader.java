package scraper;

import main.FirefoxIssue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVIssueReader {

    /**
     * readIssuesFromCSV will create a list of Issues from a CSV file
     * @param filepath the location of the CSV file
     * @return An ArrayList containing a list of Issues
     */
    public ArrayList<FirefoxIssue> readIssuesFromCSV(String filepath) throws FileNotFoundException {

        ArrayList<FirefoxIssue> issues = new ArrayList<>();

        // Open the CSV file for reading
        File f = new File(filepath);

        if (!f.exists()) {
            throw new FileNotFoundException();
        }

        try (com.opencsv.CSVReader reader = new com.opencsv.CSVReader(new FileReader(f))) {

            // Remove the headers from the CSV file
            reader.readNext();

            // Create each issue
            String[] tokens;
            while ((tokens = reader.readNext()) != null) {
                issues.add(createIssue(tokens));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return issues;
    }

    /**
     * createIssue will create an issue from an array of tokens.
     * @param tokens An array of strings containing the individual components of an issue
     * @return A new FirefoxIssue created using the array of tokens
     */
    private FirefoxIssue createIssue(String[] tokens) {
        FirefoxIssue issue = new FirefoxIssue();

        issue.setBugID(Long.parseLong(tokens[0]));
        issue.setComponent(tokens[1]);
        issue.setReporterEmail(tokens[8]);
        issue.setAssigneeEmail(tokens[9]);
        issue.setAssigneeEmail30Days(tokens[15]);

        return issue;
    }
}
