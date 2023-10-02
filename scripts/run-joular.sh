#!/bin/bash

config_file="/plugins/config.properties"
build_file_gradle="/plugins/build.gradle"

# -----------
# Spring-boot
# -----------

#cd /$REPO_DIR/spring-boot || exit
# Update config.properties
#package_spring_boot="filter-method-names=org.springframework.boot"
#sed -i "17s/.*/${package_spring_boot}/" "$config_file"

# Update root build.gradle
#cp "$build_file_gradle" /$REPO_DIR/spring-boot

# Add config.properties for every subproject
#find . -type f -name 'build.gradle' -exec dirname {} \; | while read dir; do
#  if [ -d "$dir/src" ]; then
#    echo "Copying config.properties to $dir"
#    cp "$config_file" "$dir"
#  fi
#done

# Run tests 30 times
#for i in {1..1}
#do
#    echo -e "Start test for iteration $i\n"
#    ./gradlew test
#    echo -e "Test for iteration $i done!\n"
#done


build_maven_commons_lang="/plugins/commons-lang/pom.xml"
cd /$REPO_DIR/commons-lang || exit
package_commons_lang="filter-method-names=org.apache.commons.lang3"
sed -i "17s/.*/${package_commons_lang}/" "$config_file"
cp "$build_maven_commons_lang" /$REPO_DIR/commons-lang
cp "$config_file" /$REPO_DIR/commons-lang

for i in {1..1}
do
    echo -e "Start test for iteration $i\n"
    mvn test -Drat.skip=true
done
