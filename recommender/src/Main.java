import puller.JiraIssue;
import puller.Puller;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Puller p = new Puller("localhost", "2990");
        ArrayList<JiraIssue> jiraIssues = p.getAllIssues();
    }
}
