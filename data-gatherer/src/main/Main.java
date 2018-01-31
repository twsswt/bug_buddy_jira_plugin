package main;

import converter.Converter;
import evaluationStructures.FirefoxComment;
import evaluationStructures.FirefoxIssue;
import evaluationStructures.JiraIssue;
import evaluationStructures.JiraProject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scraper.CSVIssueReader;
import scraper.FirefoxScraper;
import sender.Sender;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static int maxIssuesToProcess = Integer.MAX_VALUE;
    private static String jiraIP = "localhost";
    private static String jiraPort = "2990";

    public static void main(String[] args) {

        processCommandLineArguments(args);

        ArrayList<FirefoxIssue> firefoxIssues = getIssueData();

        String jiraJSONLocation = "../project-issue-data/bugreport.mozilla.firefox/JiraJSON/";

        Converter converter = new Converter();

        // Create a JIRA Project json, and write to file
        JiraProject jiraProject = new JiraProject();
        String jiraProjectJson = converter.convertJiraProjectToJiraJSON(jiraProject);
        String jiraProjectJsonFilename = jiraJSONLocation + "project.json";

        writeJSONToFile(jiraProjectJson, jiraProjectJsonFilename);
        logger.info("Written JSON for Project!");

        // Get every unique email in the firefox issues data set
        Set<String> userEmails = new HashSet<>();
        for (FirefoxIssue issue : firefoxIssues) {
            userEmails.add(issue.getAssigneeEmail());
            userEmails.add(issue.getAssigneeEmail30Days());
            userEmails.add(issue.getReporterEmail());
        }

        // Create Jira users from each email address, and write to a file
        for (String email : userEmails) {
            String userJson = converter.convertUserToJiraJSON(email);
            String userJsonFilename = jiraJSONLocation + "users/" + email + ".json";

            writeJSONToFile(userJson, userJsonFilename);
        }
        logger.info("Written JSON for all unique users");

        // Create jira json from every issue, and write to a file
        for (FirefoxIssue firefoxIssue : firefoxIssues) {
            JiraIssue jiraIssue = converter.convertFirefoxIssueToJiraIssue(firefoxIssue);
            String issueJson = converter.convertJiraIssueToJiraJSON(jiraIssue);
            String issueJsonFilename = jiraJSONLocation + "issues/" + firefoxIssue.getBugID() + ".json";

            writeJSONToFile(issueJson, issueJsonFilename);
        }
        logger.info("Written JSON for every issue");


        // Post the project, then all users, then all issues to Jira
        Sender sender = new Sender(jiraIP, jiraPort);
        sender.setIssueJSONLocation(jiraJSONLocation);
        sender.sendPostCommand("project.json", "project");
        logger.info("Posted Project to JIRA");

        for (String email : userEmails) {
            String emailJSONFilename = "users/" + email + ".json";
            sender.sendPostCommand(emailJSONFilename, "user");
        }
        logger.info("Posted all users to JIRA");

        for (FirefoxIssue firefoxIssue : firefoxIssues) {
            String issueID = sender.sendPostCommandExtractIssueID("issues/" + firefoxIssue.getBugID() + ".json", "issue");

            ArrayList<FirefoxComment> comments = firefoxIssue.getComments();
            for (int i = 0; i < comments.size(); i++) {
                FirefoxComment comment = comments.get(i);

                // Convert each comment to JSON
                String commentJson = converter.convertCommentToJiraJSON(comment);
                String commentJsonFilename = jiraJSONLocation + "comments/" + issueID + "-" + i + ".json";
                writeJSONToFile(commentJson, commentJsonFilename);

                // Post to Jira
                sender.sendPostCommand("comments/" + issueID + "-" + i + ".json", "issue/" + issueID + "/comment");
            }
        }
        logger.info("Posted all issues and comments to JIRA");
    }

    private static void processCommandLineArguments(String[] args) {
        if (args.length != 3) {
            logger.error("Not enough arguments!");
            String USAGE = "data-gatherer.jar [maxIssuesToProcess] [jiraIP] [jiraPort]";
            logger.error("Usage: " + USAGE);
            System.exit(1);
        }

        maxIssuesToProcess = Integer.parseInt(args[0]);
        jiraIP = args[1];
        jiraPort = args[2];
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
        FirefoxScraper scraper = new FirefoxScraper();

        for (int i = 0; i < maxIssuesToProcess; i++) {
            logger.info("Processing issue " + (i + 1) + "/" + maxIssuesToProcess);
            scraper.getIssueJSON(issues.get(i), ("../project-issue-data/bugreport.mozilla.firefox/FirefoxIssueJSON/"));

            ArrayList<FirefoxComment> comments = scraper.extractIssueCommentsFromJSON(issues.get(i));
            issues.get(i).setComments(comments);
        }

        return issues;
    }

    private static void writeJSONToFile(String JSON, String filename) {
        FirefoxScraper scraper = new FirefoxScraper();
        scraper.saveDataToFile(JSON, new File(filename));
    }
}

