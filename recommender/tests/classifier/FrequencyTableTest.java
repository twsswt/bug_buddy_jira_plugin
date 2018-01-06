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

        assertEquals(1, similarity, 0.01);
    }
}