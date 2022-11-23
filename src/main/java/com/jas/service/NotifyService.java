package com.jas.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.domain.Filter;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.jas.pojo.MailDetails;
import com.jas.thread.MailThread;
import com.jas.util.FileUtils;
import com.jas.util.JiraAutomaterPropService;

@Service
public class NotifyService {

	Logger logger = LoggerFactory.getLogger(NotifyService.class);

	@Autowired
	FilterService filterServ;

	@Autowired
	TaskExecutor taskExecutor;

	@Autowired
	MailThread mailServ;

	@Autowired
	JiraAutomaterPropService jiraAutomaterProps;

	public String notifyAllFilterSatisfiers() {
		List<String> filtersToWatch = FileUtils.fileToStringArray("filtersToWatch.properties");
		if (filtersToWatch != null) {
			for (String filter : filtersToWatch) {
				Long filterID = Long.parseLong(filter);
				notifyFilterSatisfiers(filterID);
			}
		}
		return filtersToWatch == null ? "none" : filtersToWatch.toString();
	}

	public String notifyFilterSatisfiers(long filterID) {

		Map<String, List<String>> userToIssuesMap = new HashMap<>();

		Filter filter = filterServ.getFilter(filterID);
		SearchResult searchresult = filterServ.getFilterResult(filter);
		for (Issue anIssue : searchresult.getIssues()) {
			addItem(userToIssuesMap, anIssue.getAssignee().getEmailAddress(), buildIssueDetails(anIssue));
		}
		logger.debug(userToIssuesMap.toString());

		sendMail(userToIssuesMap, filterID, filter.getName());

		return userToIssuesMap.toString();
	}

	private String buildIssueDetails(Issue anIssue) {
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
	}

	private void addItem(Map<String, List<String>> userToIssuesMap, String user, String issue) {
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
			logger.debug("Skipped as one of userToIssuesMap[<masked>], user[" + user + "], issue[" + issue + "]");
		}
	}

	private void sendMail(Map<String, List<String>> userToIssuesMap, long filterID, String filterName) {

		for (Entry<String, List<String>> entrySet : userToIssuesMap.entrySet()) {

			String mailId = entrySet.getKey();
			if (MailService.safeRecipients.contains(mailId)) {
				List<String> issues = entrySet.getValue();

				MailDetails mail = new MailDetails();
				mail.setRecipient(mailId);
				mail.setSubject("JA | Filter | " + filterName);

				StringBuilder html = new StringBuilder("<h2>Pending Tasks Report</h2>");

				html.append("<h3>Filter ID</h3><p>");
				html.append(filterID);
				html.append("</p>");

				html.append("<h3>Filter Name</h3><p>");
				html.append(filterName);
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

				mail.setMsgBody(html.toString());

				mailServ.setMail(mail);
				taskExecutor.execute(mailServ);

				logger.debug("Sent mail to " + mail + " for filterID " + filterID);
			} else {
				logger.debug("Skipping mail to " + mailId);
			}
		}
	}

}
