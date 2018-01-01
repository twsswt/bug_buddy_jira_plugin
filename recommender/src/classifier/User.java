package classifier;

public class User {
    public String email;
    public FrequencyTable wordTable;

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                '}';
    }
}
