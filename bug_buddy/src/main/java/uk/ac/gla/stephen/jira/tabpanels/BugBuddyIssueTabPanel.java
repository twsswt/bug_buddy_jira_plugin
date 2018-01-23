package uk.ac.gla.stephen.jira.tabpanels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.plugin.issuetabpanel.AbstractIssueTabPanel;
import com.atlassian.jira.plugin.issuetabpanel.IssueTabPanel;
import com.atlassian.jira.issue.tabpanels.GenericMessageAction;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.user.ApplicationUser;
import java.util.Collections;
import java.util.List;

public class BugBuddyIssueTabPanel extends AbstractIssueTabPanel implements IssueTabPanel
{
    private static final Logger log = LoggerFactory.getLogger(BugBuddyIssueTabPanel.class);

    public List getActions(Issue issue, ApplicationUser remoteUser) {
        return Collections.singletonList(new GenericMessageAction("This is where the auto-assign button should be!"));
    }

    public boolean showPanel(Issue issue, ApplicationUser remoteUser)
    {
        return true;
    }
}
