#!/bin/bash

source ./logs.sh

config_file="$PLUGINS_DIRECTORY/config.properties"

# ---------------------
# commons-configuration
# ---------------------
echo -e "---------------------"
echo -e "COMMONS-CONFIGURATION"
echo -e "---------------------"
log_and_print_output_with_date "Start JoularJX for commons-configuration"
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
sudo update-alternatives --set java "/usr/lib/jvm/java-17-openjdk-amd64/bin/java"
mvn -v
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

# Run tests with joular
for ((i=1;i<=NB_ITERATION;i++))
do
    export ITERATION_ID=$i
    echo -e "Start test for iteration $i\n"
    if [ "$i" == "$NB_ITERATION" ]; then
        log="$REPO_DIRECTORY/commons-configuration-logs.txt"
        mvn clean test -Drat.skip=true | tee "$log"
    else
        mvn clean test -Drat.skip=true
    fi
    log_iteration_output "$i"
done
log_and_print_output_with_date "Finished JoularJX for commons-configuration"
echo -e "\n\n\n\n"


# ------
# jabref
# ------
echo -e "------"
echo -e "JABREF"
echo -e "------"
log_and_print_output_with_date "Start JoularJX for jabref"
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/
sudo update-alternatives --set java "/usr/lib/jvm/java-17-openjdk-amd64/bin/java"
cd "$REPO_DIRECTORY/jabref" || exit
./gradlew -version
java -version

# Update config.properties
package_jabref="filter-method-names=org.jabref"
sed -i "17s/.*/${package_jabref}/" "$config_file"
cp "$config_file" "$REPO_DIRECTORY/jabref"

# Update build.gradle with joularjx plugin path
build_gradle="$PLUGINS_DIRECTORY/jabref/build.gradle"
line_number=$(grep -n -- "-javaagent" "$build_gradle" | cut -d: -f1)
sed -i "${line_number}s|-javaagent.*|-javaagent:${PLUGINS_DIRECTORY}/joularjx-2.8.2-modified.jar\"]|" "$build_gradle"
cp "$build_gradle" "$REPO_DIRECTORY/jabref"

# Making all added files non admin
sudo chmod 777 -R "$REPO_DIRECTORY/jabref"

# Run tests with joular
for ((i=1;i<=NB_ITERATION;i++))
do
    export ITERATION_ID=$i
    echo -e "Start test for iteration $i\n"
    if [ "$i" == "$NB_ITERATION" ]; then
        log="$REPO_DIRECTORY/jabref-logs.txt"
        sudo xvfb-run --auto-servernum ./gradlew clean test -PITERATION_ID=$i | tee "$log"
    else
        sudo xvfb-run --auto-servernum ./gradlew clean test -PITERATION_ID=$i
    fi
    log_iteration_output "$i"
done
log_and_print_output_with_date "Finished JoularJX for jabref"
echo -e "\n\n\n\n"


# -------------
# hibernate-orm
# -------------
echo -e "-------------"
echo -e "HIBERNATE-ORM"
echo -e "-------------"
log_and_print_output_with_date "Start JoularJX for hibernate-orm"
export JAVA_HOME=/usr/lib/jvm/java-19-openjdk-amd64/
sudo update-alternatives --set java "/usr/lib/jvm/java-19-openjdk-amd64/bin/java"
cd "$REPO_DIRECTORY/hibernate-orm" || exit
./gradlew -version

# Update config.properties
package_jabref="filter-method-names=org.hibernate"
sed -i "17s/.*/${package_jabref}/" "$config_file"
cp "$config_file" "$REPO_DIRECTORY/hibernate-orm/hibernate-core"

# Update build.gradle with joularjx plugin path
build_gradle="$PLUGINS_DIRECTORY/hibernate-orm.hibernate-core/hibernate-core.gradle"
line_number=$(grep -n -- "-javaagent" "$build_gradle" | cut -d: -f1)
sed -i "${line_number}s|-javaagent.*|-javaagent:${PLUGINS_DIRECTORY}/joularjx-2.8.2-modified.jar\"] )|" "$build_gradle"
cp "$build_gradle" "$REPO_DIRECTORY/hibernate-orm/hibernate-core"

sudo chmod -R 777 "$REPO_DIRECTORY/hibernate-orm/hibernate-core"

# Run tests with joular
for ((i=1;i<=NB_ITERATION;i++))
do
    export ITERATION_ID=$i
    echo -e "Start test for iteration $i\n"
    if [ "$i" == "$NB_ITERATION" ]; then
        log="$REPO_DIRECTORY/hibernate-orm-logs.txt"
        sudo ./gradlew clean hibernate-core:test -PITERATION_ID=$i --rerun | tee "$log"
    else
        sudo ./gradlew clean hibernate-core:test -PITERATION_ID=$i --rerun
    fi
    log_iteration_output "$i"
done
log_and_print_output_with_date "Finished JoularJX for hibernate-orm"
echo -e "\n\n\n\n"


# -----------
# Spring-boot
# -----------
echo -e "-----------"
echo -e "SPRING-BOOT"
echo -e "-----------"
log_and_print_output_with_date "Start JoularJX for spring-boot"
export JAVA_HOME=/usr/lib/jvm/java-19-openjdk-amd64
./gradlew -v
cd "$REPO_DIRECTORY/spring-boot" || exit

# Update config.properties
package_spring_boot="filter-method-names=org.springframework.boot"
sed -i "17s/.*/${package_spring_boot}/" "$config_file"
cp "$config_file" "$REPO_DIRECTORY/spring-boot/spring-boot-project/spring-boot/"

# Update root build.gradle with joularjx plugin path
build_gradle="$PLUGINS_DIRECTORY/spring-boot/build.gradle"
line_number=$(grep -n -- "-javaagent" "$build_gradle" | cut -d: -f1)
sed -i "${line_number}s|-javaagent.*|-javaagent:${PLUGINS_DIRECTORY}/joularjx-2.8.2-modified.jar\"|" "$build_gradle"
cp "$build_gradle" "$REPO_DIRECTORY/spring-boot/spring-boot-project/spring-boot/"

sudo chmod 777 -R "$REPO_DIRECTORY/spring-boot/spring-boot-project/spring-boot/"

# Run tests with joular
for ((i=1;i<=NB_ITERATION;i++))
do
    export ITERATION_ID=$i
    echo -e "Start test for iteration $i\n"
    if [ "$i" == "$NB_ITERATION" ]; then
        log="$REPO_DIRECTORY/spring-boot-logs.txt"
        sudo ./gradlew clean spring-boot-project:spring-boot:test -PITERATION_ID=$i --rerun | tee "$log"
    else
        sudo ./gradlew clean spring-boot-project:spring-boot:test -PITERATION_ID=$i --rerun
    fi
    log_iteration_output "$i"
done
log_and_print_output_with_date "Finished JoularJX for spring-boot"
echo -e "\n\n\n\n"


# -----
# spoon
# -----
echo -e "-----"
echo -e "SPOON"
echo -e "-----"
log_and_print_output_with_date "Start JoularJX for spoon"
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
sudo update-alternatives --set java "/usr/lib/jvm/java-17-openjdk-amd64/bin/java"
mvn -v
cd "$REPO_DIRECTORY/spoon" || exit

# Update config.properties
package_spoon="filter-method-names=spoon"
sed -i "17s/.*/${package_spoon}/" "$config_file"
cp "$config_file" "$REPO_DIRECTORY/spoon"

# Update pom.xml with joularjx plugin path
build_maven_spoon="$PLUGINS_DIRECTORY/spoon/pom.xml"
line_number=$(awk '/-javaagent/{print NR; exit}' "$build_maven_spoon")
sed -i "${line_number}s|-javaagent.*|-javaagent:${PLUGINS_DIRECTORY}/joularjx-2.8.2-modified.jar|" "$build_maven_spoon"
cp "$build_maven_spoon" "$REPO_DIRECTORY/spoon"

sudo chmod 777 -R "$REPO_DIRECTORY/spoon"

# Run tests with joular
for ((i=1;i<=NB_ITERATION;i++))
do
    export ITERATION_ID=$i
    echo -e "Start test for iteration $i\n"
    if [ "$i" == "$NB_ITERATION" ]; then
        log="$REPO_DIRECTORY/spoon-logs.txt"
        mvn clean test -Drat.skip=true | tee "$log"
    else
        mvn clean test -Drat.skip=true
    fi
    log_iteration_output "$i"
done
log_and_print_output_with_date "Finished JoularJX for spoon"
echo -e "\n\n\n\n"

