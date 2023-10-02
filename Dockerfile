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
COPY scripts/clone-repos.sh ./scripts
COPY scripts/run-ck.sh ./scripts
COPY scripts/run-joular.sh ./scripts
COPY scripts/docker-sentinel.sh ./scripts
COPY scripts/all-workflow.sh ./scripts

# Copy CK and JoularJX plugins into the container
COPY plugins/ck-0.7.1-SNAPSHOT-jar-with-dependencies.jar ./plugins
COPY plugins/joularjx-2.0-modified.jar ./plugins

# Make the scripts executable
WORKDIR ./scripts
RUN chmod +x clone-repos.sh run-ck.sh run-joular.sh docker-sentinel.sh all-workflow.sh

RUN ./all-workflow.sh

# Start the entire workflow
#CMD ["./all-workflow.sh"]
