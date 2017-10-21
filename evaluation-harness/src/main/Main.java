package main;

import scraper.CSVIssueReader;
import scraper.Scraper;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<FirefoxIssue> issues;

        // Extract data from the Bug Database CSV File
        CSVIssueReader reader = new CSVIssueReader();
        issues = reader.readIssuesFromCSV("/home/stephen/bug_buddy_jira_plugin/project-issue-data/bugreport.mozilla.firefox/mozilla_firefox_bugmeasures.csv");
        System.out.println(issues);

        // Get the comments for each bug
        // Comments aren't provided in the Bug Database
        Scraper s = new Scraper();
        System.out.println(s.getIssueXML(issues.get(0)));

    }
}
