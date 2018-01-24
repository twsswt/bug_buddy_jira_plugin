package evaluationStructures;

/**
 * This class represents a project in JIRA.
 */
public class JiraProject {
    private String key;
    private String name;
    private String projectTypeKey;
    private String projectTemplateKey;
    private String description;
    private String lead;
    private String url;
    private String assigneeType;
    private int avatarId;
    private int permissionScheme;
    private int notificationScheme;

    public JiraProject() {
        key = "FRFX";
        name = "Firefox Issues";
        projectTypeKey = "business";
        projectTemplateKey = "com.atlassian.jira-core-project-templates:jira-core-project-management";
        description = "Firefox data set for recommendations";
        lead = "admin";
        url = "https://atlassian.com";
        assigneeType = "PROJECT_LEAD";
        avatarId = 10200;
        permissionScheme = 0;
        notificationScheme = 10000;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectTypeKey() {
        return projectTypeKey;
    }

    public void setProjectTypeKey(String projectTypeKey) {
        this.projectTypeKey = projectTypeKey;
    }

    public String getProjectTemplateKey() {
        return projectTemplateKey;
    }

    public void setProjectTemplateKey(String projectTemplateKey) {
        this.projectTemplateKey = projectTemplateKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAssigneeType() {
        return assigneeType;
    }

    public void setAssigneeType(String assigneeType) {
        this.assigneeType = assigneeType;
    }

    public int getAvatarID() {
        return avatarId;
    }

    public void setAvatarID(int avatarID) {
        this.avatarId = avatarID;
    }

    public int getPermissionScheme() {
        return permissionScheme;
    }

    public void setPermissionScheme(int permissionScheme) {
        this.permissionScheme = permissionScheme;
    }

    public int getNotificationScheme() {
        return notificationScheme;
    }

    public void setNotificationScheme(int notificationScheme) {
        this.notificationScheme = notificationScheme;
    }
}
