package com.jas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Filter;
import com.atlassian.jira.rest.client.api.domain.SearchResult;

@Service
public class FilterService {
	@Autowired
	private JiraClientService restClient;

	public Filter getFilter(long filterID) {
		return getRestClient().getSearchClient().getFilter(filterID).claim();
	}

	public SearchResult getFilterResult(long filterID) {
		return getRestClient().getSearchClient().searchJql(getFilter(filterID).getJql()).claim();
	}

	private JiraRestClient getRestClient() {
		return restClient.getJiraRestClient();
	}

	public SearchResult getFilterResult(Filter filter) {
		return getRestClient().getSearchClient().searchJql(filter.getJql()).claim();
	}
}
