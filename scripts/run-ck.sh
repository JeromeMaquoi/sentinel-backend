#!/bin/bash

cd "$REPO_DIRECTORY" || exit
find "$PWD" -mindepth 1 -maxdepth 1 -type d | while read -r dir; do
    cd "$dir" || exit
    mkdir "output-ck"
    java -jar "$PLUGINS_DIRECTORY/ck-0.7.1-SNAPSHOT-jar-with-dependencies.jar" "$dir" False 0 True "$dir/output-ck/"
done
