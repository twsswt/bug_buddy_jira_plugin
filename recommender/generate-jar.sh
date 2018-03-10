#!/bin/bash

rm recommender.jar

full_classpath="-cp lib/gson-2.8.2.jar:lib/log4j-api-2.10.0.jar:lib/log4j-core-2.10.0.jar:src:."
all_sources="src/classifier/*.java src/main/*.java src/matcher/*.java src/puller/*.java"

# Create destination folders
mkdir bin
mkdir META-INF

# Compile the java files
javac -d bin $full_classpath $all_sources

cp -r src/META-INF .
cp src/log4j2.xml log4j2.xml

# Combine everything into a jar
jar cvmf META-INF/MANIFEST.MF recommender.jar -C bin . log4j2.xml

# Cleanup
rm -r META-INF
rm log4j2.xml
rm -r bin
