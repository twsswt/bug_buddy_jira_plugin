# bug_buddy_jira_plugin

## Install Instructions
1. Clone the repository
1. cd into the repository
1. Run setup.sh to download the atlassian sdk, if it hasn't already been installed
1. cd into jira-hello-world/myPlugin
1. run `atlas-run`

## How to generate and run the data-gatherer binary
1. Open IntelliJ Idea
1. Add a jar in 'project structure' -> 'artifacts'
1. Build artifacts
1. Find the jar in out/artifacts/data\_gatherer\_jar
1. Run the jar using `java -jar data-gatherer.jar`

## Evaluation Plan
1. Post a set of X issues to JIRA using the 'evaluation-harness' codebase
1. Download a set of X issues from JIRA using the 'recommender' codebase
1. For every issue in set X, build an alternative set X2 containing every issue in X bar the issue itself
1. Run the recommendation algorithm, using X2 as the training set. Get it to recommend an assignee for the issue, and record how accurate it was
1. After this has happened for all issues in X
1. Output the data in the form IssueID -> actualAssignee -> recommenderAssignee
1. Also output some summary statistics (eg average accuracy)
