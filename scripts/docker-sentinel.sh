#!/bin/bash
cd ..
#export JAVA_HOME=/usr/lib/jvm/java-19-openjdk-amd64
export COMPOSE_PROJECT_NAME=sentinelbackend

sudo ./mvnw clean
export $(grep -v '^#' .env | xargs)
./mvnw package -Pprod jib:dockerBuild
docker-compose -f src/main/docker/app.yml up --build
