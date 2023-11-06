#!/bin/bash

# Clone repositories
cd "$REPO_DIRECTORY" || exit

# -----------
# spring-boot
# -----------
if [ ! -d "spring-boot" ]; then
    git clone https://github.com/spring-projects/spring-boot "$REPO_DIRECTORY/spring-boot"
    echo "Repository spring-boot cloned successfully!"
else
    echo "Repository spring-boot already there!"
fi
cd "$REPO_DIRECTORY/spring-boot" || exit
git reset --hard 3ed1f1a064a10e53adc2ad8c0b46a4b2c148ee21 # release v3.1.4
echo -e "\n\n"

# ------------
# commons-lang
# ------------
if [ ! -d "commons-lang" ]; then
    git clone https://github.com/apache/commons-lang "$REPO_DIRECTORY/commons-lang"
    echo "Repository commons-lang cloned successfully!"
else
    echo "Repository already there!"
fi
cd "$REPO_DIRECTORY/commons-lang" || exit
git reset --hard bcc10b359318397a4d12dbaef22b101725bc6323 # release v3.13.0
echo -e "\n\n"


# ---------------------
# commons-configuration
# ---------------------
if [ ! -d "commons-configuration" ]; then
    git clone https://github.com/apache/commons-configuration "$REPO_DIRECTORY/commons-configuration"
    echo "Repository commons-configuration cloned successfully!"
else
    echo "Repository already cloned !"
fi
cd "$REPO_DIRECTORY/commons-configuration" || exit
git reset --hard 59e5152722198526c6ffe5361de7d1a6a87275c7
echo -e "\n\n"

#cd .. || exit
#if [ ! -d "jabref" ]; then
#    git clone https://github.com/JabRef/jabref ./jabref
#    echo "Repository jabref cloned successfully!"
#fi
#cd jabref || exit
#git reset --hard 779e555c10eeb0d4d682d66167e188dcb79d765a # release v5.10

