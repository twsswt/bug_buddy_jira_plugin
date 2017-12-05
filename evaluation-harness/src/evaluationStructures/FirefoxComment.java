package evaluationStructures;

/**
 * This class represents a comment stored on the
 * Firefox Bugzilla tracker.
 *
 * It only contains fields that are relevant to auto-assigning issues.
 */
public class FirefoxComment {

    private String authorEmail;
    private String commentText;
    private String creationTime;

    public FirefoxComment() {
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
