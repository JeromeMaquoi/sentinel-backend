#!/bin/bash

# Clone repositories
echo "Repo dir = $REPO_DIR"
#git clone https://github.com/spring-projects/spring-boot $REPO_DIR/spring-boot
#cd spring-boot || exit
#git reset --hard 3ed1f1a064a10e53adc2ad8c0b46a4b2c148ee21 # release v3.1.4
# Replacement of the root build.gradle file
# ...
# Copy of config.properties to every subproject which runs tests
# ...

cd /$REPO_DIR || exit
if [ ! -d "commons-lang" ]; then
    git clone https://github.com/apache/commons-lang ./commons-lang
    echo "Repository commons-lang cloned successfully!"
fi
cd commons-lang || exit
git reset --hard bcc10b359318397a4d12dbaef22b101725bc6323 # release v3.13.0

#cd .. || exit
#if [ ! -d "jabref" ]; then
#    git clone https://github.com/JabRef/jabref ./jabref
#    echo "Repository jabref cloned successfully!"
#fi
#cd jabref || exit
#git reset --hard 779e555c10eeb0d4d682d66167e188dcb79d765a # release v5.10

