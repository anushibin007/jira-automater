# docker build --pull --progress plain -t jira-automater:latest .

FROM maven:3.8.6-openjdk-8-slim as builder
WORKDIR /tmp/jira-automater
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY . .
RUN mvn package -Dmaven.test.skip=true

FROM eclipse-temurin:8-jre-alpine as runner
COPY --from=builder /tmp/jira-automater/target/jira-automater-0.0.1-SNAPSHOT.jar jira-automater.jar
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar jira-automater.jar