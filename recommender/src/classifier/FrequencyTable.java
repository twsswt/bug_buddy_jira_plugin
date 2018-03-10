package classifier;

import java.util.ArrayList;
import java.util.List;

/**
 * A FrequencyTable provides a list of Strings, along with the number
 * of times they appear
 */
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

        int thisTotalWords = this.getTotalWords();
        int otherTotalWords = other.getTotalWords();

        for (FrequencyTableEntry entry : this.entries) {
            // attempt to find an entry with the same word in the other table
            // if no such entry exists then they have 0% similarity
            FrequencyTableEntry matchedEntry = other.getEntryWithWord(entry.getWord());
            if (matchedEntry == null) {
                percentages.add(0.0);
            } else {

                // Calculate each frequency as a percentage of their overall occurrences
                double thisWordFrequencyPercentage = entry.getFrequency() / (double) thisTotalWords;
                double otherWordFrequencyPercentage = matchedEntry.getFrequency() / (double) otherTotalWords;
                double averageWordFrequencyPercentage = (thisWordFrequencyPercentage + otherWordFrequencyPercentage) / 2.0;
                percentages.add(averageWordFrequencyPercentage);
            }
        }

        // Get total similarity
        double total = 0.0;
        for (double percentage : percentages) {
            total += percentage;
        }
        return total;
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

    /**
     * Adds an entry to the table
     */
    public void addEntry(FrequencyTableEntry entry) {
        entries.add(entry);
    }

    /**
     * Gets the total number of words in this frequency table
     */
    public int getTotalWords() {
        int total = 0;
        for (FrequencyTableEntry entry : entries) {
            total += entry.getFrequency();
        }
        return total;
    }

    /**
     * Increases the number of occurrences of a word by one
     * @param word the word we want to increment
     */
    public void incrementEntry(String word) {
        FrequencyTableEntry currentEntry = this.getEntryWithWord(word);

        if (currentEntry == null) {
            this.addEntry(new FrequencyTableEntry(word, 1));
        } else {
            this.entries.remove(currentEntry);
            currentEntry.setFrequency(currentEntry.getFrequency()+1);
            this.addEntry(currentEntry);
        }
    }

    /**
     * Returns the most frequent entry in the frequency table
     * @return the most frequent entry
     */
    public String getMostFrequentEntry() {
        String largestWord = "";
        int largestFrequency = 0;

        for (FrequencyTableEntry entry: this.entries) {
            if (entry.getFrequency() > largestFrequency) {
                largestWord = entry.getWord();
                largestFrequency = entry.getFrequency();
            }
        }

        return largestWord;
    }
}