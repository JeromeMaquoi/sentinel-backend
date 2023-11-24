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
sudo chmod -R 777 "$REPO_DIRECTORY/spring-boot/"
cd "$REPO_DIRECTORY/spring-boot" || exit
git reset --hard 3ed1f1a064a10e53adc2ad8c0b46a4b2c148ee21 # release v3.1.4
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
git reset --hard 5c9d8989f968d0ee3a942b411ef7fe121ed94609 # release v5.6
echo -e "\n\n"


# -------------
# hibernate-orm
# -------------
if [ ! -d "hibernate-orm" ]; then
    git clone https://github.com/hibernate/hibernate-orm.git "$REPO_DIRECTORY/hibernate-orm"
    echo "Repository hibernate-orm cloned successfully !"
else
    echo "Repository hibernate-orm already cloned !"
fi
sudo chmod -R 777 "$REPO_DIRECTORY/hibernate-orm"
cd "$REPO_DIRECTORY/hibernate-orm" || exit
git reset --hard 12442bd8c7cde6e7c006a6277eeb8e81ad0c2219 # release 6.2.13.FINAL
echo -e "\n\n"


# -----
# spoon
# -----
if [ ! -d "spoon" ]; then
    git clone https://github.com/INRIA/spoon.git "$REPO_DIRECTORY/spoon"
    echo "Repository spoon cloned successfully !"
else
    echo "Repository spoon already cloned !"
fi

sudo chmod -R 777 "$REPO_DIRECTORY/spoon"
cd "$REPO_DIRECTORY/spoon" || exit
git reset --hard 066f4cf207359e06d30911a553dedd054aef595c # release v 10.4.2




echo -e "\n\n\n"
