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

    public double compareSimilarity(FrequencyTable other) {
        List<Double> percentages = new ArrayList<>();

        for (FrequencyTableEntry entry: this.entries) {
            FrequencyTableEntry matchedEntry = other.getEntryWithWord(entry.word);
            if (matchedEntry == null) {
                percentages.add(0.0);
            } else {
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

    public FrequencyTableEntry getEntryWithWord(String word) {
        for (FrequencyTableEntry entry: entries) {
            if (entry.word.equals(word)) {
                return entry;
            }
        }

        return null;
    }
}