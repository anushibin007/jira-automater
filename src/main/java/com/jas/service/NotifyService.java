package com.jas.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Filter;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.jas.pojo.MailDetails;
import com.jas.thread.MailThread;
import com.jas.util.FileUtils;
import com.jas.util.JiraAutomaterPropService;

// TODO: Add debug logs to all methods for entering, exiting, who's calling, whom the mail is sent to, etc
@Service
public class NotifyService {

	Logger logger = LoggerFactory.getLogger(NotifyService.class);

	@Autowired
	FilterService filterServ;

	@Autowired
	TaskExecutor taskExecutor;

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	JiraAutomaterPropService jiraAutomaterProps;

	/**
	 * Think of it like this JSON:
	 * 
	 * <pre>
	 * {
			"user1":{
				"1998785":[
					"PROJ-12745",
					"PROJ-12547"
				],
				"2048798":[
					"PROJ-9948",
					"PROJ-5078"
				]
			},
			"user2":{
				"1998785":[
					"PROJ-17987",
					"PROJ-8978"
				],
				"2048798":[
					"PROJ-1278",
					"PROJ-9878"
				]
			}
		}
	 * </pre>
	 */
	private Map<String, Map<String, List<String>>> userToIssues = new HashMap<String, Map<String, List<String>>>();

	public String notifyAllFilterSatisfiers() {
		logger.trace("Entering notifyAllFilterSatisfiers");
		StringBuilder result = new StringBuilder(50);

		result.append("{ filtersToWatch.properties : ");

		result.append(processFile("filtersToWatch.properties", true));

		result.append(", jqlToWatch.properties : ");

		result.append(processFile("jqlToWatch.properties", false));

		result.append("}");

		sendMail();

		return result.toString();
	}

	private String processFile(String propertyFileName, boolean isFilterId) {
		try {
			logger.debug("Processing file: [" + propertyFileName + "] with property isFilterId = " + isFilterId);
			List<String> filtersToWatch = FileUtils.fileToStringArray(propertyFileName);
			if (filtersToWatch != null) {
				for (String filterIdOrJql : filtersToWatch) {
					Map<String, List<String>> filterResult = null;
					logger.debug("Processing line: [" + filterIdOrJql + "]");
					if (isFilterId) {
						Long filterID = Long.parseLong(filterIdOrJql);
						filterResult = notifyFilterSatisfiers(filterID);
					} else {
						// We process the string as a JQL if it is not a filter ID
						filterResult = notifyFilterSatisfiers(filterIdOrJql);
					}
					buildFilterResultsForJql(filterIdOrJql, filterResult);
				}
			}
			return filtersToWatch == null ? "none" : filtersToWatch.toString();
		} finally {
			logger.trace("Leaving processFilterIds");
		}
	}

	public Map<String, List<String>> notifyFilterSatisfiers(long filterID) {
		logger.trace("Entering notifyFilterSatisfiers");
		try {
			Map<String, List<String>> userToIssuesMap = new HashMap<>();

			Filter filter = filterServ.getFilter(filterID);
			SearchResult searchresult = filterServ.getFilterResult(filter);
			for (Issue anIssue : searchresult.getIssues()) {
				String emailId = anIssue.getAssignee() == null ? "" : anIssue.getAssignee().getEmailAddress();
				appendToMap(userToIssuesMap, emailId, buildIssueDetails(anIssue));
			}
			logger.debug(userToIssuesMap.toString());

//			sendMail(userToIssuesMap, filterID, filter.getName());

			return userToIssuesMap;
		} finally {
			logger.trace("Leaving notifyFilterSatisfiers");
		}
	}

	// TODO: Optimize redundant code notifyFilterSatisfiers(String jqlString) &
	// notifyFilterSatisfiers(long filterID)
	public Map<String, List<String>> notifyFilterSatisfiers(String jqlString) {
		logger.trace("Entering notifyFilterSatisfiers");
		try {
			Map<String, List<String>> userToIssuesMap = new HashMap<>();

			SearchResult searchresult = filterServ.getFilterResult(jqlString);
			for (Issue anIssue : searchresult.getIssues()) {
				// TODO: redirect unassigned tickets to someone else
				String emailId = anIssue.getAssignee() == null ? "" : anIssue.getAssignee().getEmailAddress();
				appendToMap(userToIssuesMap, emailId, buildIssueDetails(anIssue));
			}
			logger.debug(userToIssuesMap.toString());

//			sendMail(userToIssuesMap, jqlString, "JQL: " + jqlString);

			return userToIssuesMap;
		} finally {
			logger.trace("Leaving notifyFilterSatisfiers");
		}
	}

	private String buildIssueDetails(Issue anIssue) {
		logger.trace("Entering buildIssueDetails");
		try {
			// TODO URL or Entity encode this
			StringBuilder result = new StringBuilder(50);

			// <a href='https://jira.organization.com/browse/PROJ-125'>PROJ-125</a>
			result.append("<a href='");
			result.append(jiraAutomaterProps.getJiraServerUrl());
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

	private void appendToMap(Map<String, List<String>> userToIssuesMap, String user, String issue) {
		List<String> existingIssues = userToIssuesMap.get(user);
		if (userToIssuesMap != null && user != null && issue != null) {
			if (userToIssuesMap.get(user) == null) {
				// first entry
				userToIssuesMap.put(user, new ArrayList<String>(Arrays.asList(issue)));
			} else {
				// entry already exists
//				userToIssuesMap.get(user).add(issue);
				existingIssues.add(issue);
			}
		} else {
			logger.debug("Skipped as one of userToIssuesMap[<masked>], user[" + user + "], issue[" + issue + "] was null");
		}
	}

	private void buildFilterResultsForJql(String filterIdOrJql, Map<String, List<String>> userToIssuesMap) {
		for (Entry<String, List<String>> filter : userToIssuesMap.entrySet()) {

			String userMail = filter.getKey();
			List<String> userIssues = filter.getValue();

			Map<String, List<String>> existingIssuesForThisUser = userToIssues.get(userMail);
			if (existingIssuesForThisUser == null) {
				Map<String, List<String>> issuesMap = new HashMap<String, List<String>>();
				issuesMap.put(filterIdOrJql, userIssues);
				userToIssues.put(userMail, issuesMap);
			} else {
				existingIssuesForThisUser.put(filterIdOrJql, userIssues);
			}
		}
	}

//	private void sendMail(Map<String, List<String>> userToIssuesMap, long filterID, String filterName) {
//		sendMail(userToIssuesMap, String.valueOf(filterID), filterName);
//	}

	private void sendMail() {
		logger.trace("Entering sendMail");

		Date date = new Date();

		for (Entry<String, Map<String, List<String>>> entrySet : userToIssues.entrySet()) {
			String mailId = entrySet.getKey();
			if (mailId != null && !mailId.isEmpty() && MailService.safeRecipients.contains(mailId)) {

				logger.debug("Building mail for [" + mailId + "]");

				Map<String, List<String>> allFiltersAndTheirIssues = entrySet.getValue();

				MailDetails mail = new MailDetails();
				mail.setRecipient(mailId);
				mail.setSubject("JIRA Automater Report | " + date.toString());

				StringBuilder html = new StringBuilder("<h2>JIRA Automater Report for ");
				html.append(mailId);
				html.append(" on ");
				html.append(date.toString());
				html.append("</h2>");
				for (Entry<String, List<String>> aFilterAndItsIssues : allFiltersAndTheirIssues.entrySet()) {

					String filterID = aFilterAndItsIssues.getKey();
					List<String> issues = aFilterAndItsIssues.getValue();

//					html.append("<h3>Filter ID</h3><p>");
//					html.append(filterID);
//					html.append("</p>");

					html.append("<h3>Filter Name</h3><p>");
					html.append(filterID);
					html.append("</p>");

					html.append("<h3>Issues (<span style='color:red'>");
					html.append(issues.size());
					html.append("</span>)</h3>");

					html.append("<ol>");
					for (String issue : issues) {
						html.append("<li>");
						html.append(issue);
						html.append("</li>");
					}
					html.append("</ol>");
					html.append("<hr/>");
				}
				mail.setMsgBody(html.toString());

				MailThread mailThread = ctx.getBean(MailThread.class);
				mailThread.setMail(mail);
				taskExecutor.execute(mailThread);

				logger.debug("Sent mail to [" + mailId + "]");
			} else if (mailId != null && !mailId.isEmpty()) {
				logger.warn("Not sending mail to empty mail ID");
			} else {
				logger.debug("Skipping mail to [" + mailId + "]");
			}
		}
		logger.trace("Leaving sendMail");
	}

}
