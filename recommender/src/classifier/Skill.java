package classifier;

import java.util.ArrayList;
import java.util.List;

public class Skill {

    private String name;
    private List<String> keywords;

    public Skill(String name) {
        this.name = name;
        this.keywords = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void addKeyword(String keyword) {
        keywords.add(keyword);
    }

    @Override
    public String toString() {
        return "Skill{" +
                "name='" + name + '\'' +
                ", keywords=" + keywords +
                '}';
    }
}
