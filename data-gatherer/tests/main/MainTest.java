package main;

import evaluationStructures.FirefoxIssue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class MainTest {

    @Test(expected = IllegalArgumentException.class)
    public void testProcessCommandLineArgumentsThrowsExceptionWithTooFewArguments() {
        String[] args = new String[]{"hello", "you"};
        Main.processCommandLineArguments(args);
    }

    @Test
    public void testProcessCommandLineArgumentsSetsParametersCorrectly() {

        String expectedIssuesToProcessString = "100";
        int expectedIssuesToProcess = 100;
        String expectedJiraIp = "192.168.100.50";
        String expectedJiraPort = "6666";

        String[] args = new String[]{expectedIssuesToProcessString, expectedJiraIp, expectedJiraPort};
        Main.processCommandLineArguments(args);

        int actualIssuesToProcess = Main.getMaxIssuesToProcess();
        String actualJiraIP = Main.getJiraIP();
        String actualJiraPort = Main.getJiraPort();

        assertEquals(expectedIssuesToProcess, actualIssuesToProcess);
        assertEquals(expectedJiraIp, actualJiraIP);
        assertEquals(expectedJiraPort, actualJiraPort);
    }

    @Test
    public void testGetAllUniqueEmails() {
        ArrayList<FirefoxIssue> issues = new ArrayList<>();

        FirefoxIssue issue1 = new FirefoxIssue();
        issue1.setAssigneeEmail("stephen@gmail.com");
        issue1.setReporterEmail("stephen@gmail.com");

        FirefoxIssue issue2 = new FirefoxIssue();
        issue2.setAssigneeEmail("tim@gmail.com");
        issue2.setReporterEmail("tom@gmail.com");

        FirefoxIssue issue3 = new FirefoxIssue();
        issue3.setAssigneeEmail("shaun@gmail.com");
        issue3.setReporterEmail("stephen@gmail.com");

        issues.add(issue1);
        issues.add(issue2);
        issues.add(issue3);

        Set<String> expectedUniqueEmails = new HashSet<>();
        expectedUniqueEmails.add("stephen@gmail.com");
        expectedUniqueEmails.add("tim@gmail.com");
        expectedUniqueEmails.add("tom@gmail.com");
        expectedUniqueEmails.add("shaun@gmail.com");

        Set<String> actualUniqueEmails = Main.getAllUniqueEmails(issues);

        assertEquals(expectedUniqueEmails, actualUniqueEmails);

    }
}