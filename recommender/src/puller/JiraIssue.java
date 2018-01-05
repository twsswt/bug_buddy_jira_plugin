package puller;

import java.util.ArrayList;

public class JiraIssue {

    private String text;
    private String reporter;
    private String assignee;
    private String id;
    private ArrayList<JiraComment> comments;

    public JiraIssue() {
        text = "N/A";
        reporter = "N/A";
        assignee = "N/A";
        id = "N/A";
        comments = new ArrayList<>();
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<JiraComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<JiraComment> comments) {
        this.comments = comments;
    }

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
}
