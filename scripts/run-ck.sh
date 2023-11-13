#!/bin/bash

cd "$REPO_DIRECTORY" || exit
find "$PWD" -mindepth 1 -maxdepth 1 -type d | while read -r dir; do
    cd "$dir" || exit
    if [ ! -d "output-ck" ] || [ "$(ls -1A "$dir/output-ck/" | wc -l)" -eq 0 ]; then
        echo -e "\nRunning CK for $dir"
        mkdir "output-ck"
        java -jar "$PLUGINS_DIRECTORY/ck-0.7.1-SNAPSHOT-jar-with-dependencies.jar" "$dir" False 0 True "$dir/output-ck/"
    else
        echo "Directory 'output-ck' already exists or is not empty in $dir"
    fi
done
