#!/bin/bash

# Setup Emacs

# The following sets the variable SetupEmacsVersion if it is unset or empty
: ${SetupEmacsVersion:=emacs24}

sudo apt-get install $SetupEmacsVersion

