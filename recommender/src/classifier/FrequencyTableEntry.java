package classifier;

public class FrequencyTableEntry {
    public String word;
    public int frequency;

    public FrequencyTableEntry(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "FrequencyTableEntry{" +
                "word='" + word + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}
