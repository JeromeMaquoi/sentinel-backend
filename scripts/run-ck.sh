#!/bin/bash

cd "$REPO_DIRECTORY" || exit
find "$PWD" -mindepth 1 -maxdepth 1 -type d | while read -r dir; do
    cd "$dir" || exit
    if [ ! -d "output-ck" ] || [ "$(ls -1A "$dir/output-ck/" | wc -l)" -eq 0 ]; then
        echo -e "Running CK for $dir"
        # Change the CK version in function of the analyzed project
        if [[ "$dir" == *"jabref" ]] || [[ "$dir" == *"spoon" ]]; then
            echo -e "Using CK with JDK 17"
            ck_jar="ck-0.7.1-SNAPSHOT-jar-with-dependencies-jdk17.jar"
        else
            echo -e "Using normal CK"
            ck_jar="ck-0.7.1-SNAPSHOT-jar-with-dependencies-normal.jar"
        fi
        mkdir "output-ck"
        java -jar "$PLUGINS_DIRECTORY/$ck_jar" "$dir" False 0 True "$dir/output-ck/"
    else
        echo "Directory 'output-ck' already exists or is not empty in $dir"
    fi
    sudo chmod 777 -R "$dir"
    echo -e "\n"
done
