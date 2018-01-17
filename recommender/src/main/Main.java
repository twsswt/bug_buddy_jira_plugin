package main;

import classifier.FrequencyTable;
import classifier.FrequencyTableEntry;
import classifier.Skill;
import classifier.User;
import puller.JiraIssue;
import puller.Puller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    /**
     * This stores the class of matching algorithm to be used
     * when assigning users to issues
     */
    private enum MatchingAlgorithm {
        WORD_BASED, SKILLS_BASED
    }

    private static MatchingAlgorithm matchingAlgorithm;
    private static List<Skill> globalSkills;

    /**
     * Identifies which matching algorithm to use based
     * on the command line arguments passed
     */
    private static void parseCommandLineArguments(String args[]){
        if (args.length == 0) {
            matchingAlgorithm = MatchingAlgorithm.WORD_BASED;
        }
        if (args[0].equals("skills")) {
            matchingAlgorithm = MatchingAlgorithm.SKILLS_BASED;
        } else {
            matchingAlgorithm = MatchingAlgorithm.WORD_BASED;
        }
    }

    /**
     * Initialises the global skills list with all skills
     * we are searching for
     */
    private static void initialiseGlobalSkills() {
        globalSkills = new ArrayList<>();

        Skill networking = new Skill("networking");
        networking.addKeyword("TCP");
        networking.addKeyword("UDP");
        networking.addKeyword("IP");
        networking.addKeyword("connection");
        globalSkills.add(networking);

        Skill frontend = new Skill("frontend");
        frontend.addKeyword("GUI");
        frontend.addKeyword("color");
        frontend.addKeyword("colour");
        globalSkills.add(frontend);

        Skill programmer = new Skill("programmer");
        programmer.addKeyword("method");
        programmer.addKeyword("exception");
        programmer.addKeyword("solution");
        globalSkills.add(programmer);

    }

    /**
     * runs the given matching algorithm over the entire data set, and determines
     * how successful it was at matching the data
     */
    public static void main(String[] args) {

        parseCommandLineArguments(args);
        initialiseGlobalSkills();

        int successfulMatches = 0;

        // Get all issues from a jira instance
        Puller p = new Puller("localhost", "2990");
        ArrayList<JiraIssue> jiraIssues = p.getAllIssues();

        for (int i = 0; i < jiraIssues.size(); i++) {
            
            JiraIssue issueBeingRecommended = jiraIssues.get(i);
            List<JiraIssue> otherIssues = new ArrayList<>(jiraIssues);
            otherIssues.remove(issueBeingRecommended);

            // Build data for test issue
            JiraIssue testIssue = new JiraIssue();
            testIssue.setText(issueBeingRecommended.getComments().get(0).getBody());
            testIssue.setAssignee("newissue@newissue.com");
            testIssue.setReporter(issueBeingRecommended.getReporter());

            List<User> allUsers = identifyAllAssignableUsers(otherIssues);

            if (matchingAlgorithm == MatchingAlgorithm.WORD_BASED) {
                // Build all frequency tables for the test set
                buildAllFrequencyTables(allUsers, otherIssues);

                // Find the closest match between our new frequency table and all other frequency tables...
                String email = findClosestMatchWordBased(testIssue, allUsers);
                if (email.equals(issueBeingRecommended.getAssignee())) {
                    successfulMatches++;
                }
            } else {
                buildAllFrequencyTables(allUsers, otherIssues);

                for (User user : allUsers) {
                    identifySkillsFromFrequencyTable(user, globalSkills);
                }

                String email = findClosestMatchSkillBased(testIssue, allUsers);
                if (email.equals(issueBeingRecommended.getAssignee())) {
                    successfulMatches++;
                }
            }

        }

        System.out.println("");
        System.out.println("Matched " + successfulMatches + "/" + jiraIssues.size());
    }

    private static void identifySkillsFromFrequencyTable(User user, List<Skill> globalSkills) {
        FrequencyTable ft = user.getWordTable();

        for (Skill skill : globalSkills) {
            int keywordHits = 0;

            for (String keyword: skill.getKeywords()) {
                FrequencyTableEntry entry = ft.getEntryWithWord(keyword);
                if (entry != null) {
                    keywordHits += entry.getFrequency();
                }
            }

            // If the number of occurrences of a skills keyword is higher than a threshold, then the user
            // is considered to have that skill
            double threshold = 0.001;
            double skillPercentage = keywordHits / (double) ft.getTotalWords();

            if (skillPercentage > threshold) {
                List<Skill> updatedSkillsList = user.getSkills();
                updatedSkillsList.add(skill);
                user.setSkills(updatedSkillsList);
            }
        }
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
     * making use of their skills
     * <p>
     * Returns the email of the closest match
     */
    private static String findClosestMatchSkillBased(JiraIssue issue, List<User> users) {
        // Build an issue list and a user for the issue
        // This is to deal with the coupling between users and frequency tables
        ArrayList<JiraIssue> issueList = new ArrayList<>();
        issueList.add(issue);
        User issueUser = new User();
        issueUser.setEmail("newissue@newissue.com");
        issueUser.buildFrequencyTable(issueList);

        identifySkillsFromFrequencyTable(issueUser, globalSkills);

        List<Skill> skillsRequired = issueUser.getSkills();
        System.out.println("Skills required: " + skillsRequired);

        User bestUser = null;
        int skillsMatched = 0;

        for (int i = 0; i < users.size(); i++) {
            int thisSkillsMatched = 0;
            List<Skill> userSkills = users.get(i).getSkills();

            for (Skill requiredSkill : skillsRequired) {
                if (userSkills.contains(requiredSkill)) {
                    thisSkillsMatched++;
                }
            }

            if (thisSkillsMatched > skillsMatched) {
                bestUser = users.get(i);
                skillsMatched = thisSkillsMatched;
            }
        }

        if (bestUser == null) {
            System.out.println("Couldn't find any match! :(");
            return "";
        } else {

            System.out.println("We recommend you assign this issue to: " + bestUser.getEmail());
            return bestUser.getEmail();
        }
    }

    /**
     * Finds the closest match between an issue and a list of users, by
     * making use of their frequency tables.
     * <p>
     * Returns the email of the closest match
     */
    private static String findClosestMatchWordBased(JiraIssue issue, List<User> users) {

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
