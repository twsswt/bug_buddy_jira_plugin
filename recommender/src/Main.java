import puller.JiraComment;
import puller.JiraIssue;
import puller.Puller;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Main {

    private static final String testCsvFilename = "test.csv";
    private static final String trainingCsvFilename = "training.csv";

    public static void main(String[] args) {
        Puller p = new Puller("localhost", "2990");
        ArrayList<JiraIssue> jiraIssues = p.getAllIssues();

        writeTestSetToCSV(jiraIssues);
        writeTrainingSetToCSV(jiraIssues);

        try {
            DataSource testCsvSource = new DataSource(testCsvFilename);
            DataSource trainingCsvSource = new DataSource(trainingCsvFilename);
            Instances testData = testCsvSource.getDataSet();
            Instances trainingData = trainingCsvSource.getDataSet();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeTestSetToCSV(ArrayList<JiraIssue> issues) {
        try {
            FileWriter writer = new FileWriter(new File(testCsvFilename));

            writer.write("Reporter,Assignee,AllCommentText\n");

            for (JiraIssue issue: issues) {
                StringBuilder line = new StringBuilder();
                line.append(issue.reporter);
                line.append(",");
                line.append(issue.assignee);
                line.append(",");

                StringBuilder allComments = new StringBuilder();
                for (JiraComment comment: issue.comments) {
                    String commentBody = comment.body;
                    commentBody = commentBody.replace(',', ' ');
                    commentBody = commentBody.replace('\n', ' ');
                    allComments.append(commentBody);
                }

                line.append(allComments);
                line.append("\n");

                writer.write(line.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeTrainingSetToCSV(ArrayList<JiraIssue> issues) {
        try {
            FileWriter writer = new FileWriter(new File(trainingCsvFilename));

            writer.write("Reporter,Assignee,AllCommentText\n");

            for (int i = 1; i < issues.size(); i++) {
                JiraIssue issue = issues.get(i);
                StringBuilder line = new StringBuilder();
                line.append(issue.reporter);
                line.append(",");
                line.append(issue.assignee);
                line.append(",");

                StringBuilder allComments = new StringBuilder();
                for (JiraComment comment: issue.comments) {
                    String commentBody = comment.body;
                    commentBody = commentBody.replace(',', ' ');
                    commentBody = commentBody.replace('\n', ' ');
                    allComments.append(commentBody);
                }

                line.append(allComments);
                line.append("\n");

                writer.write(line.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
