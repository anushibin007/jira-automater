package com.jas.util;

import java.util.Base64;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = Constants.JIRA_AUTOMATER_JIRA_SERVER_PROP_PREFIX)
public class JiraAutomaterServerPropService implements Constants {

	String url;

	String username;

	String password;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public String getPasswordDecoded() {
		return new String(Base64.getDecoder().decode(password.trim())).trim();
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void validate() {
		if (url == null || url.isEmpty()) {
			throw new NullPointerException(JIRA_AUTOMATER_JIRA_SERVER_PROP_PREFIX
					+ ".url is either not present or is empty in jira-automater.properties");
		}
		if (username == null || username.isEmpty()) {
			throw new NullPointerException(JIRA_AUTOMATER_JIRA_SERVER_PROP_PREFIX
					+ ".username is either not present or is empty in jira-automater.properties");
		}
		if (password == null || password.isEmpty()) {
			throw new NullPointerException(JIRA_AUTOMATER_JIRA_SERVER_PROP_PREFIX
					+ ".password is either not present or is empty in jira-automater.properties");
		}
	}

}
