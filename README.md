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
2. Run [setupEnv.cmd](setupEnv.cmd) to stop tracking files that have confidential info.
3. Edit and update the following files with necessary information:

**Note:** All property files support commenting with a "`#`" symbol. All property files ignore empty lines.

|File|Description|Optional/Mandatory|
|----|-----------|------------------|
|[application.properties](src/main/resources/application.properties)|Contains<ul><li>All Spring Boot related properties</li><li>Mail Server Properties</li><li>Mail Sender & BCC Properties</li><li>Logging properties</li><li>Cron JOB properties</li></ul>|Mandatory|
|[filtersToWatch.properties](src/main/resources/filtersToWatch.properties)|Each line in this file is a valid JIRA filter ID|Optional|
|[jqlToWatch.properties](src/main/resources/jqlToWatch.properties)|Each line in this file is a valid JIRA Query Language (JQL) String|Optional|
|[jira-automater.properties](src/main/resources/jira-automater.properties)|The JIRA Server URL and Credentials are configured here|Mandatory|
|[mail-recipients.properties](src/main/resources/mail-recipients.properties)|<ul><li>Each line in this file is a mail ID to which JIRA Automater is allowed to send mails</li><li>If there is no entry for a particular mail ID which satisifies a filter, then no mail is sent even to the BCC recepient</li></ul>|Optional|
