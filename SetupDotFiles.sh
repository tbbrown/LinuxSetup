#!/bin/bash

# Setup Account Initialization (Dot) files

if [ -f ~/.bashrc ]; then
    echo "WARNING: Original ~/.bashrc file will be moved to ~/.bashrc.orig and replaced"
    mv ~/.bashrc ~/.bashrc.orig
fi
cp template.bashrc ~/.bashrc

if [ -f ~/.bashrc_mine ]; then
    echo "WARNING: Original ~/.bashrc_mine file will be moved to ~/.bashrc_mine.orig and replaced"
    mv ~/.bashrc_mine ~/.bashrc_mine.orig
fi
cp template.bashrc_mine ~/.bashrc_mine

if [ -f ~/.gnomerc ]; then
    echo "WARNING: Original ~/.gnomerc file will be moved to ~/.gnomerc.orig and replaced"
    mv ~/.gnomerc ~/.gnomerc.orig
fi
cp template.gnomerc ~/.gnomerc
