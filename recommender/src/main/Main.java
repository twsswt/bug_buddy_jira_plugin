package main;

import matcher.Matcher;
import puller.JiraIssue;
import puller.Puller;

import java.util.ArrayList;

public class Main {


    /**
     * Identifies which matching algorithm to use based
     * on the command line arguments passed
     */
    private static void parseCommandLineArguments(String args[], Matcher matcher) {
        if (args.length == 0) {
            matcher.setMatchingAlgorithm(Matcher.MatchingAlgorithm.WORD_BASED);
        }
        if (args[0].equals("skills")) {
            matcher.setMatchingAlgorithm(Matcher.MatchingAlgorithm.SKILLS_BASED);
        } else {
            matcher.setMatchingAlgorithm(Matcher.MatchingAlgorithm.WORD_BASED);
        }
    }


    /**
     * runs the given matching algorithm over the entire data set, and determines
     * how successful it was at matching the data
     */
    public static void main(String[] args) {

        Matcher matcher = new Matcher();
        parseCommandLineArguments(args, matcher);

        if (matcher.getMatchingAlgorithm() == Matcher.MatchingAlgorithm.SKILLS_BASED) {
            matcher.initialiseGlobalSkills();
        }

        int successfulMatches = 0;


        // Get all issues from a jira instance
        Puller p = new Puller("localhost", "2990");
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
