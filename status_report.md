
## *Auto Assigning Users to Issues*
#### *Stephen Brown* 
#### *1106679B* 

## Proposal
### Motivation
The purpose of this project is to investigate automatically assigning users in an issue tracker (such as JIRA) to reported issues. This should help reduce the amount of effort spent
triaging issues when they are first reported.

### Aims
The deliverable for this project will be a plugin for the JIRA issue tracker, which should be able to

1. Understand the complete history of issues in JIRA
1. Make recommendations for whom to assign to new issues

The project will be evaluated using a real life issue set, namely a database of Firefox issues, along with their reporter, initial assignee and final assignee. The various evaluation methods
used will be judged based on many issues end up with the correct final assignee.

## Progress
* The evaluation harness for loading issues into JIRA is mostly complete.
* A literature review for methods of auto-assigning has been completed

## Problems and risks
### Problems
* I have taken 6 of the required 8 subjects in this first semester. I will have more time to work on the project next semester
* Acquiring issues from Firefox, and parsing the resulting XML, has been quite slow

### Risks
* I'm still not quite sure what best practices are when developing a JIRA plugin. I will mitigate this with futher research
* Different matching techniques might have better results on different data sizes. I will mitigate this by either - allowing the selection of multiple matching implementations, or
programming such that it would be easy to add one.

## Plan
1. December - finish up evaluation harness, clean up code base a little
1. January - Develop jira plugin, test machine learning based matching
1. February - implement and test graph / social network based matching
1. March - Finish anything above not yet completed, write dissertation and video summary

