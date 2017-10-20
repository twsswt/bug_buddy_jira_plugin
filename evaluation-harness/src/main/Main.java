package main;

import main.FirefoxIssue;
import scraper.Scraper;

import java.util.ArrayList;

public class Main {

    public static ArrayList<FirefoxIssue> issues;

    public static void main(String[] args) {
        System.out.println("Hello World!");
        FirefoxIssue ff = new FirefoxIssue();
        ff.setBugID(600);

        System.out.println(ff);

        Scraper s = new Scraper();
        ArrayList<FirefoxIssue> issues = s.readFile("/home/stephen/bug_buddy_jira_plugin/project-issue-data/bugreport.mozilla.firefox/mozilla_firefox_bugmeasures.csv");
        System.out.println(issues);
    }
}
