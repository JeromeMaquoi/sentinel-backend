#!/bin/bash

LOG_FILE="ck-joular.log"
exec &> >(tee -a "$LOG_FILE")

start=$(date +%s)

sudo apt-get update && apt-get install -y openjdk-19-jdk openjdk-17-jdk maven gradle docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
sudo apt install docker-compose xmlstarlet

# Clone all the open source repositories
sudo REPO_DIRECTORY="$REPO_DIRECTORY" bash ./clone-repos.sh

# Execution of CK for each repository
sudo REPO_DIRECTORY="$REPO_DIRECTORY" PLUGINS_DIRECTORY="$PLUGINS_DIRECTORY" bash ./run-ck.sh

# Execution of JoularJX for each repository
sudo REPO_DIRECTORY="$REPO_DIRECTORY" PLUGINS_DIRECTORY="$PLUGINS_DIRECTORY" NB_ITERATION="$NB_ITERATION" bash ./run-joular.sh

echo "All CK and joular data generated for all the projects !"
end=$(date +%s)
diff=$((end-start))
echo "Execution time for cloning, running CK and running Joular: $diff seconds." >> ../plugins/totalTime.txt

exec &> /dev/tty