# JIRA Automater

This is a Spring Boot based application that can be set up to run 24\*7 and send out mail notifications to recepients that satisfy the configured JQL strings or JIRA Filter IDs.

This project was developed as a side project to notify teammates about subtle items they might have missed. For example:

-   Forgetting to add Release Notes
-   Forgetting to add Test Information
-   Missing proper Fix Versions
-   Leaving JIRAs in improper state (Resolved instead of Closed)
-   etc

All of the above items can be configured as JQLs or saved JIRA filters.

## Docker Image

A Continuous Build of the Docker Image happens every time the code changes.

Link: [jira-automater on Docker Hub](https://hub.docker.com/r/anushibin007/jira-automater)

The latest Docker Image can be pulled from Docker Hub using the following commands:

```console
docker pull anushibin007/jira-automater
docker run -d -p8080:8080 --name jira-automater anushibin007/jira-automater
```

## Setup Instructions

Here are the steps to setup a development environment for jira-automater:

1. Fork this repo and clone it to your localhost
2. Copy [application.properties](src/main/resources/application.properties) and name it to `application-dev.properties`
3. Update all the configuration properties to this file. This file is not version-tracked and can be used for development purposes.
4. Run [startApp.cmd](./startApp-dev.cmd) to start the Development Server.
