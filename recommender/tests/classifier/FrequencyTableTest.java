package classifier;

import org.junit.Test;

import static org.junit.Assert.*;

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
}