package classifier;

import java.util.ArrayList;
import java.util.List;

public class FrequencyTable {

    public ArrayList<FrequencyTableEntry> entries;

    public FrequencyTable() {
        entries = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "FrequencyTable{" +
                "entries=" + entries +
                '}';
    }

    /**
     * Compares how similar this frequency table is with another
     *
     * @param other the other frequency table
     * @return a percentage between 0.00 and 1.00, representing how similar the two frequency tables are
     */
    public double compareSimilarity(FrequencyTable other) {
        List<Double> percentages = new ArrayList<>();

        for (FrequencyTableEntry entry : this.entries) {
            // attempt to find an entry with the same word in the other table
            // if no such entry exists then they have 0% similarity
            FrequencyTableEntry matchedEntry = other.getEntryWithWord(entry.word);
            if (matchedEntry == null) {
                percentages.add(0.0);
            } else {

                // Calculate each frequency as a percentage of their overall occurrences
                double thisFrequencyPercentage = (entry.frequency / (double) this.entries.size()) / 100;
                double otherFrequencyPercentage = (matchedEntry.frequency / (double) other.entries.size()) / 100;
                double averageFrequencyPercentage = (thisFrequencyPercentage + otherFrequencyPercentage) / 2.0;
                percentages.add(averageFrequencyPercentage);
            }
        }

        // Get average
        double total = 0.0;
        for (double percentage : percentages) {
            total += percentage;
        }
        return total / (double) this.entries.size();
    }

    /**
     * Gets the entry of the frequency table with the specified word
     */
    public FrequencyTableEntry getEntryWithWord(String word) {
        for (FrequencyTableEntry entry : entries) {
            if (entry.word.equals(word)) {
                return entry;
            }
        }

        return null;
    }
}