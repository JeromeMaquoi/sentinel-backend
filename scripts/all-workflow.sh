#!/bin/bash

# Configuration of the server
echo -e "---------------------------"
echo -e "---------------------------"
echo -e "CONFIGURATION OF THE SERVER"
echo -e "---------------------------"
echo -e "---------------------------"
bash ./configuration.sh

# Clone each project, start CK and JoularJX analysis for each one
echo -e "-------------------------------------------------"
echo -e "-------------------------------------------------"
echo -e "STARTING CLONE, CK AND JOULARJX FOR EACH PROJECT"
echo -e "-------------------------------------------------"
echo -e "-------------------------------------------------"
EPO_DIRECTORY="$REPO_DIRECTORY" PLUGINS_DIRECTORY="$PLUGINS_DIRECTORY" BATCH_SIZE="$BATCH_SIZE" GITHUB_TOKEN="$GITHUB_TOKEN" NB_ITERATION="$NB_ITERATION" bash ./ck-joular-workflow.sh

# Starting the docker sentinel container
echo -e "-----------------------------"
echo -e "-----------------------------"
echo -e "STARTING THE DOCKER CONTAINER"
echo -e "-----------------------------"
echo -e "-----------------------------"
bash ./docker-sentinel.sh
