#!/bin/bash
cd ..
# Preparation of the data: cloning repositories, executing CK and Joular
docker build -t sentinel-workflow --build-arg REPO_PATH_ARG=./open-source-repositories/ .
docker run -it --name sentinel-workflow sentinel-workflow

# Start sentinel-backend app to handle data gathered by CK and Joular into a MongoDB database
