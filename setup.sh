SDK_DOWNLOAD_LOCATION=https://marketplace.atlassian.com/download/plugins/atlassian-plugin-sdk-tgz
SDK_DOWNLOAD_FILENAME=atlassian-plugin-sdk-tgz
SDK_OUTPUT_DIRECTORY=/scratch/atlassian-plugin-sdk

# Returns us to the location of the script, after the install is finished
ORIGINAL_DIR=$(pwd)

# Ensure java home is set on this lab machine
export JAVA_HOME="/usr/java/jdk1.8.0_144"

# Download the SDK
cd /scratch
wget $SDK_DOWNLOAD_LOCATION

# Extract it
mkdir $SDK_OUTPUT_DIRECTORY
tar -xvzf $SDK_DOWNLOAD_FILENAME -C $SDK_OUTPUT_DIRECTORY --strip-components 1

# Delete any extraneous files
rm $SDK_DOWNLOAD_FILENAME

# Go back to the location of this script
cd $ORIGINAL_DIR
