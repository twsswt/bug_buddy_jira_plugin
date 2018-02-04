package main;

import org.junit.Test;

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

}