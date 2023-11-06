#!/bin/bash

start=$(date +%s)

cd ..
export COMPOSE_PROJECT_NAME=sentinelbackend
sudo ./mvnw clean
./mvnw package -Pprod jib:dockerBuild
echo "Clean and package done ! Writing total execution time to file..."

end=$(date +%s)
diff=$((end-start))
echo "TOTAL EXECUTION TIME" > ./plugins/totalTime.txt
echo "Clean and package: $diff seconds." >> ./plugins/totalTime.txt

docker-compose -f src/main/docker/app.yml up --build
