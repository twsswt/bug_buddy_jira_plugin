package classifier;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FrequencyTableTest {
    @Test
    public void testCompareSimilaritySameTable() {

        FrequencyTable ft = new FrequencyTable();
        ft.addEntry(new FrequencyTableEntry("The", 6));
        ft.addEntry(new FrequencyTableEntry("Cat", 4));
        ft.addEntry(new FrequencyTableEntry("Meows", 1));

        double similarity = ft.compareSimilarity(ft);

        assertEquals(1, similarity, 0.0001);
    }

    @Test
    public void testCompareSimilarityCompletelyDifferent() {
        FrequencyTable ft1 = new FrequencyTable();
        ft1.addEntry(new FrequencyTableEntry("The", 6));

        FrequencyTable ft2 = new FrequencyTable();
        ft2.addEntry(new FrequencyTableEntry("Cat", 7));

        double similarity = ft1.compareSimilarity(ft2);

        assertEquals(0, similarity, 0.0001);
    }

    @Test
    public void testCompareSimilarityGreaterThanOther() {
        FrequencyTableEntry cats = new FrequencyTableEntry("Cat", 7);
        FrequencyTableEntry dogs = new FrequencyTableEntry("Dog", 6);
        FrequencyTableEntry fish = new FrequencyTableEntry("Fish", 5);
        FrequencyTableEntry pigs = new FrequencyTableEntry("Pigs", 4);

        FrequencyTable ft1 = new FrequencyTable();
        ft1.addEntry(cats);
        ft1.addEntry(dogs);
        ft1.addEntry(pigs);
        ft1.addEntry(fish);

        FrequencyTable ft2 = new FrequencyTable();
        ft2.addEntry(cats);
        ft2.addEntry(dogs);
        ft2.addEntry(fish);

        FrequencyTable ft3 = new FrequencyTable();
        ft3.addEntry(cats);
        ft3.addEntry(dogs);

        double similarity12 = ft1.compareSimilarity(ft2);
        double similarity13 = ft1.compareSimilarity(ft3);

        assertTrue(similarity12 > similarity13);
    }

    @Test
    public void testGetTotalWords() {
        FrequencyTableEntry cats = new FrequencyTableEntry("Cats", 7);
        FrequencyTableEntry dogs = new FrequencyTableEntry("Dogs", 6);

        FrequencyTable ft = new FrequencyTable();
        ft.addEntry(cats);
        ft.addEntry(dogs);

        int totalWords = ft.getTotalWords();

        assertEquals(13, totalWords);
    }

    @Test
    public void testSetEntries() {
        FrequencyTable ft = new FrequencyTable();

        ArrayList<FrequencyTableEntry> expectedEntries = new ArrayList<>();
        expectedEntries.add(new FrequencyTableEntry("Cats", 7));
        expectedEntries.add(new FrequencyTableEntry("Dogs", 6));

        ft.setEntries(expectedEntries);

        ArrayList<FrequencyTableEntry> actualEntries = ft.getEntries();

        assertEquals(expectedEntries, actualEntries);
    }

    @Test
    public void testToString() {
        FrequencyTable ft = new FrequencyTable();

        ArrayList<FrequencyTableEntry> entries = new ArrayList<>();
        entries.add(new FrequencyTableEntry("Cats", 7));
        entries.add(new FrequencyTableEntry("Dogs", 6));

        ft.setEntries(entries);

        String expectedString = "FrequencyTable{entries=[FrequencyTableEntry{word='cats', frequency=7}, FrequencyTableEntry{word='dogs', frequency=6}]}";
        String actualString = ft.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testIncrementWordDoesntExist() {
        FrequencyTable ft = new FrequencyTable();

        ft.incrementEntry("hello");

        FrequencyTableEntry fte = ft.getEntryWithWord("hello");

        assertEquals(1, fte.getFrequency());
    }

    @Test
    public void testIncrementWord() {
        FrequencyTable ft = new FrequencyTable();
        ft.addEntry(new FrequencyTableEntry("hello", 1));

        ft.incrementEntry("hello");

        FrequencyTableEntry fte = ft.getEntryWithWord("hello");

        assertEquals(2, fte.getFrequency());
    }

    @Test
    public void testGetMostFrequentEntry() {
        FrequencyTable ft = new FrequencyTable();
        ft.addEntry(new FrequencyTableEntry("cat", 7));
        ft.addEntry(new FrequencyTableEntry("dog", 6));

        String mostFrequentEntry = ft.getMostFrequentEntry();

        assertEquals("cat", mostFrequentEntry);
    }
}