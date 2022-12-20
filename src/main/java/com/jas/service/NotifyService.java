package com.jas.service;

import java.util.Date;
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
import com.jas.pojo.NotifyResult;
import com.jas.thread.MailThread;
import com.jas.util.Helper;
import com.jas.util.JiraAutomaterClientPropService;
import com.jas.util.MailRecepientsService;

// TODO: Add debug logs to all methods for entering, exiting, who's calling, whom the mail is sent to, etc
@Service
public class NotifyService {

	Logger logger = LoggerFactory.getLogger(NotifyService.class);

	@Autowired
	private FilterService filterServ;

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private JiraAutomaterClientPropService clientProps;

	@Autowired
	private MailRecepientsService mailRecepService;

	@Autowired
	private Helper helper;

	private NotifyResult notifyResult = null;

	public String notifyAllFilterSatisfiers() {
		logger.trace("Entering notifyAllFilterSatisfiers");

		notifyResult = new NotifyResult();

		processAllSatisfiers();

		sendMail();

		return getCombinedJsonFormatOfProperties();
	}

	private String getCombinedJsonFormatOfProperties() {
		StringBuilder result = new StringBuilder(50);
		result.append("{ jira.automater.jira-client.filter-ids-to-watch : ");
		result.append(clientProps.getFilterIdsToWatch().toString());
		result.append(", jira.automater.jira-client.jql : ");
		result.append(clientProps.getJql().toString());
		result.append("}");

		return result.toString();
	}

	private void processAllSatisfiers() {
		logger.trace("Entering processAllSatisfiers");
		try {
			List<String> filtersToWatch = clientProps.getFilterIdsToWatch();
			if (filtersToWatch != null) {
				for (String filterId : filtersToWatch) {
					logger.debug("Processing filterId[" + filterId + "]");
					Long filterID = Long.parseLong(filterId);
					processUsersSatisfyingFilterId(filterID);
				}
			}
			Map<String, String> jqlsToWatch = clientProps.getJql();
			if (jqlsToWatch != null) {
				for (Entry<String, String> aJql : jqlsToWatch.entrySet()) {
					String jqlName = aJql.getKey();
					String jql = aJql.getValue();

					logger.debug("Processing jqlName[" + jqlName + "]");
					processUsersSatisfyingJQL(jqlName, jql);
				}
			}
		} finally {
			logger.trace("Leaving processAllSatisfiers");
		}
	}

	private void processUsersSatisfyingFilterId(long filterID) {
		logger.trace("Entering processUsersSatisfyingFilterId");
		try {
			Filter filter = filterServ.getFilter(filterID);
			SearchResult searchresult = filterServ.getFilterResult(filter);
			for (Issue anIssue : searchresult.getIssues()) {
				String emailId = anIssue.getAssignee() == null ? "" : anIssue.getAssignee().getEmailAddress();
				notifyResult.appendToMap(emailId, helper.buildFilterDetails(filter), helper.buildIssueDetails(anIssue));
			}
		} finally {
			logger.trace("Leaving processUsersSatisfyingFilterId");
		}
	}

	private void processUsersSatisfyingJQL(String jqlName, String jql) {
		logger.trace("Entering processUsersSatisfyingJQL");
		if (jqlName == null || jqlName.isEmpty()) {
			logger.warn("jqlName[" + jqlName + "] was invalid");
			return;
		}
		if (jql == null || jql.isEmpty()) {
			logger.warn("jql[" + jql + "] was invalid");
			return;
		}
		try {
			SearchResult searchresult = filterServ.getFilterResult(jql);
			for (Issue anIssue : searchresult.getIssues()) {
				String emailId = anIssue.getAssignee() == null ? "" : anIssue.getAssignee().getEmailAddress();
				notifyResult.appendToMap(emailId, helper.buildJqlDetails(jqlName, jql),
						helper.buildIssueDetails(anIssue));
			}
		} finally {
			logger.trace("Leaving processUsersSatisfyingJQL");
		}
	}

	private void sendMail() {
		logger.trace("Entering sendMail");

		Date date = new Date();

		for (Entry<String, Map<String, List<String>>> entrySet : notifyResult.getUserToFilterResultsMap().entrySet()) {

			String mailId = entrySet.getKey();

			if (mailId == null || mailId.isEmpty()) {
				logger.warn("Not sending mail to empty mail ID");
				continue;
			}

			if (!mailRecepService.getSafeRecipients().contains(mailId)) {
				logger.debug("Skipping mail to [" + mailId + "]");
				continue;
			}

			logger.debug("Building mail for [" + mailId + "]");

			Map<String, List<String>> existingIssuesForAnUser = entrySet.getValue();

			MailDetails mail = new MailDetails();
			mail.setRecipient(mailId);
			mail.setSubject("JIRA Automater Report | " + date.toString());

			StringBuilder html = new StringBuilder("<h2>JIRA Automater Report for ");
			html.append(mailId);
			html.append(" on ");
			html.append(date.toString());
			html.append("</h2>");
			for (Entry<String, List<String>> aFilterAndItsIssues : existingIssuesForAnUser.entrySet()) {

				String filterID = aFilterAndItsIssues.getKey();
				List<String> issues = aFilterAndItsIssues.getValue();

				html.append("<h3>");
				html.append(filterID);
				html.append("</h3>");

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
		}
		logger.trace("Leaving sendMail");
	}

}
