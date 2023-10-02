#!/bin/bash

cd /scripts || exit
# Run the clone and analysis scripts
./clone-repos.sh
./run-ck.sh
