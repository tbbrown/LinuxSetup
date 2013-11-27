#!/bin/bash

# Setup Git

sudo apt-get install libcurl4-gnutls-dev libexpat1-dev gettext libz-dev libssl-dev
sudo apt-get install git

git config --global user.name "Timothy Brown"
git config --global user.email "tbb@acm.org"
git config --global core.editor gedit
git config --global merge.tool meld
git config --list


