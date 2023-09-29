# Use a base image with Ubuntu
FROM ubuntu:22.04

# Install necessary tools and dependencies
RUN apt-get update && apt-get install -y \
    git \
    openjdk-19-jdk \
    maven \
    gradle

ARG REPO_PATH_ARG
ENV REPO_PATH $REPO_PATH_ARG

RUN echo $REPO_PATH

# Set the working directory
WORKDIR ./

RUN echo "Copy of the sh scripts started..."

RUN mkdir "scripts"

# Copy the scripts into the container
COPY scripts/clone-repos.sh ./scripts
COPY scripts/run-ck.sh ./scripts
COPY scripts/run-joular.sh ./scripts
COPY scripts/docker-sentinel.sh ./scripts

RUN echo "All sh scripts copied!"

# Make the scripts executable
WORKDIR ./scripts
RUN chmod +x clone-repos.sh run-ck.sh run-joular.sh docker-sentinel.sh

# Run the clone and analysis scripts
#RUN ./clone-repos.sh
#RUN ./run-ck-joularjx.sh

# Start the entire workflow
CMD ["./clone-repos.sh"]
#CMD ["./docker-sentinel.sh"]
