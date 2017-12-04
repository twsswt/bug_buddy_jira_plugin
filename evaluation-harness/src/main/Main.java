package main;

import converter.Converter;
import evaluationStructures.FirefoxIssue;
import evaluationStructures.JiraIssue;
import evaluationStructures.JiraProject;
import scraper.CSVIssueReader;
import scraper.Scraper;
import sender.Sender;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Main {

    private static int maxIssuesToProcess = Integer.MAX_VALUE;

    public static void main(String[] args) {

        processArguments(args);

        ArrayList<FirefoxIssue> firefoxIssues = getIssueData();

        String issueJSONlocation = "../project-issue-data/bugreport.mozilla.firefox/issueJSON/";

        Converter converter = new Converter();

        // Create a JIRA Project json, and write to file
        JiraProject jiraProject = new JiraProject();
        String jiraProjectJson = converter.convertJiraProjectToJiraJSON(jiraProject);
        String jiraProjectJsonFilename = issueJSONlocation + "project.json";

        writeJSONToFile(jiraProjectJson, jiraProjectJsonFilename);

        // Get every unique email in the firefox issues dataset
        Set<String> userEmails = new HashSet<>();
        for (FirefoxIssue issue : firefoxIssues) {
            userEmails.add(issue.getAssigneeEmail());
            userEmails.add(issue.getAssigneeEmail30Days());
            userEmails.add(issue.getReporterEmail());
        }

        // Create Jira users from each email address, and write to a file
        for (String email : userEmails) {
            String userJson = converter.convertEmailAddressToJiraUser(email);
            String userJsonFilename = issueJSONlocation + "users/" + email + ".json";

            writeJSONToFile(userJson, userJsonFilename);

        }

        // Create jira json from every issue, and write to a file
        for (FirefoxIssue firefoxIssue : firefoxIssues) {
            JiraIssue jiraIssue = converter.convertFirefoxIssueToJiraIssue(firefoxIssue);
            String issueJson = converter.convertJiraIssueToJiraJSON(jiraIssue);
            String issueJsonFilename = issueJSONlocation + "issues/" + firefoxIssue.getBugID() + ".json";

            writeJSONToFile(issueJson, issueJsonFilename);
        }


        // Post the project, then all users, then all issues to Jira
        Sender sender = new Sender();
        sender.setIssueJSONLocation(issueJSONlocation);
        sender.sendPostCommand("project.json", "project");

        for (String email : userEmails) {
            String emailJSONFilename = "users/" + email + ".json";
            sender.sendPostCommand(emailJSONFilename, "user");
        }

        for (FirefoxIssue firefoxIssue : firefoxIssues) {
            String issueID = sender.sendPostCommandExtractIssueID("issues/" + firefoxIssue.getBugID() + ".json", "issue");

            ArrayList<String> comments = firefoxIssue.getComments();
            for (int i = 0; i < comments.size(); i++) {
                String comment = comments.get(i);

                // Convert each comment to JSON
                String commentJson = converter.convertCommentToJiraJSON(comment);
                String commentJsonFilename = issueJSONlocation + "comments/" + issueID + "-" + i + ".json";
                writeJSONToFile(commentJson, commentJsonFilename);

                // Post to Jira
                sender.sendPostCommand("comments/" + issueID + "-" + i + ".json", "issue/" + issueID + "/comment");
            }
        }
    }

    private static void processArguments(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Not Enough Arguments");
        }

        maxIssuesToProcess = Integer.parseInt(args[0]);
    }

    private static void downloadIssueXMLIfRequired(Scraper s, FirefoxIssue issue, int current) {
        boolean downloaded = s.getIssueXML(issue, ("../project-issue-data/bugreport.mozilla.firefox/issueXML/"));
        if (downloaded) {
            System.out.println("Downloaded:\t" + (current + 1) + "/" + maxIssuesToProcess);
        } else {
            System.out.println("Skipped:\t" + (current + 1) + "/" + maxIssuesToProcess);
        }
    }

    private static ArrayList<FirefoxIssue> getIssueData() {
        // Extract issue data from the Bug Database CSV File
        CSVIssueReader reader = new CSVIssueReader();

        List<FirefoxIssue> allIssues = new ArrayList<>();
        try {
            allIssues = reader.readIssuesFromCSV("../project-issue-data/bugreport.mozilla.firefox/mozilla_firefox_bugmeasures.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Ensure we only process as many issues as actually exist
        if (allIssues.size() < maxIssuesToProcess) {
            maxIssuesToProcess = allIssues.size();
        }
        ArrayList<FirefoxIssue> issues = new ArrayList<>(allIssues.subList(0, maxIssuesToProcess));

        // Get the comments for each issue, since they
        // aren't provided in the issue data CSV
        Scraper s = new Scraper();

        for (int i = 0; i < maxIssuesToProcess; i++) {
            downloadIssueXMLIfRequired(s, issues.get(i), i);

            ArrayList<String> comments = s.extractIssueComments(issues.get(i));
            issues.get(i).setComments(comments);
        }

        return issues;
    }

    private static void writeJSONToFile(String JSON, String filename) {
        Scraper s = new Scraper();
        s.saveDataToFile(JSON, new File(filename));
    }
}

