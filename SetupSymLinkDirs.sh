#!/bin/bash

# Setup symbolic links in home directory to shared Windows directories 

: ${SetupSymLinkDriveLetter:=E}

mkdir -p ~/shared

if [ ! -h "$HOME/shared/data" ]; then
    ln -s /mnt/hgfs/Windows$SetupSymLinkDriveLetter/data ~/shared/data
fi

if [ ! -h "$HOME/shared/Documents" ]; then
    ln -s /mnt/hgfs/Windows$SetupSymLinkDriveLetter/Documents ~/shared/Documents
fi

if [ ! -h "$HOME/shared/Music" ]; then
    ln -s /mnt/hgfs/Windows$SetupSymLinkDriveLetter/Music ~/shared/Music
fi

if [ ! -h "$HOME/shared/Pictures" ]; then
    ln -s /mnt/hgfs/Windows$SetupSymLinkDriveLetter/Pictures ~/shared/Pictures
fi

if [ ! -h "$HOME/shared/Podcasts" ]; then
    ln -s /mnt/hgfs/Windows$SetupSymLinkDriveLetter/Podcasts ~/shared/Podcasts
fi 

if [ ! -h "$HOME/shared/projects" ]; then
    ln -s /mnt/hgfs/Windows$SetupSymLinkDriveLetter/projects ~/shared/projects
fi
