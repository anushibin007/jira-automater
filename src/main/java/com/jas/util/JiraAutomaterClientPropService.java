package com.jas.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = Constants.JIRA_AUTOMATER_JIRA_CLIENT_PROP_PREFIX)
public class JiraAutomaterClientPropService implements Constants {
	@Value("#{'${jira.automater.jira-client.filter-ids-to-watch}'.split(',')}")
	private List<String> filterIdsToWatch;

	private final Map<String, String> jql = new HashMap<>();

	public List<String> getFilterIdsToWatch() {
		return filterIdsToWatch;
	}

	public void setFilterIdsToWatch(List<String> filterIdsToWatch) {
		this.filterIdsToWatch = filterIdsToWatch;
	}

	public Map<String, String> getJql() {
		return jql;
	}

}
