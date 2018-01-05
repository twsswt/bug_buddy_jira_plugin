package classifier;

import java.util.ArrayList;
import java.util.List;

public class FrequencyTable {

    private ArrayList<FrequencyTableEntry> entries;

    public FrequencyTable() {
        entries = new ArrayList<>();
    }

    public ArrayList<FrequencyTableEntry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<FrequencyTableEntry> entries) {
        this.entries = entries;
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
            FrequencyTableEntry matchedEntry = other.getEntryWithWord(entry.getWord());
            if (matchedEntry == null) {
                percentages.add(0.0);
            } else {

                // Calculate each frequency as a percentage of their overall occurrences
                double thisWordFrequencyPercentage = (entry.getFrequency() / (double) this.entries.size()) / 100;
                double otherWordFrequencyPercentage = (matchedEntry.getFrequency() / (double) other.entries.size()) / 100;
                double averageWordFrequencyPercentage = (thisWordFrequencyPercentage + otherWordFrequencyPercentage) / 2.0;
                percentages.add(averageWordFrequencyPercentage);
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
            if (entry.getWord().equals(word)) {
                return entry;
            }
        }

        return null;
    }
}