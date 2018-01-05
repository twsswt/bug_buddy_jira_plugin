package classifier;

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
}
