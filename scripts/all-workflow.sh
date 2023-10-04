#!/bin/bash

#sudo apt-get update && apt-get install -y openjdk-19-jdk maven gradle

export REPO_DIRECTORY="/home/student/j/m/jmaquoi/Documents/Recherche/open-source-repositories"
export PLUGINS_DIRECTORY="/home/student/j/m/jmaquoi/Documents/Recherche/sentinel-backend/plugins"
# Clone all the open source repositories
echo "before clone-repos : $PWD"
sudo REPO_DIRECTORY="$REPO_DIRECTORY" PLUGINS_DIRECTORY="$PLUGINS_DIRECTORY" bash ./clone-repos.sh

# Execution of CK for each repository
#./run-ck.sh

# Execution of JoularJX for each repository
sudo REPO_DIRECTORY="$REPO_DIRECTORY" PLUGINS_DIRECTORY="$PLUGINS_DIRECTORY" bash ./run-joular.sh

# Execution of sentinel-backend to store the data into the database
