package puller;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class JiraIssueTest {

    @Test
    public void testSetReporter() {
        JiraIssue issue = new JiraIssue();

        String expectedReporter = "me";
        issue.setReporter(expectedReporter);
        String actualReporter = issue.getReporter();

        assertEquals(expectedReporter, actualReporter);
    }

    @Test
    public void testSetId() {
        JiraIssue issue = new JiraIssue();

        String expectedId = "1";
        issue.setId(expectedId);
        String actualId = issue.getId();

        assertEquals(expectedId, actualId);
    }

    @Test
    public void testToString() {
        JiraIssue issue = new JiraIssue();

        issue.setId("1");
        issue.setReporter("me");
        issue.setAssignee("you");
        issue.setText("problem");

        JiraComment comment = new JiraComment();
        comment.setAuthor("tim");
        comment.setDate("now");
        comment.setBody("big problem");

        ArrayList<JiraComment> comments = new ArrayList<>();
        comments.add(comment);

        issue.setComments(comments);

        String expectedString = "JiraIssue{text='problem', reporter='me', assignee='you', id='1', comments=[JiraComment{author='tim', body='big problem', date='now'}]}";
        String actualString = issue.toString();

        assertEquals(expectedString, actualString);
    }
}