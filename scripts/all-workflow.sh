#!/bin/bash

sudo apt-get update && apt-get install -y openjdk-19-jdk maven gradle

# Clone all the open source repositories
cd ~/Documents/Recherche/open-source-repositories || exit
echo "before clone-repos : $PWD"
sudo bash ./clone-repos.sh

# Execution of CK for each repository
#./run-ck.sh

# Execution of JoularJX for each repository
sudo bash ./run-joular.sh

# Execution of sentinel-backend to store the data into the database
