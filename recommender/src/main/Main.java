package main;

import matcher.Matcher;
import puller.JiraIssue;
import puller.Puller;

import java.util.ArrayList;

public class Main {

    private static String jiraIP = "localhost";
    private static String jiraPort = "2990";

    /**
     * Identifies which matching algorithm to use based
     * on the command line arguments passed
     */
    private static void parseCommandLineArguments(String args[], Matcher matcher) {
        if (args.length != 3) {
            throw new IllegalArgumentException();
        }

        switch (args[0]) {
            case "skills":
                matcher.setMatchingAlgorithm(Matcher.MatchingAlgorithm.SKILLS_BASED);
                break;
            case "word":
                matcher.setMatchingAlgorithm(Matcher.MatchingAlgorithm.WORD_BASED);
                break;
            default:
                matcher.setMatchingAlgorithm(Matcher.MatchingAlgorithm.WORD_BASED);
                break;
        }

        jiraIP = args[1];
        jiraPort = args[2];
    }

    private static String generateUsageString() {
        StringBuilder usageBuilder = new StringBuilder("recommender.jar [algorithmToEvaluate] [jiraIP] [jiraPort]\n");

        usageBuilder.append("Algorithms available:\n");
        usageBuilder.append("word\n");
        usageBuilder.append("skills");

        return usageBuilder.toString();
    }

    /**
     * runs the given matching algorithm over the entire data set, and determines
     * how successful it was at matching the data
     */
    public static void main(String[] args) {

        Matcher matcher = new Matcher();

        try {
            parseCommandLineArguments(args, matcher);
        } catch (IllegalArgumentException e) {
            System.err.println("Not enough arguments!");
            String USAGE = generateUsageString();
            System.err.println("Usage: " + USAGE);
            System.exit(1);
        }

        if (matcher.getMatchingAlgorithm() == Matcher.MatchingAlgorithm.SKILLS_BASED) {
            matcher.initialiseGlobalSkills();
        }

        int successfulMatches = 0;


        // Get all issues from a jira instance
        Puller p = new Puller(jiraIP, jiraPort);
        ArrayList<JiraIssue> jiraIssues = p.getAllIssues();

        // Assign every issue to a user, and determine if our assignment was the same
        // as the actual assignment
        for (int i = 0; i < jiraIssues.size(); i++) {
            if (matcher.recommendUserAndValidate(jiraIssues, jiraIssues.get(i))) {
                successfulMatches++;
            }
        }

        System.out.println("");
        System.out.println("Matched " + successfulMatches + "/" + jiraIssues.size());
    }


}
