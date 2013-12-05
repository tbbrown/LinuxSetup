#!/bin/bash

# Setup FreeSurfer

# The following sets the variable SetupFreeSurferVersion if it is unset or empty
: ${SetupFreeSurferVersion:=5.3.0}

# Store current directory so we can get back to it
StartingDirectory=`pwd`
#echo $StartingDirectory

# Download the FreeSurfer tarball
cd ~/Downloads
FreeSurferTarBall=freesurfer-Linux-centos6_x86_64-stable-pub-v$SetupFreeSurferVersion.tar.gz
if [ -f $FreeSurferTarBall ]; then
    echo "Using existing previously downloaded file: " $FreeSurferTarBall
else
    FreeSurferDownloadUri=ftp://surfer.nmr.mgh.harvard.edu/pub/dist/freesurfer/$SetupFreeSurferVersion/$FreeSurferTarBall
    echo "Downloading file from: " $FreeSurferDownloadUri
    wget -c $FreeSurferDownloadUri
fi

# Unpack the tarball in the /usr/local
cd /usr/local
sudo tar xvf ~/Downloads/$FreeSurferTarBall

# Return to the directory we were in when this started
cd $StartingDirectory
