package sender;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SenderTest {

    @Test
    public void testExtractIssueIdFromSuccessJSON() {
        Sender sender = new Sender("localhost", "2990");

        String successJSON = "{\"id\":\"10402\",\"key\":\"FRFX-6\",\"self\":\"http://localhost:2990/jira/rest/api/2/issue/10402\"}";
        String expectedID = "10402";
        String actualID = sender.extractIssueIDFromSuccessJSON(successJSON);

        assertEquals(expectedID, actualID);
    }

    @Test
    public void testUpdateJiraAPILocation() {
        Sender sender = new Sender("66.66.66.66", "666");
        sender.updateJiraAPILocation();

        String expectedJiraAPILocation = "http://66.66.66.66:666/jira/rest/api/2/";
        String actualJiraAPILocation = sender.getJiraAPILocation();

        assertEquals(expectedJiraAPILocation, actualJiraAPILocation);
    }

    @Test
    public void setIssueJsonLocation() {
        Sender sender = new Sender("localhost", "2990");

        String expectedIssueJSONLocation = "/home/stephen";
        sender.setIssueJSONLocation(expectedIssueJSONLocation);
        String actualIssueJSONLocation = sender.getIssueJSONLocation();

        assertEquals(expectedIssueJSONLocation, actualIssueJSONLocation);
    }
}

