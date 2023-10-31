#!/bin/bash

#sudo apt-get update && apt-get install -y openjdk-19-jdk maven gradle

# Clone all the open source repositories
sudo REPO_DIRECTORY="$REPO_DIRECTORY" bash ./clone-repos.sh

# Execution of CK for each repository
sudo REPO_DIRECTORY="$REPO_DIRECTORY" PLUGINS_DIRECTORY="$PLUGINS_DIRECTORY" bash ./run-ck.sh

# Execution of JoularJX for each repository
sudo REPO_DIRECTORY="$REPO_DIRECTORY" PLUGINS_DIRECTORY="$PLUGINS_DIRECTORY" NB_ITERATION="$NB_ITERATION" bash ./run-joular.sh

echo "All CK and joular data generated for all the projects !"
