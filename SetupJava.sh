#!/bin/bash

# Setup Java 

# The following sets the variable SetupJavaVersion if it is unset or empty
: ${SetupJavaVersion:=openjdk-7-jdk}

sudo apt-get install $SetupJavaVersion
update-java-alternatives -l
java -version
javac -version

# To see java versions available
#
# update-java-alternatives -l
#
# To select java version
# update-java-alternatives -s java-1.7.0-openjdk-amd64

