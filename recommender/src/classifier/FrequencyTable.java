package classifier;

import java.util.ArrayList;

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
}