package evaluationStructures;

import org.junit.Test;

import static org.junit.Assert.*;

public class FirefoxCommentTest {

    @Test
    public void testSetCreationTime() {
        FirefoxComment firefoxComment = new FirefoxComment();
        String expectedCreationTime = "2018-01-30";
        firefoxComment.setCreationTime(expectedCreationTime);

        String actualCreationTime = firefoxComment.getCreationTime();

        assertEquals(expectedCreationTime, actualCreationTime);
    }

    @Test
    public void testSetAuthorEmail() {
        FirefoxComment firefoxComment = new FirefoxComment();
        String expectedAuthorEmail = "1106679b@student.gla.ac.uk";
        firefoxComment.setAuthorEmail(expectedAuthorEmail);

        String actualAuthorEmail = firefoxComment.getAuthorEmail();

        assertEquals(expectedAuthorEmail, actualAuthorEmail);
    }

    @Test
    public void testSetCommentText() {
        FirefoxComment firefoxComment = new FirefoxComment();
        String expectedCommentText = "This is a problem";
        firefoxComment.setCommentText(expectedCommentText);

        String actualCommentText = firefoxComment.getCommentText();

        assertEquals(expectedCommentText, actualCommentText);
    }
}