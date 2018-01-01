import classifier.FrequencyTable;
import classifier.FrequencyTableEntry;
import classifier.User;
import puller.JiraComment;
import puller.JiraIssue;
import puller.Puller;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Puller p = new Puller("localhost", "2990");
        ArrayList<JiraIssue> jiraIssues = p.getAllIssues();

        List<User> allUsers = identifyAllUsers(jiraIssues);

        for (int i = 0; i < allUsers.size(); i++) {
            buildFrequencyTable(allUsers.get(i), jiraIssues);
            System.out.println("Built Frequency Table for user " + i);
        }

    }

    public static void buildFrequencyTable(User user, ArrayList<JiraIssue> issues) {
        // Get every word the user has ever said
        StringBuilder everyWrittenWordBuilder = new StringBuilder();

        for (JiraIssue issue : issues) {
            if (issue.assignee.equals(user.email)) {
                everyWrittenWordBuilder.append(issue.text).append(" ");
            }

            for (JiraComment comment : issue.comments) {
                if (comment.author.equals(user.email)) {
                    everyWrittenWordBuilder.append(comment.body).append(" ");
                }
            }
        }

        // Remove all the whitespace and punctuation
        String everyWrittenWord = everyWrittenWordBuilder.toString();
        everyWrittenWord = everyWrittenWord.replace(',', ' ').replace('.', ' ').replace('(', ' ').replace(')', ' ');
        everyWrittenWord = everyWrittenWord.replace('"', ' ').replace('>', ' ');
        String[] allWordsArray = everyWrittenWord.split("\\s+");
        List<String> allWords = Arrays.asList(allWordsArray);

        // Get every unique word
        Set<String> allUniqueWords = new HashSet<>(allWords);

        FrequencyTable table = new FrequencyTable();

        // Get Frequencies
        for (String uniqueWord : allUniqueWords) {
            int numOccurrences = 0;
            for (String word : allWords) {
                if (word.equals(uniqueWord)) {
                    numOccurrences++;
                }
            }

            FrequencyTableEntry entry = new FrequencyTableEntry(uniqueWord, numOccurrences);

            table.entries.add(entry);
        }

        user.wordTable = table;
    }

    public static List<User> identifyAllUsers(ArrayList<JiraIssue> issues) {
        // Find all Unique Emails
        Set<String> allUniqueEmails = new HashSet<>();
        for (JiraIssue issue : issues) {
            allUniqueEmails.add(issue.reporter);
            allUniqueEmails.add(issue.assignee);

            for (JiraComment comment : issue.comments) {
                allUniqueEmails.add(comment.author);
            }
        }

        // Create a list of user objects using all unique emails
        List<User> allUsers = new ArrayList<>();
        for (String email : allUniqueEmails) {
            User u = new User();
            u.email = email;
            u.wordTable = new FrequencyTable();
            allUsers.add(u);
        }

        return allUsers;
    }
}
