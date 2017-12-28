import puller.Puller;

public class Main {

    public static void main(String[] args) {
        Puller p = new Puller("localhost", "2990");
        p.getAllIssues();
        System.out.println("Hello World!");
    }
}
