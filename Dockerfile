# Use a base image with Ubuntu
FROM ubuntu:22.04

# Install necessary tools and dependencies
RUN apt-get update && apt-get install -y \
    git \
    openjdk-19-jdk \
    maven \
    gradle

ENV JAVA_HOME=/usr/lib/jvm/java-19-openjdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

ARG REPO_DIR_ARG
ENV REPO_DIR $REPO_DIR_ARG

# Set the working directory
WORKDIR ./

RUN mkdir "scripts"
RUN mkdir "plugins"
RUN mkdir $REPO_DIR

# Copy the scripts into the container
COPY scripts/ ./scripts/

# Copy CK and JoularJX plugins into the container
COPY plugins/ ./plugins

# Make the scripts executable
WORKDIR ./scripts
RUN chmod +x clone-repos.sh run-ck.sh run-joular.sh docker-sentinel.sh all-workflow.sh

RUN ./all-workflow.sh
