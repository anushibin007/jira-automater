package com.jas.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.jas.util.JiraAutomaterPropService;

@Service
public class JiraClientService {

	@Autowired
	JiraAutomaterPropService jiraAutomaterProps;

	JiraRestClient client = null;

	public JiraRestClient getJiraRestClient() {
		return client == null ? buildClient() : client;
	}

	private JiraRestClient buildClient() {
		try {
			client = new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(
					new URI(jiraAutomaterProps.getJiraServerUrl()), jiraAutomaterProps.getJiraServerUsername(),
					jiraAutomaterProps.getJiraServerPassword());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return client;
	}
}
