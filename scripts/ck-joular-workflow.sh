#!/bin/bash

#LOG_FILE="ck-joular.log"
#exec &> >(tee -a "$LOG_FILE")

start=$(date +%s)

if [ -z "$REMOVE_REPO" ] || [ "$REMOVE_REPO" == "true" ]; then
    # Remove all directories if any
    sudo rm -rf "$REPO_DIRECTORY/commons-configuration/" "$REPO_DIRECTORY/commons-lang/" "$REPO_DIRECTORY/spring-boot/" "$REPO_DIRECTORY/jabref" "$REPO_DIRECTORY/hibernate-orm"
fi

if [ -z "$CLONE" ] || [ "$CLONE" == "true" ]; then
    # Clone all the open source repositories
    sudo REPO_DIRECTORY="$REPO_DIRECTORY" bash ./clone-repos.sh
fi

if [ -z "$CK" ] || [ "$CK" == "true" ]; then
    # Execution of CK for each repository
    sudo REPO_DIRECTORY="$REPO_DIRECTORY" PLUGINS_DIRECTORY="$PLUGINS_DIRECTORY" bash ./run-ck.sh
fi

if [ -z "$JOULAR" ] || [ "$JOULAR" == "true" ]; then
    # Execution of JoularJX for each repository
    sudo REPO_DIRECTORY="$REPO_DIRECTORY" PLUGINS_DIRECTORY="$PLUGINS_DIRECTORY" NB_ITERATION="$NB_ITERATION" bash ./run-joular.sh
fi

echo "All CK and joular data generated for all the projects !"
end=$(date +%s)
diff=$((end-start))
echo "Execution time for cloning, running CK and running Joular: $diff seconds." >> ../plugins/totalTime.txt

#exec &> /dev/tty
#chmod 777 $LOG_FILE
