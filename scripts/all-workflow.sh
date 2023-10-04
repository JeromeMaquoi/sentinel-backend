#!/bin/bash

sudo apt-get update && apt-get install -y openjdk-19-jdk maven gradle

export REPO_DIR="/open-source-repositories"
# Clone all the open source repositories
sudo bash ./clone-repos.sh

# Execution of CK for each repository
#./run-ck.sh

# Execution of JoularJX for each repository
sudo bash ./run-joular.sh

# Execution of sentinel-backend to store the data into the database
