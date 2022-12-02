# docker build --pull --progress plain -t jiraautomater:latest .

FROM maven:3.8.6-openjdk-8-slim as builder
WORKDIR /tmp/jiraautomater
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY . .
RUN mvn package -Dmaven.test.skip=true

FROM eclipse-temurin:8-jre-alpine as runner
COPY --from=builder /tmp/jiraautomater/target/jira-automater-0.0.1-SNAPSHOT.jar jiraautomater.jar
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar jiraautomater.jar