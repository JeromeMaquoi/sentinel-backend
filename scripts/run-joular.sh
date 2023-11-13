#!/bin/bash

config_file="$PLUGINS_DIRECTORY/config.properties"

# ---------------------
# commons-configuration
# ---------------------
echo -e "---------------------"
echo -e "COMMONS-CONFIGURATION"
echo -e "---------------------"
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
mvn -v
cd "$REPO_DIRECTORY/commons-configuration" || exit

# Update config.properties
package_commons_configuration="filter-method-names=org.apache.commons.configuration2"
sed -i "17s/.*/${package_commons_configuration}/" "$config_file"
cp "$config_file" "$REPO_DIRECTORY/commons-configuration"

# Update pom.xml with joularjx plugin path
build_maven_commons_configuration="$PLUGINS_DIRECTORY/commons-configuration/pom.xml"
line_number=$(awk '/-javaagent/{print NR; exit}' "$build_maven_commons_configuration")
sed -i "${line_number}s|-javaagent.*|-javaagent:${PLUGINS_DIRECTORY}/joularjx-2.0-modified.jar|" "$build_maven_commons_configuration"
cp "$build_maven_commons_configuration" "$REPO_DIRECTORY/commons-configuration"

# Run tests with joular
for ((i=1;i<=NB_ITERATION;i++))
do
    export ITERATION_ID=$i
    echo -e "Start test for iteration $i\n"
    mvn clean test -Drat.skip=true
    echo -e "\n\n"
done
echo -e "\n\n\n\n"


# ------------
# commons-lang
# ------------
echo -e "------------"
echo -e "COMMONS-LANG"
echo -e "------------"
mvn -v
cd "$REPO_DIRECTORY/commons-lang" || exit

# Update config.properties
package_commons_lang="filter-method-names=org.apache.commons.lang3"
sed -i "17s/.*/${package_commons_lang}/" "$config_file"
cp "$config_file" "$REPO_DIRECTORY/commons-lang"

# Update pom.xml with joularjx plugin path
build_maven_commons_lang="$PLUGINS_DIRECTORY/commons-lang/pom.xml"
line_number=$(awk '/-javaagent/{print NR; exit}' "$build_maven_commons_lang")
sed -i "${line_number}s|-javaagent.*|-javaagent:${PLUGINS_DIRECTORY}/joularjx-2.0-modified.jar|" "$build_maven_commons_lang"
cp "$build_maven_commons_lang" "$REPO_DIRECTORY/commons-lang"

# Run tests with joular
for ((i=1;i<=NB_ITERATION;i++))
do
    export ITERATION_ID=$i
    echo -e "Start test for iteration $i\n"
    mvn clean test -Drat.skip=true
    echo -e "\n\n"
done
echo -e "\n\n\n\n"


# ------
# jabref
# ------
echo -e "------"
echo -e "JABREF"
echo -e "------"
export JAVA_HOME=/usr/lib/jvm/jdk-21.0.1+12/
cd "$REPO_DIRECTORY/jabref" || exit
./gradlew -version

# Update config.properties
package_jabref="filter-method-names=org.jabref"
sed -i "17s/.*/${package_jabref}/" "$config_file"
cp "$config_file" "$REPO_DIRECTORY/jabref"

# Update build.gradle with joularjx plugin path
build_gradle="$PLUGINS_DIRECTORY/build.gradle"
line_number=$(grep -n -- "-javaagent" "$build_gradle" | cut -d: -f1)
sed -i "${line_number}s|-javaagent.*|-javaagent:${PLUGINS_DIRECTORY}/joularjx-2.0-modified.jar\"]|" "$build_gradle"
cp "$build_gradle" "$REPO_DIRECTORY/jabref"

# Run tests with joular
for ((i=1;i<=NB_ITERATION;i++))
do
    export ITERATION_ID=$i
    echo -e "Start test for iteration $i\n"
    sudo ./gradlew clean test -PITERATION_ID=$i
    echo -e "Test for iteration $i done!\n\n"
done
echo -e "\n\n\n\n"


:'
# -----------
# Spring-boot
# -----------
echo -e "-----------"
echo -e "SPRING-BOOT"
echo -e "-----------"
export JAVA_HOME=/usr/lib/jvm/java-19-openjdk-amd64
./gradlew -v
cd "$REPO_DIRECTORY/spring-boot" || exit

# Update config.properties
package_spring_boot="filter-method-names=org.springframework.boot"
sed -i "17s/.*/${package_spring_boot}/" "$config_file"
# Add config.properties for every subproject
find . -type f -name 'build.gradle' -exec dirname {} \; | while read dir; do
  if [ -d "$dir/src" ]; then
    echo "Copying config.properties to $dir"
    cp "$config_file" "$dir"
  fi
done

# Update root build.gradle with joularjx plugin path
build_gradle="$PLUGINS_DIRECTORY/build.gradle"
line_number=$(grep -n -- "-javaagent" "$build_gradle" | cut -d: -f1)
sed -i "${line_number}s|-javaagent.*|-javaagent:${PLUGINS_DIRECTORY}/joularjx-2.0-modified.jar\"|" "$build_gradle"
cp "$build_gradle" "$REPO_DIRECTORY/spring-boot/spring-boot-project/spring-boot/"

# Run tests with joular
sudo ./gradlew clean
for ((i=1;i<=NB_ITERATION;i++))
do
    export ITERATION_ID=$i
    echo -e "Start test for iteration $i\n"
    sudo ./gradlew :spring-boot-project:spring-boot:test
    echo -e "Test for iteration $i done!\n\n"
done
echo -e "\n\n\n\n"
'
