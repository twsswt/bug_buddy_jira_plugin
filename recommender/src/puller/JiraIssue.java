package puller;

import java.util.ArrayList;

public class JiraIssue {

    @Override
    public String toString() {
        return "JiraIssue{" +
                "text='" + text + '\'' +
                ", reporter='" + reporter + '\'' +
                ", assignee='" + assignee + '\'' +
                ", id='" + id + '\'' +
                ", comments=" + comments +
                '}';
    }

    public String text = "DOOT DOOT";
    public String reporter = "DOOT DOOT";
    public String assignee = "DOOT DOOT";
    public String id = "DOOT DOOT";
    public ArrayList<JiraComment> comments = new ArrayList<>();
}
