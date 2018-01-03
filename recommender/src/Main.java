import classifier.FrequencyTable;
import classifier.FrequencyTableEntry;
import classifier.User;
import puller.JiraComment;
import puller.JiraIssue;
import puller.Puller;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Get all issues from a jira instance
        Puller p = new Puller("localhost", "2990");
        ArrayList<JiraIssue> jiraIssues = p.getAllIssues();

        // Build all frequency tables
        List<User> allUsers = identifyAllUsers(jiraIssues);

        for (int i = 0; i < allUsers.size(); i++) {
            buildFrequencyTable(allUsers.get(i), jiraIssues);
            System.out.println("Built Frequency Table for user " + i);
        }

        // Input a test issue, to be automatically assigned to a user
        System.out.print("Enter an issue: ");
        StringBuilder issueText = new StringBuilder();
        Scanner reader = new Scanner(System.in);
        while (reader.hasNextLine()) {
            issueText.append(reader.nextLine());
        }
        reader.close();

        // Build frequency table for test issue
        JiraIssue newIssue = new JiraIssue();
        newIssue.setText(issueText.toString());
        newIssue.setAssignee("newissue@newissue.com");
        ArrayList<JiraIssue> newIssueList = new ArrayList<>();
        newIssueList.add(newIssue);
        User newUser = new User();
        newUser.setEmail("newissue@newissue.com");
        buildFrequencyTable(newUser, newIssueList);

        System.out.println(newUser.getWordTable());

        // Find the closest match between our new frequency table and all other frequency tables...
        FrequencyTable issueTable = newUser.getWordTable();

        double maxMatch = 0;
        int maxIndex = 0;

        for (int i = 0; i < allUsers.size(); i++) {
            double similarity = issueTable.compareSimilarity(allUsers.get(i).getWordTable());
            System.out.println("i: " + i + " sim " + similarity);

            if (similarity > maxMatch) {
                maxMatch = similarity;
                maxIndex = i;
            }
        }

        System.out.println("Max match: " + maxMatch);
        System.out.println("Max index: " + maxIndex);
        System.out.println("We Recommend you assign this issue to");
        System.out.println(allUsers.get(maxIndex).getEmail());

    }


    /**
     * Builds the frequency table for a single user for a collection of issues
     */
    public static void buildFrequencyTable(User user, ArrayList<JiraIssue> issues) {
        // Get every word the user has ever said
        StringBuilder everyWrittenWordBuilder = new StringBuilder();

        for (JiraIssue issue : issues) {
            if (issue.getAssignee().equals(user.getEmail())) {
                everyWrittenWordBuilder.append(issue.getText()).append(" ");
            }

            for (JiraComment comment : issue.getComments()) {
                if (comment.getAuthor().equals(user.getEmail())) {
                    everyWrittenWordBuilder.append(comment.getBody()).append(" ");
                }
            }
        }

        // Remove all the whitespace and punctuation
        String everyWrittenWord = everyWrittenWordBuilder.toString();
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

        user.setWordTable(table);
    }

    /**
     * Identify all the users in a collection of issues
     *
     * @return A list of users involved in the issue collection
     */
    public static List<User> identifyAllUsers(ArrayList<JiraIssue> issues) {
        // Find all Unique Emails
        Set<String> allUniqueEmails = new HashSet<>();
        for (JiraIssue issue : issues) {
            allUniqueEmails.add(issue.getReporter());
            allUniqueEmails.add(issue.getAssignee());

            for (JiraComment comment : issue.getComments()) {
                allUniqueEmails.add(comment.getAuthor());
            }
        }

        // Create a list of user objects using all unique emails
        List<User> allUsers = new ArrayList<>();
        for (String email : allUniqueEmails) {
            User u = new User();
            u.setEmail(email);
            u.setWordTable(new FrequencyTable());
            allUsers.add(u);
        }

        return allUsers;
    }
}
