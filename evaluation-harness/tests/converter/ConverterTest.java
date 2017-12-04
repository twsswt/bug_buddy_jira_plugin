package converter;

import evaluationStructures.FirefoxIssue;
import evaluationStructures.JiraIssue;
import evaluationStructures.JiraProject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConverterTest {

    @Test
    public void TestConvertJiraProjectToJiraJSONDefaultProject() throws Exception {
        JiraProject jiraProject = new JiraProject();

        Converter converter = new Converter();

        String actualJson = converter.convertJiraProjectToJiraJSON(jiraProject);

        String expectedJson = "{\"key\":\"FRFX\",\"name\":\"Firefox Issues\",\"projectTypeKey\":\"business\",\"projectTemplateKey\":\"com.atlassian.jira-core-project-templates:jira-core-project-management\",\"description\":\"Firefox data set for recommendations\",\"lead\":\"admin\",\"url\":\"https://atlassian.com\",\"assigneeType\":\"PROJECT_LEAD\",\"avatarId\":10200,\"permissionScheme\":0,\"notificationScheme\":10000}";

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void TestConvertFirefoxIssueToJiraIssue() throws Exception {
        Converter c = new Converter();

        FirefoxIssue fi = new FirefoxIssue();
        fi.setAssigneeEmail("sbrown1992@gmail.com");
        fi.setAssigneeEmail30Days("1106679b@student.gla.ac.uk");
        fi.setBugID(666);
        fi.setComponent("Hairdryer");
        fi.setReporterEmail("tim.storer@gla.ac.uk");

        JiraIssue ji = c.convertFirefoxIssueToJiraIssue(fi);

        assertEquals("", ji.getDescription());
        assertEquals("1106679b@student.gla.ac.uk", ji.getAssigneeName());
        assertEquals("10000", ji.getIssueTypeID());
        assertEquals(c.getProjectKey(), ji.getProjectKey());
        assertEquals("tim.storer@gla.ac.uk", ji.getReporterName());
        assertEquals("666", ji.getSummary());
    }

    @Test
    public void TestConvertJiraIssueToJiraJSON() throws Exception {
        Converter c = new Converter();

        JiraIssue ji = new JiraIssue("666");
        ji.setAssigneeName("1106679b@student.gla.ac.uk");
        ji.setIssueTypeID("10000");
        ji.setProjectKey("FRFX");
        ji.setReporterName("tim.storer@gla.ac.uk");

        String expectedJson = "{\n" +
                "\"fields\":{\n" +
                "\"project\":{\n" +
                "\"key\":\"FRFX\"\n" +
                "},\n" +
                "\"summary\": \"666\",\n" +
                "\"issuetype\":{\n" +
                "\"id\":\"10000\"\n" +
                "},\n" +
                "\"assignee\":{\n" +
                "\"name\":\"1106679b@student.gla.ac.uk\"\n" +
                "},\n" +
                "\"reporter\":{\n" +
                "\"name\":\"tim.storer@gla.ac.uk\"\n" +
                "}\n" +
                "}\n" +
                "}";
        String actualJson = c.convertJiraIssueToJiraJSON(ji);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void TestConstructorSetsProjectKey() throws Exception {
        String projectKey = "PSD";
        Converter c = new Converter(projectKey);

        assertEquals(projectKey, c.getProjectKey());

    }

    @Test
    public void TestConvertEmailAddressToJiraUserJson() throws Exception {
        Converter c = new Converter();

        String expectedJson = "{\n" +
                "\"name\":\"sbrown1992@gmail.com\",\n" +
                "\"password\":\"sbrown1992@gmail.com\",\n" +
                "\"emailAddress\":\"sbrown1992@gmail.com\",\n" +
                "\"displayName\":\"sbrown1992@gmail.com\"\n" +
                "}";
        String actualJson = c.convertEmailAddressToJiraUser("sbrown1992@gmail.com");

        assertEquals(expectedJson, actualJson);
    }
}