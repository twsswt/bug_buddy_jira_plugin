package classifier;

public class FrequencyTableEntry {
    public String word;

    @Override
    public String toString() {
        return "FrequencyTableEntry{" +
                "word='" + word + '\'' +
                ", frequency=" + frequency +
                '}';
    }

    public int frequency;

    public FrequencyTableEntry(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }
}
