FROM ubuntu:latest

RUN apt-get update && apt-get install -y openjdk-19-jdk openjdk-17-jdk maven git

WORKDIR ./

COPY ./scripts/ /scripts/
COPY ./plugins/ /plugins/

WORKDIR /scripts/
RUN chmod +x ck-joular-workflow.sh clone-repos.sh run-ck.sh run-joular.sh

ENV REPO_DIRECTORY /open-source-repositories
ENV PLUGINS_DIRECTORY /plugins

CMD ["./ck-joular-workflow.sh"]
