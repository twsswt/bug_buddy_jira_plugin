package classifier;

import org.junit.Test;
import puller.JiraComment;
import puller.JiraIssue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class UserTest {

    public List<JiraIssue> getStandardIssueSet() {
        JiraIssue j1 = new JiraIssue();
        j1.setAssignee("hello@hello.com");
        j1.setReporter("hello@hello.com");
        j1.setText("GUI is broken");

        JiraIssue j2 = new JiraIssue();
        j2.setAssignee("hello@hello.com");
        j2.setReporter("hello@hello.com");
        j2.setText("TCP connection is broken");

        JiraIssue j3 = new JiraIssue();
        j3.setAssignee("goodbye@hello.com");
        j3.setReporter("goodbye@hello.com");
        j3.setText("UDP connection is broken");

        List<JiraIssue> issues = new ArrayList<>();
        issues.add(j1);
        issues.add(j2);
        issues.add(j3);

        return issues;
    }

    @Test
    public void testSetSkills() {
        User user = new User();

        Skill s1 = new Skill("testing");

        List<Skill> expectedSkills = new ArrayList<>();
        expectedSkills.add(s1);

        user.setSkills(expectedSkills);

        List<Skill> actualSkills = user.getSkills();

        assertEquals(expectedSkills, actualSkills);
    }

    @Test
    public void testSetWordTable() {
        User user = new User();

        FrequencyTable expectedWordTable = new FrequencyTable();
        user.setWordTable(expectedWordTable);
        FrequencyTable actualWordTable = user.getWordTable();

        assertEquals(expectedWordTable, actualWordTable);
    }

    @Test
    public void testToString() {
        User user = new User();
        user.setEmail("test@test.com");
        String expectedString = "User{email='test@test.com'}";
        String actualString = user.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetAllUniqueWords() {
        List<JiraIssue> issues = getStandardIssueSet();

        User u = new User();
        u.setEmail("hello@hello.com");
        Set<String> uniqueWordsActual = u.getAllUniqueWords(issues);

        Set<String> uniqueWordsExpected = new HashSet<>();
        uniqueWordsExpected.add("GUI");
        uniqueWordsExpected.add("is");
        uniqueWordsExpected.add("broken");
        uniqueWordsExpected.add("TCP");
        uniqueWordsExpected.add("connection");

        assertEquals(uniqueWordsExpected, uniqueWordsActual);
    }

    @Test
    public void testGetFrequencyTableFromUniqueWords() {
        List<JiraIssue> issues = getStandardIssueSet();

        User u = new User();
        u.setEmail("hello@hello.com");
        Set<String> uniqueWords = u.getAllUniqueWords(issues);
        List<String> allWords = u.getAllWordsList(issues);
        FrequencyTable frequencyTableActual = u.getFrequencyTableFromUniqueWords(uniqueWords, allWords);

        FrequencyTable frequencyTableExpected = new FrequencyTable();
        frequencyTableExpected.addEntry(new FrequencyTableEntry("GUI", 1));
        frequencyTableExpected.addEntry(new FrequencyTableEntry("is", 2));
        frequencyTableExpected.addEntry(new FrequencyTableEntry("broken", 2));
        frequencyTableExpected.addEntry(new FrequencyTableEntry("TCP", 1));
        frequencyTableExpected.addEntry(new FrequencyTableEntry("connection", 1));

        double similarity = frequencyTableActual.compareSimilarity(frequencyTableExpected);

        assertEquals(1, similarity, 0.0001);
    }

    @Test
    public void testBuildFrequencyTable() {
        User user = new User();
        List<JiraIssue> issues = getStandardIssueSet();
        ArrayList<JiraComment> comments = new ArrayList<>();
        JiraComment comment = new JiraComment();
        comment.setAuthor("hello@hello.com");
        comment.setBody("broken");

        comments.add(comment);
        issues.get(2).setComments(comments);

        user.setEmail("hello@hello.com");

        user.buildFrequencyTable(issues);

        FrequencyTable expectedWordTable = new FrequencyTable();
        expectedWordTable.addEntry(new FrequencyTableEntry("broken", 3));
        expectedWordTable.addEntry(new FrequencyTableEntry("tcp", 1));
        expectedWordTable.addEntry(new FrequencyTableEntry("gui", 1));
        expectedWordTable.addEntry(new FrequencyTableEntry("is", 2));
        expectedWordTable.addEntry(new FrequencyTableEntry("connection", 1));

        FrequencyTable actualWordTable = user.getWordTable();

        assertEquals(expectedWordTable.toString(), actualWordTable.toString());
    }

}