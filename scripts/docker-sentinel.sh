#!/bin/bash

source ./logs.sh

log_title_output "Start docker-sentinel workflow"

docker stop sentinelbackend_app_1 sentinelbackend_mongodb_1
docker rm sentinelbackend_app_1 sentinelbackend_mongodb_1
docker rmi sentinelbackend

cd ..
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export COMPOSE_PROJECT_NAME=sentinelbackend
sudo ./mvnw clean
./mvnw package -Pprod jib:dockerBuild

log_and_print_output_with_date "Clean and package done ! Starting docker build..."

docker-compose -f src/main/docker/app.yml up --build
