#!/bin/bash

# Setup Account bin directory

if [ -d ~/bin ]; then
    mv ~/bin ~/bin.orig
fi
cp -r template_bin ~
mv ~/template_bin ~/bin


