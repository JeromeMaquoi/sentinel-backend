#!/bin/bash

LOG_FILE="docker-sentinel.log"
exec &> >(tee -a "$LOG_FILE")

start=$(date +%s)

sudo apt-get update && apt-get install -y openjdk-19-jdk openjdk-17-jdk maven docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
sudo apt install docker-compose xmlstarlet

cd ..
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export COMPOSE_PROJECT_NAME=sentinelbackend
sudo ./mvnw clean
./mvnw package -Pprod jib:dockerBuild

echo "Clean and package done ! Writing total execution time to file..."
end=$(date +%s)
diff=$((end-start))
echo "TOTAL EXECUTION TIME" > ./plugins/totalTime.txt
chmod 777 ./plugins/totalTime.txt
echo "Clean and package: $diff seconds." >> ./plugins/totalTime.txt

docker-compose -f src/main/docker/app.yml up --build

exec &> /dev/tty
chmod 777 $LOG_FILE
