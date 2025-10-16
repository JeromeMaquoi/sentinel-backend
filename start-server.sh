#!/bin/bash
cd "$(dirname "$0")" || exit 1
./mvnw clean
./mvnw &  # Start server in background

SERVER_PID=$!

TIMEOUT=${1:-120}
echo $TIMEOUT
ready=false
for i in $(seq 1 $TIMEOUT); do
    if nc -z localhost 8080; then
        echo "READYYYYYYYYYYYYYYYYYY"
        echo "READY" > /tmp/backend-ready
        ready=true
        break
    fi
    sleep 1
done

if [ "$ready" = false ]; then
    echo "FAILED" > /tmp/backend-ready
    exit 1
fi

wait $SERVER_PID
