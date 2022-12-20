package com.jas.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.atlassian.jira.rest.client.api.domain.Filter;
import com.atlassian.jira.rest.client.api.domain.Issue;

@Component
public class Helper {

	Logger logger = LoggerFactory.getLogger(Helper.class);

	@Autowired
	JiraAutomaterServerPropService serverProps;

	public String buildIssueDetails(Issue anIssue) {
		logger.trace("Entering buildIssueDetails");
		try {
			StringBuilder result = new StringBuilder(50);

			// <a href='https://jira.organization.com/browse/PROJ-125'>PROJ-125</a>
			result.append("<a href='");
			result.append(serverProps.getUrl());
			result.append("/browse/");
			result.append(urlEncode(anIssue.getKey()));
			result.append("'>");
			result.append(anIssue.getKey());
			result.append("</a>");

			// (JIRA Summary)
			result.append(" (");
			result.append(anIssue.getSummary());
			result.append(")");
			return result.toString();
		} finally {
			logger.trace("Leaving buildIssueDetails");
		}
	}

	public String buildFilterDetails(Filter aFilter) {
		logger.trace("Entering buildFilterDetails");
		try {
			StringBuilder result = new StringBuilder(50);

			// <a href='https://jira.organization.com/issues/?filter=95216'>Filter Name</a>
			result.append("<a href='");
			result.append(serverProps.getUrl());
			result.append("/issues/?filter=");
			result.append(aFilter.getId());
			result.append("'>");
			result.append(aFilter.getName());
			result.append("</a>");

			return result.toString();
		} finally {
			logger.trace("Leaving buildFilterDetails");
		}
	}

	public String buildJqlDetails(String jqlName, String jql) {
		logger.trace("Entering buildJqlDetails");
		try {
			StringBuilder result = new StringBuilder(50);

			// jqlName - <a href='
			// https://jira.organization.com/issues/?jql=url%20encoded%20jql%20string'>jql
			// string</a>
			result.append("[JQL] ");
			result.append(jqlName);

			result.append(" - ");

			result.append("<a href='");
			result.append(serverProps.getUrl());
			result.append("/issues/?jql=");
			result.append(urlEncode(jql));
			result.append("'>");
			result.append(jql);
			result.append("</a>");

			return result.toString();
		} finally {
			logger.trace("Leaving buildJqlDetails");
		}
	}

	public String urlEncode(String url) {
		if (url == null) {
			return null;
		}
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
}
