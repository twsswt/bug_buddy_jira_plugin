package puller;

public class JiraComment {
    @Override
    public String toString() {
        return "JiraComment{" +
                "author='" + author + '\'' +
                ", body='" + body + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String author;
    public String body;
    public String date;
}
