#!/bin/bash

source ./logs.sh

log_title_output "START CK-JOULAR WORKFLOW"
log_configuration

if [ -z "$REMOVE_REPO" ] || [ "$REMOVE_REPO" == "true" ]; then
    log_and_print_output_with_date "Start removing all directories"
    # Remove all directories if any
    sudo rm -rf "$REPO_DIRECTORY/commons-configuration/" "$REPO_DIRECTORY/commons-lang/" "$REPO_DIRECTORY/spring-boot/" "$REPO_DIRECTORY/jabref" "$REPO_DIRECTORY/hibernate-orm"
    log_and_print_output_with_date "All directories removed"
fi

if [ -z "$CLONE" ] || [ "$CLONE" == "true" ]; then
    log_and_print_output_with_date "Start cloning all repositories (clones-repos.sh)"
    # Clone all the open source repositories
    sudo REPO_DIRECTORY="$REPO_DIRECTORY" bash ./clone-repos.sh
    log_and_print_output_with_date "All directories cloned"
fi

if [ -z "$CK" ] || [ "$CK" == "true" ]; then
    log_and_print_output_with_date "Start CK analysis for all directories (run-ck.sh)"
    # Execution of CK for each repository
    sudo REPO_DIRECTORY="$REPO_DIRECTORY" PLUGINS_DIRECTORY="$PLUGINS_DIRECTORY" bash ./run-ck.sh
    log_and_print_output_with_date "CK analysis finished"
fi

if [ -z "$JOULAR" ] || [ "$JOULAR" == "true" ]; then
    log_and_print_output_with_date "Start JoularJX analysis for all directories (run-joular.sh)"
    # Execution of JoularJX for each repository
    sudo REPO_DIRECTORY="$REPO_DIRECTORY" PLUGINS_DIRECTORY="$PLUGINS_DIRECTORY" NB_ITERATION="$NB_ITERATION" bash ./run-joular.sh
    log_and_print_output_with_date "JoularJX analysis finished"
fi

log_and_print_output_with_date "All CK and joular data generated for all the projects !"
log_output_without_date ""

