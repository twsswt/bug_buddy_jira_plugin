package puller;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JiraCommentTest {

    @Test
    public void testSetDate() {
        JiraComment comment = new JiraComment();

        String expectedDate = "now";
        comment.setDate(expectedDate);
        String actualDate = comment.getDate();

        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testToString() {
        JiraComment comment = new JiraComment();
        comment.setDate("now");
        comment.setAuthor("me");
        comment.setBody("problem");

        String expectedString = "JiraComment{author='me', body='problem', date='now'}";
        String actualString = comment.toString();

        assertEquals(expectedString, actualString);
    }
}