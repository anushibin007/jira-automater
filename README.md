# JIRA Automater

This is a Spring Boot based application that can be set up to run 24\*7 and send out mail notifications to recepients that satisfy the configured JQL strings or JIRA Filter IDs.

This project was developed as a side project to notify teammates about subtle items they might have missed. For example:

-   Forgetting to add Release Notes
-   Forgetting to add Test Information
-   Missing proper Fix Versions
-   Leaving JIRAs in improper state (Resolved instead of Closed)
-   etc

All of the above items can be configured as JQLs or saved JIRA filters.

## Setup Instructions

Here are the steps to setup a development environment for jira-automater:

1. Fork this repo and clone it to your localhost
2. Edit and update the following files with necessary information:

||File||Optional/Mandatory||
