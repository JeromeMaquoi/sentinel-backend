#!/bin/bash

# Clone repositories
#cd /open-source-repositories || exit
#git clone https://github.com/spring-projects/spring-boot /open-source-repositories/spring-boot
#cd spring-boot || exit
#git reset --hard 3ed1f1a064a10e53adc2ad8c0b46a4b2c148ee21 # release v3.1.4

cd "$REPO_DIRECTORY" || exit
if [ ! -d "commons-lang" ]; then
    git clone https://github.com/apache/commons-lang "$REPO_DIRECTORY/commons-lang"
    echo "Repository commons-lang cloned successfully!"
else
    echo "Repository already there!"
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

