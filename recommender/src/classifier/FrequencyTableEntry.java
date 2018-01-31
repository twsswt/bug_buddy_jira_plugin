package classifier;

public class FrequencyTableEntry {
    private String word;
    private int frequency;

    public FrequencyTableEntry(String word, int frequency) {
        this.word = word.toLowerCase();
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word.toLowerCase();
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
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
