#!/bin/bash

# Setup Eclipse

sudo apt-get install eclipse eclipse-jdt eclipse-cdt

# ---------------------------
# Install XML and XSL editors
# ---------------------------
# 
# Eclipse --> Help --> Install New Software
# Work with or Add: Eclipse WebTools Project - http://download.eclipse.org/webtools/updates
# Open Web Tools Platform (WTP) - latest version
# Select - Eclipse XML Editors and Tools
# Select - Eclipse XSL Devloper Tools

# eclipse -nosplash -application org.eclipse.equinox.p2.director -repository "http://download.eclipse.org/webtools/updates" -installIU org.eclipse.wst.xml_ui.feature.feature.group

