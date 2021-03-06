package evaluationStructures;

import org.junit.Test;

import static org.junit.Assert.*;

public class JiraProjectTest {

    @Test
    public void testSetKey() {
        JiraProject jiraProject = new JiraProject();
        String expectedKey = "FRFX2";
        jiraProject.setKey(expectedKey);

        String actualKey = jiraProject.getKey();

        assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testSetName() {
        JiraProject jiraProject = new JiraProject();
        String expectedName = "FirefoxQuantum";
        jiraProject.setName(expectedName);

        String actualName = jiraProject.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void testSetProjectTypeKey() {
        JiraProject jiraProject = new JiraProject();
        String expectedProjectTypeKey = "charity";
        jiraProject.setProjectTypeKey(expectedProjectTypeKey);

        String actualProjectTypeKey = jiraProject.getProjectTypeKey();

        assertEquals(expectedProjectTypeKey, actualProjectTypeKey);
    }

    @Test
    public void testSetProjectTemplateKey() {
        JiraProject jiraProject = new JiraProject();
        String expectedProjectTemplateKey = "com.atlassian.jira-core-project-templates:jira-core-charity";
        jiraProject.setProjectTemplateKey(expectedProjectTemplateKey);

        String actualProjectTemplateKey = jiraProject.getProjectTemplateKey();

        assertEquals(expectedProjectTemplateKey, actualProjectTemplateKey);
    }

    @Test
    public void testSetDescription() {
        JiraProject jiraProject = new JiraProject();
        String expectedDescription = "Super fast Firefox";
        jiraProject.setDescription(expectedDescription);

        String actualDescription = jiraProject.getDescription();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testSetLead() {
        JiraProject jiraProject = new JiraProject();
        String expectedLead = "StephenBrown";
        jiraProject.setLead(expectedLead);

        String actualLead = jiraProject.getLead();

        assertEquals(expectedLead, actualLead);
    }

    @Test
    public void testSetUrl() {
        JiraProject jiraProject = new JiraProject();
        String expectedURL = "https://gitlab.com/u/sbrown1992";
        jiraProject.setUrl(expectedURL);

        String actualUrl = jiraProject.getUrl();

        assertEquals(expectedURL, actualUrl);
    }

    @Test
    public void testSetAssigneeType() {
        JiraProject jiraProject = new JiraProject();
        String expectedAssigneeType = "PROJECT_LEADER";
        jiraProject.setAssigneeType(expectedAssigneeType);

        String actualAssigneeType = jiraProject.getAssigneeType();

        assertEquals(expectedAssigneeType, actualAssigneeType);
    }

    @Test
    public void testSetAvatarID() {
        JiraProject jiraProject = new JiraProject();
        int expectedAvatarID = 666;
        jiraProject.setAvatarID(expectedAvatarID);

        int actualAvatarID = jiraProject.getAvatarID();

        assertEquals(expectedAvatarID, actualAvatarID);
    }

    @Test
    public void testSetPermissionScheme() {
        JiraProject jiraProject = new JiraProject();
        int expectedPermissionScheme = 666;
        jiraProject.setPermissionScheme(expectedPermissionScheme);

        int actualPermissionScheme = jiraProject.getPermissionScheme();

        assertEquals(expectedPermissionScheme, actualPermissionScheme);
    }

    @Test
    public void testSetNotificationScheme() {
        JiraProject jiraProject = new JiraProject();
        int expectedNotificationScheme = 666;
        jiraProject.setNotificationScheme(expectedNotificationScheme);

        int actualNotificationScheme = jiraProject.getNotificationScheme();

        assertEquals(expectedNotificationScheme, actualNotificationScheme);
    }
}