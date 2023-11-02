#!/bin/bash

config_file="$PLUGINS_DIRECTORY/config.properties"
build_file_gradle="$PLUGINS_DIRECTORY/build.gradle"
:'
# -----------
# Spring-boot
# -----------
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
cd "$REPO_DIRECTORY/spring-boot" || exit

# Update config.properties
package_spring_boot="filter-method-names=org.springframework.boot"
sed -i "17s/.*/${package_spring_boot}/" "$config_file"

# Update root build.gradle
cp "$build_file_gradle" "$REPO_DIRECTORY/spring-boot"

# Add config.properties for every subproject
find . -type f -name 'build.gradle' -exec dirname {} \; | while read dir; do
  if [ -d "$dir/src" ]; then
    echo "Copying config.properties to $dir"
    cp "$config_file" "$dir"
  fi
done

# Run tests 30 times
for ((i=1;i<=NB_ITERATION;i++))
do
    export ITERATION_ID=$i
    echo -e "Start test for iteration $i\n"
    ./gradlew clean test
    echo -e "Test for iteration $i done!\n\n"
done
echo -e "\n\n\n\n"
'

# ------------
# commons-lang
# ------------
cd "$REPO_DIRECTORY/commons-lang" || exit

# Update config.properties
package_commons_lang="filter-method-names=org.apache.commons.lang3"
sed -i "17s/.*/${package_commons_lang}/" "$config_file"
cp "$config_file" "$REPO_DIRECTORY/"commons-lang

# Update pom.xml with joularjx plugin path
build_maven_commons_lang="$PLUGINS_DIRECTORY/commons-lang/pom.xml"
line_number=$(xmlstarlet sel -t -v "count(//profile[activation/jdk='[16,)'])" "$build_maven_commons_lang")
sed -i "${line_number}s|-javaagent.*|-javaagent:${PLUGINS_DIRECTORY}/joularjx-2.0-modified.jar|" "$build_maven_commons_lang"
cp "$build_maven_commons_lang" "$REPO_DIRECTORY/commons-lang"

# Run joular
for ((i=1;i<=NB_ITERATION;i++))
do
    export ITERATION_ID=$i
    echo -e "Start test for iteration $i\n"
    mvn clean test -Drat.skip=true
    echo -e "\n\n"
done
echo -e "\n\n\n\n"

# ---------------------
# commons-configuration
# ---------------------
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
cd "$REPO_DIRECTORY/commons-configuration" || exit

# Update config.properties
package_commons_configuration="filter-method-names=org.apache.commons.configuration2"
sed -i "17s/.*/${package_commons_configuration}/" "$config_file"
cp "$config_file" "$REPO_DIRECTORY/"commons-configuration

# Update pom.xml with joularjx plugin path
build_maven_commons_configuration="$PLUGINS_DIRECTORY/commons-configuration/pom.xml"
line_number=$(xmlstarlet sel -t -v "count(//profile[activation/jdk='[16,)'])" "$build_maven_commons_configuration")
sed -i "${line_number}s|-javaagent.*|-javaagent:${PLUGINS_DIRECTORY}/joularjx-2.0-modified.jar|" "$build_maven_commons_configuration"
cp "$build_maven_commons_configuration" "$REPO_DIRECTORY/"commons-configuration

# Run joular
for ((i=1;i<=NB_ITERATION;i++))
do
    export ITERATION_ID=$i
    echo -e "Start test for iteration $i\n"
    mvn clean test -Drat.skip=true
    echo -e "\n\n"
done
echo -e "\n\n\n\n"
