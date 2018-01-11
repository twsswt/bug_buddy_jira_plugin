package main;

import classifier.FrequencyTable;
import classifier.User;
import puller.JiraComment;
import puller.JiraIssue;
import puller.Puller;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        int successfulMatches = 0;

        // Get all issues from a jira instance
        Puller p = new Puller("localhost", "2990");
        ArrayList<JiraIssue> jiraIssues = p.getAllIssues();

        for (int i = 0; i < jiraIssues.size(); i++) {
            JiraIssue issueBeingRecommended = jiraIssues.get(i);
            List<JiraIssue> otherIssues = new ArrayList<>(jiraIssues);
            otherIssues.remove(issueBeingRecommended);


            // Build all frequency tables for the test set
            List<User> allUsers = identifyAllUsers(otherIssues);
            buildAllFrequencyTables(allUsers, otherIssues);

            // Build frequency table for test issue
            JiraIssue testIssue = new JiraIssue();
            testIssue.setText(issueBeingRecommended.getText());
            testIssue.setAssignee("newissue@newissue.com");
            testIssue.setReporter(issueBeingRecommended.getReporter());

            // Find the closest match between our new frequency table and all other frequency tables...
            String email = findClosestMatch(testIssue, allUsers);
            if (email.equals(issueBeingRecommended.getAssignee())) {
                successfulMatches++;

            }

        }

        System.out.println("");
        System.out.println("Matched " + successfulMatches + "/" + jiraIssues.size());
    }

    /**
     * Identify all the users in a collection of issues
     *
     * @return A list of users involved in the issue collection
     */
    private static List<User> identifyAllUsers(List<JiraIssue> issues) {
        // Find all Unique Emails
        Set<String> allUniqueEmails = new HashSet<>();
        for (JiraIssue issue : issues) {
            allUniqueEmails.add(issue.getReporter());
            allUniqueEmails.add(issue.getAssignee());

            for (JiraComment comment : issue.getComments()) {
                allUniqueEmails.add(comment.getAuthor());
            }
        }

        // Create a list of user objects using all unique emails
        List<User> allUsers = new ArrayList<>();
        for (String email : allUniqueEmails) {
            User u = new User();
            u.setEmail(email);
            u.setWordTable(new FrequencyTable());
            allUsers.add(u);
        }

        return allUsers;
    }

    /**
     * Builds a frequency table for all users
     */
    private static void buildAllFrequencyTables(List<User> users, List<JiraIssue> issues) {
        for (int i = 0; i < users.size(); i++) {
            users.get(i).buildFrequencyTable(issues);
            System.out.println("Built Frequency Table for user " + i);
        }
    }

    /**
     * Finds the closest match between an issue and a list of users, by
     * making use of their frequency tables.
     *
     * Returns the email of the closest match
     */
    private static String findClosestMatch(JiraIssue issue, List<User> users) {

        // Build an issue list and a user for the issue
        // This is to deal with the coupling between users and frequency tables
        ArrayList<JiraIssue> issueList = new ArrayList<>();
        issueList.add(issue);
        User issueUser = new User();
        issueUser.setEmail("newissue@newissue.com");
        issueUser.buildFrequencyTable(issueList);

        FrequencyTable issueTable = issueUser.getWordTable();


        // Identify the best match between the issues frequency table and all other frequency tables
        double maxMatch = 0;
        int maxIndex = 0;

        for (int i = 0; i < users.size(); i++) {
            double similarity = issueTable.compareSimilarity(users.get(i).getWordTable());
            System.out.println("i: " + i + " sim " + similarity);

            if (similarity > maxMatch) {
                maxMatch = similarity;
                maxIndex = i;
            }
        }

        // Output the results
        System.out.println("Max match: " + maxMatch);
        System.out.println("Max index: " + maxIndex);
        System.out.println("We Recommend you assign this issue to");
        System.out.println(users.get(maxIndex).getEmail());

        return users.get(maxIndex).getEmail();

    }
}
