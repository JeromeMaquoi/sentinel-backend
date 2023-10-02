#!/bin/bash

cd /scripts || exit
# Clone all the open source repositories
./clone-repos.sh

# Execution of CK for each repository
./run-ck.sh

# Execution of JoularJX for each repository
#./run-joular.sh

# Execution of sentinel-backend to store the data into the database
