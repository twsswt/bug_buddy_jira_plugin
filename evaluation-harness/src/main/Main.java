package main;

import converter.Converter;
import scraper.CSVIssueReader;
import scraper.Scraper;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Main {

    private static int maxIssuesToProcess = Integer.MAX_VALUE;

    public static void main(String[] args) {

        processArguments(args);

        ArrayList<FirefoxIssue> firefoxIssues = getIssueData();
        ArrayList<JiraIssue> jiraIssues = new ArrayList<>();

        String issueJSONlocation = "../project-issue-data/bugreport.mozilla.firefox/issueJSON/";

        Converter converter = new Converter();

        // Create a JIRA Project json, and write to file
        JiraProject jiraProject = new JiraProject();
        String jiraProjectJson = converter.convertJiraProjectToJiraJSON(jiraProject);
        String jiraProjectJsonFilename = issueJSONlocation + "project.json";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(jiraProjectJsonFilename));
            writer.write(jiraProjectJson);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get every unique email in the firefox issues dataset
        Set<String> userEmails = new HashSet<>();
        for (FirefoxIssue issue: firefoxIssues) {
            userEmails.add(issue.getAssigneeEmail());
            userEmails.add(issue.getAssigneeEmail30Days());
            userEmails.add(issue.getReporterEmail());
        }

        // Create Jira users from each email address, and write to a file
        for (String email: userEmails) {
            String userJson = converter.convertEmailAddressToJiraUser(email);
            String userJsonFilename = issueJSONlocation + "users/" + email + ".json";

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(userJsonFilename));
                writer.write(userJson);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Create jira json from every issue, and write to a file
        for (FirefoxIssue firefoxIssue: firefoxIssues) {
            JiraIssue jiraIssue = converter.convertFirefoxIssueToJiraIssue(firefoxIssue);
            String issueJson = converter.convertJiraIssueToJiraJSON(jiraIssue);
            String issueJsonFilename = issueJSONlocation + "issues/" + firefoxIssue.getBugID() + ".json";

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(issueJsonFilename));
                writer.write(issueJson);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // TODO convert each issues comments to Jira Json

        // Post the project, then all users, then all issues to Jira
        String basicCurlCommand1 = "curl -D- -u admin:admin -X POST --data @" + issueJSONlocation;
        String basicCurlCommand2 = " -H Content-Type:application/json http://localhost:2990/jira/rest/api/2/";
        try {
            String fullCurlCommand = basicCurlCommand1 + "project.json" + basicCurlCommand2 + "project";
            System.out.println(fullCurlCommand);
            Process p = Runtime.getRuntime().exec(fullCurlCommand);
            p.waitFor();

            InputStream stdout = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (String email: userEmails) {
                String fullCurlCommand = basicCurlCommand1 + "users/" + email + ".json" + basicCurlCommand2 + "user";
                System.out.println(fullCurlCommand);
                Process p = Runtime.getRuntime().exec(fullCurlCommand);
                p.waitFor();

                InputStream stdout = p.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (FirefoxIssue firefoxIssue: firefoxIssues) {
                String fullCurlCommand = basicCurlCommand1 + "issues/" + firefoxIssue.getBugID() + ".json" + basicCurlCommand2 + "issue";
                System.out.println(fullCurlCommand);
                Process p = Runtime.getRuntime().exec(fullCurlCommand);
                p.waitFor();

                InputStream stdout = p.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO post all issue comments to JIRA
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
}
