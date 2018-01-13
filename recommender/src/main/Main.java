package main;

import classifier.FrequencyTable;
import classifier.User;
import puller.JiraIssue;
import puller.Puller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            List<User> allUsers = identifyAllAssignableUsers(otherIssues);
            buildAllFrequencyTables(allUsers, otherIssues);

            // Build frequency table for test issue
            JiraIssue testIssue = new JiraIssue();
            testIssue.setText(issueBeingRecommended.getComments().get(0).getBody());
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
    private static List<User> identifyAllAssignableUsers(List<JiraIssue> issues) {
        // Find all Unique Emails that have previously had an issue assigned to them
        Set<String> allUniqueEmails = new HashSet<>();
        for (JiraIssue issue : issues) {
            allUniqueEmails.add(issue.getAssignee());
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
        }
    }

    /**
     * Finds the closest match between an issue and a list of users, by
     * making use of their frequency tables.
     * <p>
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
            
            if (similarity > maxMatch) {
                maxMatch = similarity;
                maxIndex = i;
            }
        }

        // Output the results
        System.out.println("We Recommend you assign this issue to: " + users.get(maxIndex).getEmail());

        return users.get(maxIndex).getEmail();

    }
}
