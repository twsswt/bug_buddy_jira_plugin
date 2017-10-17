# Create project
curl -D- -u admin:admin -X POST --data @sample-data/project-json.txt -H "Content-Type: application/json" http://localhost:2990/jira/rest/api/2/project
# Create Issues
curl -D- -u admin:admin -X POST --data @sample-data/sample1.txt -H "Content-Type: application/json" http://localhost:2990/jira/rest/api/2/issue
curl -D- -u admin:admin -X POST --data @sample-data/sample2.txt -H "Content-Type: application/json" http://localhost:2990/jira/rest/api/2/issue
curl -D- -u admin:admin -X POST --data @sample-data/sample3.txt -H "Content-Type: application/json" http://localhost:2990/jira/rest/api/2/issue
curl -D- -u admin:admin -X POST --data @sample-data/sample4.txt -H "Content-Type: application/json" http://localhost:2990/jira/rest/api/2/issue
curl -D- -u admin:admin -X POST --data @sample-data/sample5.txt -H "Content-Type: application/json" http://localhost:2990/jira/rest/api/2/issue
