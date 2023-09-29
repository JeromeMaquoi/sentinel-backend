#!/bin/bash

# Clone repositories
REPO_PATH="$REPO_PATH"
echo "Repo path = $REPO_PATH"
git clone https://github.com/spring-projects/spring-boot /$REPO_PATH/spring-boot
#cd spring-boot || exit
#git reset --hard 3ed1f1a064a10e53adc2ad8c0b46a4b2c148ee21 # release v3.1.4
# Replacement of the root build.gradle file
# ...
# Copy of config.properties to every subproject which runs tests
# ...

#git clone https://github.com/apache/commons-lang $REPO_PATH/commons-lang
#git clone https://github.com/JabRef/jabref /$REPO_PATH/jabref
