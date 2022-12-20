package com.jas.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.atlassian.jira.rest.client.api.domain.Issue;

@Component
public class Helper {

	Logger logger = LoggerFactory.getLogger(Helper.class);

	@Autowired
	JiraAutomaterServerPropService serverProps;

	public String buildIssueDetails(Issue anIssue) {
		logger.trace("Entering buildIssueDetails");
		try {
			// TODO URL or Entity encode this
			StringBuilder result = new StringBuilder(50);

			// <a href='https://jira.organization.com/browse/PROJ-125'>PROJ-125</a>
			result.append("<a href='");
			result.append(serverProps.getUrl());
			result.append("/browse/");
			result.append(anIssue.getKey());
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
}
