package com.jas.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("jira-automater.properties")
public class JiraAutomaterPropService {
	@Value("${jira.server.url}")
	String jiraServerUrl;

	@Value("${jira.server.username}")
	String jiraServerUsername;

	@Value("${jira.server.password}")
	String jiraServerPassword;

	public String getJiraServerUrl() {
		return jiraServerUrl;
	}

	public String getJiraServerUsername() {
		return jiraServerUsername;
	}

	public String getJiraServerPassword() {
		return jiraServerPassword;
	}

	public void validate() {
		if (jiraServerUrl == null || jiraServerUrl.isEmpty()) {
			throw new NullPointerException("jira.server.url is either not present or is empty in jira-automater.properties");
		}
		if (jiraServerUsername == null || jiraServerUsername.isEmpty()) {
			throw new NullPointerException("jira.server.username is either not present or is empty in jira-automater.properties");
		}
		if (jiraServerPassword == null || jiraServerPassword.isEmpty()) {
			throw new NullPointerException("jira.server.password is either not present or is empty in jira-automater.properties");
		}
	}

}
