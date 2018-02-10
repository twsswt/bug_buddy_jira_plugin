#!/bin/bash

rm recommender.jar

full_classpath="lib/gson-2.8.2.jar:src:."
all_sources="src/classifier/*.java src/main/*.java src/matcher/*.java src/puller/*.java"

# Create destination folders
mkdir bin
mkdir META-INF

# Compile the java files
javac -d bin -cp $full_classpath $all_sources

# Copy meta inf 
cp -r src/META-INF .

# Combine everything into a jar
jar cvmf META-INF/MANIFEST.MF recommender.jar -C bin .

# Cleanup
rm -r META-INF
rm -r bin
