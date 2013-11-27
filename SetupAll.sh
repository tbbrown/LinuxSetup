#!/bin/bash

# Setup Symbolic Links
export SetupSymLinkDriveLetter=G
. SetupSymLinkDirs.sh

# Setup Java
export SetupJavaVersion=openjdk-7-jdk
. SetupJava.sh

# Setup account configuration files
. SetupDotFiles.sh

# Setup Eclipse
. SetupEclipse.sh

# Setup Emacs
. SetupEmacs.sh

# Setup meld
. SetupMeld.sh

# Setup Git
. SetupGit.sh

# Setup Subversion
. SetupSvn.sh

# Setup LaTeX
. SetupLaTeX.sh


