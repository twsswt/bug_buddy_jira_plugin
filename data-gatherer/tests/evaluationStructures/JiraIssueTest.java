package evaluationStructures;

import org.junit.Test;

import static org.junit.Assert.*;

public class JiraIssueTest {

    @Test
    public void testSetSummary() {
        JiraIssue jiraIssue = new JiraIssue("11056");
        String expectedSummary = "This is a bug";
        jiraIssue.setSummary(expectedSummary);

        String actualSummary = jiraIssue.getSummary();

        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    public void testSetDescription() {
        JiraIssue jiraIssue = new JiraIssue("11056");
        String expectedDescription = "This is a very bad bug";
        jiraIssue.setDescription(expectedDescription);

        String actualDescription = jiraIssue.getDescription();

        assertEquals(expectedDescription, actualDescription);
    }
}