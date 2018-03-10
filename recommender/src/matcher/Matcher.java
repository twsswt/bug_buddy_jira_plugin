package matcher;

import classifier.FrequencyTable;
import classifier.FrequencyTableEntry;
import classifier.Skill;
import classifier.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import puller.JiraIssue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Matcher {

    private static final Logger logger = LogManager.getLogger(Matcher.class);

    private MatchingAlgorithm matchingAlgorithm;
    private List<Skill> globalSkills;

    public MatchingAlgorithm getMatchingAlgorithm() {
        return matchingAlgorithm;
    }

    public void setMatchingAlgorithm(MatchingAlgorithm matchingAlgorithm) {
        this.matchingAlgorithm = matchingAlgorithm;
    }

    public List<Skill> getGlobalSkills() {
        return globalSkills;
    }

    public void setGlobalSkills(List<Skill> globalSkills) {
        this.globalSkills = globalSkills;
    }

    /**
     * Initialises the global skills list with all skills
     * we are searching for
     */
    public void initialiseGlobalSkills() {
        globalSkills = new ArrayList<>();

        // http://www.teletec.f9.co.uk/networking/general/keywords.htm
        Skill networking = new Skill("networking");
        networking.addKeyword("TCP");
        networking.addKeyword("UDP");
        networking.addKeyword("IP");
        networking.addKeyword("connection");
        networking.addKeyword("firewall");
        networking.addKeyword("intranet");
        networking.addKeyword("ISDN");
        networking.addKeyword("LAN");
        networking.addKeyword("node");
        networking.addKeyword("router");
        networking.addKeyword("WAN");
        networking.addKeyword("packet");
        networking.addKeyword("server");
        networking.addKeyword("DNS");
        globalSkills.add(networking);

        // https://css-tricks.com/web-nerd-terminology-explained/
        Skill frontend = new Skill("frontend");
        frontend.addKeyword("GUI");
        frontend.addKeyword("color");
        frontend.addKeyword("colour");
        frontend.addKeyword("browser");
        frontend.addKeyword("HTML");
        frontend.addKeyword("CSS");
        frontend.addKeyword("JavaScript");
        frontend.addKeyword("JS");
        frontend.addKeyword("URL");
        frontend.addKeyword("tag");
        frontend.addKeyword("anchor");
        frontend.addKeyword("wrapping");
        frontend.addKeyword("nesting");
        frontend.addKeyword("markup");
        frontend.addKeyword("accessibility");
        frontend.addKeyword("usability");
        frontend.addKeyword("validation");
        frontend.addKeyword("standards");
        frontend.addKeyword("semantics");
        frontend.addKeyword("rendering");
        frontend.addKeyword("DOCTYPE");
        frontend.addKeyword("kerning");
        frontend.addKeyword("elastic");
        frontend.addKeyword("framework");
        frontend.addKeyword("AJAX");
        frontend.addKeyword("SEO");
        frontend.addKeyword("TLD");
        frontend.addKeyword("DOM");
        frontend.addKeyword("RGB");
        frontend.addKeyword("CYMK");
        frontend.addKeyword("RSS");
        frontend.addKeyword("DPI");
        globalSkills.add(frontend);

        // http://www.stroustrup.com/glossary.html
        Skill cpp = new Skill("c-plus-plus");
        cpp.addKeyword("method");
        cpp.addKeyword("exception");
        cpp.addKeyword("solution");
        cpp.addKeyword("header");
        cpp.addKeyword("class");
        cpp.addKeyword("address");
        cpp.addKeyword("algorithm");
        cpp.addKeyword("application");
        cpp.addKeyword("array");
        cpp.addKeyword("memory");
        cpp.addKeyword("binary");
        cpp.addKeyword("bit");
        cpp.addKeyword("byte");
        cpp.addKeyword("catch");
        cpp.addKeyword("code");
        cpp.addKeyword("compiler");
        cpp.addKeyword("constructor");
        cpp.addKeyword("deprecated");
        cpp.addKeyword("dynamic");
        cpp.addKeyword("encapsulation");
        cpp.addKeyword("executable");
        cpp.addKeyword("program");
        cpp.addKeyword("type");
        cpp.addKeyword("extension");
        cpp.addKeyword("file");
        cpp.addKeyword("float");
        cpp.addKeyword("free");
        cpp.addKeyword("function");
        cpp.addKeyword("garbage");
        cpp.addKeyword("handler");
        cpp.addKeyword("implementation");
        cpp.addKeyword("inheritance");
        cpp.addKeyword("linker");
        cpp.addKeyword("locale");
        cpp.addKeyword("member");
        cpp.addKeyword("namespace");
        cpp.addKeyword("null");
        cpp.addKeyword("object");
        cpp.addKeyword("operator");
        cpp.addKeyword("parameter");
        cpp.addKeyword("pointer");
        cpp.addKeyword("protected");
        cpp.addKeyword("public");
        cpp.addKeyword("private");
        cpp.addKeyword("recursion");
        cpp.addKeyword("reference");
        cpp.addKeyword("self");
        cpp.addKeyword("signature");
        cpp.addKeyword("stack");
        cpp.addKeyword("static");
        cpp.addKeyword("string");
        cpp.addKeyword("template");
        cpp.addKeyword("testing");
        cpp.addKeyword("variable");
        cpp.addKeyword("virtual");
        cpp.addKeyword("void");
        globalSkills.add(cpp);

        // https://raima.com/database-terminology/
        Skill databases = new Skill("databases");
        databases.addKeyword("databases");
        databases.addKeyword("SQL");
        databases.addKeyword("cache");
        databases.addKeyword("key");
        databases.addKeyword("column");
        databases.addKeyword("connection");
        databases.addKeyword("deadlock");
        databases.addKeyword("index");
        databases.addKeyword("join");
        databases.addKeyword("optimizer");
        databases.addKeyword("integrity");
        databases.addKeyword("query");
        databases.addKeyword("record");
        databases.addKeyword("relational");
        databases.addKeyword("replication");
        databases.addKeyword("schema");
        databases.addKeyword("transaction");
        globalSkills.add(databases);

    }

    public boolean recommendUserAndValidate(List<JiraIssue> jiraIssues, JiraIssue issueBeingRecommended) {
        // Separate the entire set of issues into two sets
        // One set containing only the issue we wish to test recommendations on
        // One set containing all other issues in the data set
        List<JiraIssue> otherIssues = new ArrayList<>(jiraIssues);
        otherIssues.remove(issueBeingRecommended);

        JiraIssue testIssue = new JiraIssue();
        testIssue.setText(issueBeingRecommended.getComments().get(0).getBody());
        testIssue.setAssignee("newissue@newissue.com");
        testIssue.setReporter(issueBeingRecommended.getReporter());

        // Identify all users who are candidates for assignment
        List<User> allUsers = identifyAllAssignableUsers(otherIssues);

        buildAllFrequencyTables(allUsers, otherIssues);

        String recommendedEmail = "";

        switch (matchingAlgorithm) {
            case WORD_BASED: {
                recommendedEmail = findClosestMatchWordBased(testIssue, allUsers);
                break;
            }
            case SKILLS_BASED: {

                for (User user : allUsers) {
                    identifySkillsFromFrequencyTable(user, globalSkills);
                }

                recommendedEmail = findClosestMatchSkillBased(testIssue, allUsers);
                break;
            }
            case MOST_ASSIGNED: {
                recommendedEmail = findClosestMatchMostAssigned(otherIssues);
                break;
            }
            default: {
                logger.warn("Please choose a recommendation method");
                break;
            }
        }

        logger.info("We recommend you assign this issue to " + recommendedEmail);

        return recommendedEmail.equals(issueBeingRecommended.getAssignee());
    }

    /**
     * Creates a dummy user with a frequency table for the issue being assigned a
     * recommendee
     */
    private User createDummyUserForIssueBeingAssigned(JiraIssue issue) {
        ArrayList<JiraIssue> issueList = new ArrayList<>();
        issueList.add(issue);
        User issueUser = new User();
        issueUser.setEmail("newissue@newissue.com");
        issueUser.buildFrequencyTable(issueList);

        return issueUser;
    }


    /**
     * Identifies the skills a user has, from the global set we wish to match on, based
     * on the users frequency of words
     *
     * @param user         the user who's skills we wish to identify
     * @param globalSkills the skills we wish to match on
     */
    private void identifySkillsFromFrequencyTable(User user, List<Skill> globalSkills) {
        FrequencyTable ft = user.getWordTable();

        for (Skill skill : globalSkills) {
            int keywordHits = 0;

            for (String keyword : skill.getKeywords()) {
                FrequencyTableEntry entry = ft.getEntryWithWord(keyword);
                if (entry != null) {
                    keywordHits += entry.getFrequency();
                }
            }

            // If the number of occurrences of a skills keyword is higher than a threshold, then the user
            // is considered to have that skill
            double threshold = 0.001;
            double skillPercentage = keywordHits / (double) ft.getTotalWords();

            if (skillPercentage > threshold) {
                List<Skill> updatedSkillsList = user.getSkills();
                updatedSkillsList.add(skill);
                user.setSkills(updatedSkillsList);
            }
        }
    }

    /**
     * Identify all the users in a collection of issues
     *
     * @return A list of users involved in the issue collection
     */
    private List<User> identifyAllAssignableUsers(List<JiraIssue> issues) {
        // Find all Unique Emails that have previously had an issue assigned to them
        Set<String> allUniqueEmails = new HashSet<>();
        for (JiraIssue issue : issues) {
            allUniqueEmails.add(issue.getAssignee());
        }

        // Create a list of user objects using all unique emails
        List<User> allUsers = new ArrayList<>();
        for (String email : allUniqueEmails) {
            User u = new User();
            u.setEmail(email);
            u.setWordTable(new FrequencyTable());
            allUsers.add(u);
        }

        return allUsers;
    }

    /**
     * Builds a frequency table for all users
     */
    private void buildAllFrequencyTables(List<User> users, List<JiraIssue> issues) {
        for (User user : users) {
            user.buildFrequencyTable(issues);
        }
    }


    /**
     * Finds the closest match between an issue and a list of users, by
     * making use of their skills
     * <p>
     * Returns the email of the closest match
     */
    private String findClosestMatchSkillBased(JiraIssue issue, List<User> users) {
        User issueUser = createDummyUserForIssueBeingAssigned(issue);

        identifySkillsFromFrequencyTable(issueUser, globalSkills);

        List<Skill> skillsRequired = issueUser.getSkills();

        User bestUser = null;
        int skillsMatched = 0;

        for (User user : users) {
            int thisSkillsMatched = 0;
            List<Skill> userSkills = user.getSkills();

            // Find how many matching skills this user has
            for (Skill requiredSkill : skillsRequired) {
                if (userSkills.contains(requiredSkill)) {
                    thisSkillsMatched++;
                }
            }

            // Record this user, if their skills are a better match
            if (thisSkillsMatched > skillsMatched) {
                bestUser = user;
                skillsMatched = thisSkillsMatched;
            }
        }

        return bestUser == null ? "" : bestUser.getEmail();
    }


    /**
     * Finds the 'closest match' by recommending the user who has
     * been assigned to the most issues.
     * @param issues the other issues we are basing the recommendation from
     * @return the email of the closest match
     */
    private String findClosestMatchMostAssigned(List<JiraIssue> issues) {

        FrequencyTable ft = new FrequencyTable();
        for (JiraIssue issue : issues) {
            ft.incrementEntry(issue.getAssignee());
        }

        return ft.getMostFrequentEntry();
    }
    /**
     * Finds the closest match between an issue and a list of users, by
     * making use of their frequency tables.
     * <p>
     * Returns the email of the closest match
     */
    private String findClosestMatchWordBased(JiraIssue issue, List<User> users) {

        User issueUser = createDummyUserForIssueBeingAssigned(issue);
        FrequencyTable issueTable = issueUser.getWordTable();

        // Identify the best match between this issues frequency table and all other frequency tables
        double maxMatch = 0;
        int maxIndex = 0;

        for (int i = 0; i < users.size(); i++) {
            double similarity = issueTable.compareSimilarity(users.get(i).getWordTable());

            if (similarity > maxMatch) {
                maxMatch = similarity;
                maxIndex = i;
            }
        }

        return users.get(maxIndex).getEmail();
    }

    /**
     * This stores the class of matching algorithm to be used
     * when assigning users to issues
     */
    public enum MatchingAlgorithm {
        WORD_BASED, SKILLS_BASED, MOST_ASSIGNED
    }
}
