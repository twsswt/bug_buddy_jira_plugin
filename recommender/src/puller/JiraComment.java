package puller;

public class JiraComment {
    public String author;
    public String body;
    public String date;

    @Override
    public String toString() {
        return "JiraComment{" +
                "author='" + author + '\'' +
                ", body='" + body + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
