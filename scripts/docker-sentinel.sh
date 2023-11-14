#!/bin/bash

#LOG_FILE="docker-sentinel.log"
#exec &> >(tee -a "$LOG_FILE")

start=$(date +%s)

docker stop sentinelbackend_app_1 sentinelbackend_mongodb_1
docker rm sentinelbackend_app_1 sentinelbackend_mongodb_1
docker rmi sentinelbackend

cd ..
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export COMPOSE_PROJECT_NAME=sentinelbackend
sudo ./mvnw clean
./mvnw package -Pprod jib:dockerBuild

echo "Clean and package done ! Writing total execution time to file..."
end=$(date +%s)
diff=$((end-start))
chmod 777 ./plugins/totalTime.txt
echo "Clean and package: $diff seconds." >> ./plugins/totalTime.txt

docker-compose -f src/main/docker/app.yml up --build

#exec &> /dev/tty
#chmod 777 "$LOG_FILE"
