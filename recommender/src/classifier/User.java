package classifier;

import puller.JiraComment;
import puller.JiraIssue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {
    private String email;
    private FrequencyTable wordTable;

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
        // Get every word the user has ever written
        String everyWrittenWord = getAllWords(issues);

        // Remove all the whitespace and punctuation
        everyWrittenWord = everyWrittenWord.replace(',', ' ').replace('.', ' ').replace('(', ' ').replace(')', ' ');
        everyWrittenWord = everyWrittenWord.replace('"', ' ').replace('>', ' ');

        // Get every unique word the user has ever said
        String[] allWordsArray = everyWrittenWord.split("\\s+");
        List<String> allWords = Arrays.asList(allWordsArray);

        Set<String> allUniqueWords = new HashSet<>(allWords);

        // Get the frequencies of all unique words
        FrequencyTable table = new FrequencyTable();

        for (String uniqueWord : allUniqueWords) {
            int numOccurrences = 0;
            for (String word : allWords) {
                if (word.equals(uniqueWord)) {
                    numOccurrences++;
                }
            }

            FrequencyTableEntry entry = new FrequencyTableEntry(uniqueWord, numOccurrences);

            table.getEntries().add(entry);
        }

        this.setWordTable(table);
    }

    /**
     * Gets every word the user has ever written as a
     * space seperated string
     */
    public String getAllWords(List<JiraIssue> issues) {
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
}
