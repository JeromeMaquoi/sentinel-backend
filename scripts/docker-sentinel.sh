#!/bin/bash
cd ..
#export JAVA_HOME=/usr/lib/jvm/java-19-openjdk-amd64
export COMPOSE_PROJECT_NAME=sentinelbackend

sudo ./mvnw clean
#TODO supprimer token et le mettre en variable d'environnement
export GITHUB_TOKEN=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
export REPO_PATH=/home/jerome/Documents/Assistant/Recherche/open-source-repositories/
export BATCH_SIZE=20000
./mvnw package -Pprod jib:dockerBuild
docker-compose -f src/main/docker/app.yml up --build
