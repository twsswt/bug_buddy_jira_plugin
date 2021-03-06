package main;

import matcher.Matcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import puller.JiraIssue;
import puller.Puller;

import java.util.ArrayList;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

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
            case "most-assigned":
                matcher.setMatchingAlgorithm(Matcher.MatchingAlgorithm.MOST_ASSIGNED);
                break;
            default:
                matcher.setMatchingAlgorithm(Matcher.MatchingAlgorithm.WORD_BASED);
                break;
        }

        jiraIP = args[1];
        jiraPort = args[2];
    }

    private static String generateUsageString() {

        return "recommender.jar [algorithmToEvaluate] [jiraIP] [jiraPort]\n" + "Algorithms available:\n" +
                "word\n" +
                "skills\n" +
                "most-assigned";
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
            logger.error("Not enough arguments!");
            String USAGE = generateUsageString();
            logger.error("Usage: " + USAGE);
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
            logger.info("Matching issue " + (i+1) + "/" + jiraIssues.size());
            if (matcher.recommendUserAndValidate(jiraIssues, jiraIssues.get(i))) {
                successfulMatches++;
            }
        }

        logger.info("Matched " + successfulMatches + "/" + jiraIssues.size());
    }


}
