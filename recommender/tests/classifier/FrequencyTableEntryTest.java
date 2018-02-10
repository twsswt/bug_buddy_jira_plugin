package classifier;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FrequencyTableEntryTest {

    @Test
    public void testSetWord() {
        FrequencyTableEntry entry = new FrequencyTableEntry("zero", 0);
        String expectedWordUppercase = "CAT";
        entry.setWord(expectedWordUppercase);
        String expectedWord = "cat";

        String actualWord = entry.getWord();

        assertEquals(expectedWord, actualWord);
    }

    @Test
    public void testSetFrequency() {
        FrequencyTableEntry entry = new FrequencyTableEntry("zero", 0);
        int expectedFrequency = 10;
        entry.setFrequency(10);

        int actualFrequency = entry.getFrequency();

        assertEquals(expectedFrequency, actualFrequency);
    }
}