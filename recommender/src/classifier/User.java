package classifier;

import puller.JiraComment;
import puller.JiraIssue;

import java.util.*;

public class User {
    private String email;
    private FrequencyTable wordTable;
    private List<Skill> skills;

    public User() {
        skills = new ArrayList<>();
    }
    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public FrequencyTable getWordTable() {
        return wordTable;
    }

    public void setWordTable(FrequencyTable wordTable) {
        this.wordTable = wordTable;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                '}';
    }

    /**
     * Builds the frequency table for a collection of issues
     */
    public void buildFrequencyTable(List<JiraIssue> issues) {
        Set<String> allUniqueWords = getAllUniqueWords(issues);
        List<String> allWords = getAllWordsList(issues);

        // Get the frequencies of all unique words
        FrequencyTable table = getFrequencyTableFromUniqueWords(allUniqueWords, allWords);

        this.setWordTable(table);
    }

    /**
     * Gets every word the user has ever written as a
     * space separated string
     */
    private String getAllWords(List<JiraIssue> issues) {
        StringBuilder everyWrittenWordBuilder = new StringBuilder();

        for (JiraIssue issue : issues) {
            if (issue.getAssignee().equals(this.getEmail())) {
                everyWrittenWordBuilder.append(issue.getText()).append(" ");
            }

            for (JiraComment comment : issue.getComments()) {
                if (comment.getAuthor().equals(this.getEmail())) {
                    everyWrittenWordBuilder.append(comment.getBody()).append(" ");
                }
            }
        }

        return everyWrittenWordBuilder.toString();
    }

    public List<String> getAllWordsList(List<JiraIssue> issues) {
        String[] allWordsArray = getEveryWrittenWord(issues);

        return Arrays.asList(allWordsArray);
    }

    public Set<String> getAllUniqueWords(List<JiraIssue> issues) {

        String[] allWordsArray = getEveryWrittenWord(issues);

        List<String> allWords = Arrays.asList(allWordsArray);

        return new HashSet<>(allWords);
    }

    private String[] getEveryWrittenWord(List<JiraIssue> issues) {
        // Get every word the user has ever written
        String everyWrittenWord = getAllWords(issues);

        everyWrittenWord = removePunctuation(everyWrittenWord);

        return everyWrittenWord.split("\\s+");
    }

    private String removePunctuation(String everyWrittenWord) {
        everyWrittenWord = everyWrittenWord.replace(',', ' ').replace('.', ' ').replace('(', ' ').replace(')', ' ');
        everyWrittenWord = everyWrittenWord.replace('"', ' ').replace('>', ' ');

        return everyWrittenWord;
    }

    public FrequencyTable getFrequencyTableFromUniqueWords(Set<String> uniqueWords, List<String> allWords) {

        FrequencyTable table = new FrequencyTable();

        for (String uniqueWord : uniqueWords) {
            int numOccurrences = 0;
            for (String word : allWords) {
                if (word.equals(uniqueWord)) {
                    numOccurrences++;
                }
            }

            FrequencyTableEntry entry = new FrequencyTableEntry(uniqueWord, numOccurrences);

            table.getEntries().add(entry);
        }
        return table;
    }
}
