#!/bin/bash

# Clone repositories
cd "$REPO_DIRECTORY" || exit

:'
# -----------
# spring-boot
# -----------
if [ ! -d "spring-boot" ]; then
    git clone https://github.com/spring-projects/spring-boot "$REPO_DIRECTORY/spring-boot"
    echo "Repository spring-boot cloned successfully!"
else
    echo "Repository spring-boot already there!"
fi
chmod -R 777 "$REPO_DIRECTORY/spring-boot/"
cd "$REPO_DIRECTORY/spring-boot" || exit
git reset --hard 3ed1f1a064a10e53adc2ad8c0b46a4b2c148ee21 # release v3.1.4
echo -e "\n\n"
'

# ------------
# commons-lang
# ------------
if [ ! -d "commons-lang" ]; then
    git clone https://github.com/apache/commons-lang "$REPO_DIRECTORY/commons-lang"
    echo "Repository commons-lang cloned successfully!"
else
    echo "Repository commons-lang already cloned !"
fi
chmod -R 777 "$REPO_DIRECTORY/commons-lang/"
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
    echo "Repository commons-configuration already cloned !"
fi
sudo chmod -R 777 "$REPO_DIRECTORY/commons-configuration/"
cd "$REPO_DIRECTORY/commons-configuration" || exit
git reset --hard 59e5152722198526c6ffe5361de7d1a6a87275c7
echo -e "\n\n"


# ------
# jabref
# ------
if [ ! -d "jabref" ]; then
    git clone https://github.com/JabRef/jabref "$REPO_DIRECTORY/jabref"
    echo "Repository jabref cloned successfully!"
else
    echo "Repository jabref already cloned !"
fi
sudo chmod -R 777 "$REPO_DIRECTORY/jabref/"
cd "$REPO_DIRECTORY/jabref" || exit
git reset --hard affb6acc24c3dc5fce36e1323eed415a8f711a2c # release v5.11
echo -e "\n\n"


# -----
# guava
# -----
if [ ! -d "guava" ]; then
    git clone https://github.com/google/guava "$REPO_DIRECTORY/guava"
    echo "Repository guava cloned successfully !"
else
    echo "Repository guava already cloned !"
fi
sudo chmod -R 777 "$REPO_DIRECTORY/guava"
cd "$REPO_DIRECTORY/guava" || exit
git reset --hard c1088508ddc78bd60d096d2cc3ceef4a82ec909d # release 32.1.3
echo -e "\n\n"
