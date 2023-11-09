docker stop docker-ck-joular
docker rm docker-ck-joular
docker rmi docker-ck-joular

cd ..
docker build -t docker-ck-joular .
docker run --name docker-ck-joular \
    -v "$REPO_DIR:/open-source-repositories" \
    -e REPO_DIRECTORY="$REPO_DIRECTORY" \
    -e PLUGINS_REPO_DIRECTORY="$PLUGINS_DIRECTORY" \
    -e BATCH_SIZE="$BATCH_SIZE" \
    -e GITHUB_TOKEN="$GITHUB_TOKEN" \
    -e NB_ITERATION="$NB_ITERATION" \
    docker-ck-joular
