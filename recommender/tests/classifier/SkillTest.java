package classifier;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SkillTest {
    @Test
    public void testSetName() {
        Skill skill = new Skill("testing");

        String expectedName = "bdd";
        skill.setName(expectedName);
        String actualName = skill.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void testSetKeywords() {
        Skill skill = new Skill("testing");

        List<String> expectedKeywords = new ArrayList<>();
        expectedKeywords.add("bdd");
        expectedKeywords.add("mocking");

        skill.setKeywords(expectedKeywords);

        List<String> actualKeywords = skill.getKeywords();

        assertEquals(expectedKeywords, actualKeywords);
    }

    @Test
    public void testToString() {
        Skill skill = new Skill("testing");
        skill.addKeyword("bdd");
        skill.addKeyword("mocking");

        String expectedString = "Skill{name='testing', keywords=[bdd, mocking]}";
        String actualString = skill.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testAddKeywords() {
        Skill skill = new Skill("testing");

        List<String> oneKeyword = new ArrayList<>();
        oneKeyword.add("bdd");

        List<String> twoKeywords = new ArrayList<>();
        twoKeywords.add("bdd");
        twoKeywords.add("mocking");

        skill.setKeywords(oneKeyword);
        skill.addKeyword("mocking");

        List<String> actualKeywords = skill.getKeywords();

        assertEquals(twoKeywords, actualKeywords);
    }

}