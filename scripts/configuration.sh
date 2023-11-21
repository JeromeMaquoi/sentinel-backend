#!/bin/bash

start=$(date +%s)

sudo apt-get update && apt-get install -y openjdk-19-jdk openjdk-17-jdk maven docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
sudo apt install docker-compose xmlstarlet

# TODO update to JDK 14
# Install jdk 21
cd /usr/lib/jvm || exit
if [ ! -d "jdk-21.0.1+12" ]; then
    cd "$PLUGINS_DIRECTORY" || exit
    wget https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.1%2B12/OpenJDK21U-jdk_x64_linux_hotspot_21.0.1_12.tar.gz
    sudo tar -xzvf OpenJDK21U-jdk_x64_linux_hotspot_21.0.1_12.tar.gz -C /usr/lib/jvm
    sudo update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/jdk-21.0.1+12/bin/java" 1
    sudo update-alternatives --install "/usr/bin/javac" "javac" "/usr/lib/jvm/jdk-21.0.1+12/bin/javac" 1
    rm -rf "$PLUGINS_DIRECTORY/OpenJDK21U-jdk_x64_linux_hotspot_21.0.1_12.tar.gz"
    echo "Installation of OpenJDK21"
else
    echo "OpenJDK21 already installed !"
fi

echo "Configuration done ! Writing total execution time to file..."
end=$(date +%s)
diff=$((end-start))
cd "$PLUGINS_DIRECTORY" || exit
echo "TOTAL EXECUTION TIME" > ./totalTime.txt
echo "Server configuration: $diff seconds." >> ./totalTime.txt
