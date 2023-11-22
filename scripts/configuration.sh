#!/bin/bash

start=$(date +%s)

sudo apt-get update && apt-get install -y openjdk-19-jdk openjdk-17-jdk maven docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
sudo apt install docker-compose xmlstarlet

# TODO update to JDK 15
# Install jdk 15
cd /usr/lib/jvm || exit
if [ ! -d "jdk-15.0.2" ]; then
    cd "$PLUGINS_DIRECTORY" || exit
    wget https://download.java.net/java/GA/jdk15.0.2/0d1cfde4252546c6931946de8db48ee2/7/GPL/openjdk-15.0.2_linux-x64_bin.tar.gz
    sudo tar -xzvf openjdk-15.0.2_linux-x64_bin.tar.gz -C /usr/lib/jvm
    sudo update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/jdk-15.0.2/bin/java" 1
    sudo update-alternatives --install "/usr/bin/javac" "javac" "/usr/lib/jvm/jdk-15.0.2/bin/javac" 1
    rm -rf "$PLUGINS_DIRECTORY/openjdk-15.0.2_linux-x64_bin.tar.gz"
    echo "Installation of OpenJDK15"
else
    echo "OpenJDK15 already installed !"
fi

echo "Configuration done ! Writing total execution time to file..."
end=$(date +%s)
diff=$((end-start))
cd "$PLUGINS_DIRECTORY" || exit
echo "TOTAL EXECUTION TIME" > ./totalTime.txt
echo "Server configuration: $diff seconds." >> ./totalTime.txt
