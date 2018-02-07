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

    private static final String FIREFOX_ISSUE_PATH = "project-issue-data/bugreport.mozilla.firefox";
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static int maxIssuesToProcess = Integer.MAX_VALUE;
    private static String jiraIP = "localhost";
    private static String jiraPort = "2990";

    protected static int getMaxIssuesToProcess() {
        return maxIssuesToProcess;
    }

    protected static String getJiraIP() {
        return jiraIP;
    }

    protected static String getJiraPort() {
        return jiraPort;
    }

    public static void main(String[] args) {

        try {
            processCommandLineArguments(args);
        } catch (IllegalArgumentException e) {
            logger.error("Not enough arguments!");
            String USAGE = "data-gatherer.jar [maxIssuesToProcess] [jiraIP] [jiraPort]";
            logger.error("Usage: " + USAGE);
            System.exit(1);
        }

        ArrayList<FirefoxIssue> firefoxIssues = getIssueData();

        String jiraJSONLocation = FIREFOX_ISSUE_PATH + "/JiraJSON/";

        Converter converter = new Converter();

        createAndWriteJiraProject(jiraJSONLocation, converter);

        Set<String> userEmails = getAllUniqueEmails(firefoxIssues);

        createAndWriteJiraUsers(jiraJSONLocation, converter, userEmails);

        createAndWriteJiraIssues(firefoxIssues, jiraJSONLocation, converter);


        // Post the project, then all users, then all issues to Jira
        Sender sender = new Sender(jiraIP, jiraPort);
        sendProjectToJira(jiraJSONLocation, sender);
        sendUsersToJira(userEmails, sender);
        sendIssuesAndCommentsToJira(firefoxIssues, jiraJSONLocation, converter, sender);
    }

    private static void sendIssuesAndCommentsToJira(ArrayList<FirefoxIssue> firefoxIssues, String jiraJSONLocation, Converter converter, Sender sender) {
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

    private static void sendUsersToJira(Set<String> userEmails, Sender sender) {
        for (String email : userEmails) {
            String emailJSONFilename = "users/" + email + ".json";
            sender.sendPostCommand(emailJSONFilename, "user");
        }
        logger.info("Posted all users to JIRA");
    }

    private static void sendProjectToJira(String jiraJSONLocation, Sender sender) {
        sender.setIssueJSONLocation(jiraJSONLocation);
        sender.sendPostCommand("project.json", "project");
        logger.info("Posted Project to JIRA");
    }

    private static void createAndWriteJiraIssues(ArrayList<FirefoxIssue> firefoxIssues, String jiraJSONLocation, Converter converter) {
        // Create jira json from every issue, and write to a file
        for (FirefoxIssue firefoxIssue : firefoxIssues) {
            JiraIssue jiraIssue = converter.convertFirefoxIssueToJiraIssue(firefoxIssue);
            String issueJson = converter.convertJiraIssueToJiraJSON(jiraIssue);
            String issueJsonFilename = jiraJSONLocation + "issues/" + firefoxIssue.getBugID() + ".json";

            writeJSONToFile(issueJson, issueJsonFilename);
        }
        logger.info("Written JSON for every issue");
    }

    private static void createAndWriteJiraUsers(String jiraJSONLocation, Converter converter, Set<String> userEmails) {
        // Create Jira users from each email address, and write to a file
        for (String email : userEmails) {
            String userJson = converter.convertUserToJiraJSON(email);
            String userJsonFilename = jiraJSONLocation + "users/" + email + ".json";

            writeJSONToFile(userJson, userJsonFilename);
        }
        logger.info("Written JSON for all unique users");
    }

    protected static Set<String> getAllUniqueEmails(List<FirefoxIssue> firefoxIssues) {
        Set<String> userEmails = new HashSet<>();
        for (FirefoxIssue issue : firefoxIssues) {
            userEmails.add(issue.getAssigneeEmail());
            userEmails.add(issue.getAssigneeEmail30Days());
            userEmails.add(issue.getReporterEmail());
        }
        userEmails.remove(null);
        return userEmails;
    }

    private static void createAndWriteJiraProject(String jiraJSONLocation, Converter converter) {
        // Create a JIRA Project json, and write to file
        JiraProject jiraProject = new JiraProject();
        String jiraProjectJson = converter.convertJiraProjectToJiraJSON(jiraProject);
        String jiraProjectJsonFilename = jiraJSONLocation + "project.json";

        writeJSONToFile(jiraProjectJson, jiraProjectJsonFilename);
        logger.info("Written JSON for Project!");
    }

    protected static void processCommandLineArguments(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Not Enough Arguments!");
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
            allIssues = reader.readIssuesFromCSV(FIREFOX_ISSUE_PATH + "/mozilla_firefox_bugmeasures.csv");
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
            scraper.getIssueJSON(issues.get(i), FIREFOX_ISSUE_PATH + "/FirefoxIssueJSON/");

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

