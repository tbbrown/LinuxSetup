#!/bin/bash

# Setup Account bin directory

if [ -d ~/bin ]; then
    echo "WARNING: Original ~/bin directory will be moved to ~/bin.orig and replaced"
    mv ~/bin ~/bin.orig
fi

cp -r template_bin ~
mv ~/template_bin ~/bin

# Also copy Example Unix Commands

if [ -f ~/ExampleUnixCommands.txt ]; then
    echo "WARNING: Original ~/ExampleUnixCommands.txt file will be moved to ~/ExampleUnixCommands.txt.orig and replaced"
    mv ~/ExampleUnixCommands.txt ~/ExampleUnixCommands.txt.orig
fi

cp template.ExampleUnixCommands.txt ~/ExampleUnixCommands.txt


