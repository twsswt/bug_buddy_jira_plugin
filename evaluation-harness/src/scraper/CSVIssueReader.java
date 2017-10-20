package scraper;

import main.FirefoxIssue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVIssueReader {

    public ArrayList<FirefoxIssue> readCSV(String filepath) {

        ArrayList<FirefoxIssue> issues = new ArrayList<FirefoxIssue>();
        File f = new File(filepath);

        try (com.opencsv.CSVReader reader = new com.opencsv.CSVReader(new FileReader(f))) {
            String [] tokens;
            //Remove header
            reader.readNext();

            while ((tokens = reader.readNext()) != null) {
                issues.add(createIssue(tokens));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return issues;
    }

    public FirefoxIssue createIssue(String[] tokens) {
        FirefoxIssue issue = new FirefoxIssue();

        System.out.println(tokens[0]);
        issue.setBugID(Long.parseLong(tokens[0]));
        issue.setComponent(tokens[1]);
        issue.setReporterEmail(tokens[8]);
        issue.setAssigneeEmail(tokens[9]);
        issue.setAssigneeEmail30Days(tokens[15]);

        return issue;
    }
}
