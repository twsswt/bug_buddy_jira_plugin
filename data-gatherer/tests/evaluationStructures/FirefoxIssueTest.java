package evaluationStructures;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FirefoxIssueTest {

    @Test
    public void testSetComments() {
       FirefoxIssue firefoxIssue = new FirefoxIssue();
       FirefoxComment comment1 = new FirefoxComment();
       FirefoxComment comment2 = new FirefoxComment();
       FirefoxComment comment3 = new FirefoxComment();

       comment1.setCommentText("Hello");
       comment2.setCommentText("Goodbye");
       comment3.setCommentText("");

       ArrayList<FirefoxComment> expectedComments = new ArrayList<>();
       expectedComments.add(comment1);
       expectedComments.add(comment2);
       expectedComments.add(comment3);

       firefoxIssue.setComments(expectedComments);

       List<FirefoxComment> actualComments = firefoxIssue.getComments();

       assertEquals(expectedComments, actualComments);
    }

    @Test
    public void testSetComponent() {
        FirefoxIssue firefoxIssue = new FirefoxIssue();
        String expectedComponent = "navbar";
        firefoxIssue.setComponent(expectedComponent);

        String actualComponent = firefoxIssue.getComponent();

        assertEquals(expectedComponent, actualComponent);
    }

    @Test
    public void testSetAssigneeEmail() {
        FirefoxIssue firefoxIssue = new FirefoxIssue();
        String expectedAssigneeEmail = "test@test.com";
        firefoxIssue.setAssigneeEmail(expectedAssigneeEmail);

        String actualAssigneeEmail = firefoxIssue.getAssigneeEmail();

        assertEquals(expectedAssigneeEmail, actualAssigneeEmail);
    }
}