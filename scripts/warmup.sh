#!/bin/bash

source ./logs.sh

config_file="$PLUGINS_DIRECTORY/config.properties"

log_and_print_output_with_date "Start warmup"
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
sudo update-alternatives --set java "/usr/lib/jvm/java-17-openjdk-amd64/bin/java"
cd "$REPO_DIRECTORY/commons-configuration" || exit

# Update config.properties
package_commons_configuration="filter-method-names=org.apache.commons.configuration2"
sed -i "17s/.*/${package_commons_configuration}/" "$config_file"
cp "$config_file" "$REPO_DIRECTORY/commons-configuration"

# Update pom.xml with joularjx plugin path
build_maven_commons_configuration="$PLUGINS_DIRECTORY/commons-configuration/pom.xml"
line_number=$(awk '/-javaagent/{print NR; exit}' "$build_maven_commons_configuration")
sed -i "${line_number}s|-javaagent.*|-javaagent:${PLUGINS_DIRECTORY}/joularjx-2.8.2-modified.jar|" "$build_maven_commons_configuration"
cp "$build_maven_commons_configuration" "$REPO_DIRECTORY/commons-configuration"

sudo chmod 777 -R "$REPO_DIRECTORY/commons-configuration/"

# Run tests for warming up CPU
export ITERATION_ID=999
for ((i=1;i<=NB_WARMUP;i++))
do
    echo -e "Warmup iteration $i\n"
    mvn clean test -Drat.skip=true
done

folders=$(find joularjx-result -type d -name '999-*')
for folder in $folders; do
    echo -e "$folder"
    rm -rf "$folder"
done

log_and_print_output_with_date "Finished warmup"
