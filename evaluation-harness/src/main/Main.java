package main;

import scraper.CSVIssueReader;
import scraper.Scraper;

import java.util.ArrayList;

class Main {

    public static void main(String[] args) {

        int maxIssuesToDownload = Integer.MAX_VALUE;
        if (args.length > 0) {
            maxIssuesToDownload = Integer.parseInt(args[0]);
        }

        ArrayList<FirefoxIssue> issues;

        // Extract data from the Bug Database CSV File
        CSVIssueReader reader = new CSVIssueReader();
        issues = reader.readIssuesFromCSV("/home/stephen/bug_buddy_jira_plugin/project-issue-data/bugreport.mozilla.firefox/mozilla_firefox_bugmeasures.csv");

        if (issues.size() < maxIssuesToDownload) {
            maxIssuesToDownload = issues.size();
        }

        // Get the comments for each bug
        // Comments aren't provided in the Bug Database
        Scraper s = new Scraper();
        for (int i = 0; i < maxIssuesToDownload; i++) {
            boolean downloaded = s.getIssueXML(issues.get(i));
            if (downloaded) {
                System.out.println("Downloaded:\t" + (i + 1) + "/" + maxIssuesToDownload);
            } else {
                System.out.println("Skipped:\t" + (i + 1) + "/" + maxIssuesToDownload);
            }
            ArrayList<String> comments = s.extractIssueComments(issues.get(i));
            issues.get(i).setComments(comments);
        }

    }
}
