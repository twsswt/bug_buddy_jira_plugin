#!/bin/bash

rm data-gatherer.jar

downloaded_data="project-issue-data/bugreport.mozilla.firefox"
downloaded_data_new="temp_data_store"

full_classpath="-cp lib/gson-2.8.2.jar:lib/log4j-api-2.10.0.jar:lib/log4j-core-2.10.0:lib/commons-lang3-3.6.jar:lib/opencsv-4.0.jar:src:."
all_sources="src/converter/*.java src/evaluationStructures/*.java src/main/*.java src/scraper/*.java src/sender/*.java"

# Create destination folders
mkdir bin

mkdir $downloaded_data_new

mkdir META-INF

# Compile my java files
javac -d bin $full_classpath $all_sources

cp -r src/META-INF .
cp src/log4j2.xml log4j2.xml

# Move already downloaded data somewhere else, so it doesn't mess up the JAR
mv $downloaded_data/FirefoxIssueJSON $downloaded_data_new/FirefoxIssueJSON
mv $downloaded_data/FirefoxIssueXML $downloaded_data_new/FirefoxIssueXML
mv $downloaded_data/JiraJSON $downloaded_data_new/JiraJSON

# Combine them into a jar
jar cvmf META-INF/MANIFEST.MF data-gatherer.jar -C bin . project-issue-data log4j2.xml

# Move the data back
mv $downloaded_data_new/FirefoxIssueJSON $downloaded_data/FirefoxIssueJSON
mv $downloaded_data_new/FirefoxIssueXML $downloaded_data/FirefoxIssueXML
mv $downloaded_data_new/JiraJSON $downloaded_data/JiraJSON

rm -r $downloaded_data_new
rm -r META-INF
rm log4j2.xml
rm -r bin
