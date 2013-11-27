#!/bin/bash

# Setup Account Initialization (Dot) files

if [ -f "~/.bashrc" ]; then
    mv ~/.bashrc ~/.bashrc.orig
fi
cp template.bashrc ~/.bashrc

if [ -f "~/.bashrc_mine" ]; then
    mv ~/.bashrc_mine ~/.bashrc.orig
fi
cp template.bashrc_mine ~/.bashrc_mine

if [ -f "~/.gnomerc" ]; then
    mv ~/.gnomerc ~/.gnomerc.orig
fi
cp template.gnomerc ~/.gnomerc
