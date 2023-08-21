export JAVA_HOME=/usr/lib/jvm/java-19-openjdk-amd64
chmod +x mvnw
export $(cat .env | xargs)
systemctl start mongod
./mvnw
