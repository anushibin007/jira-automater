package com.jas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicVotes;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;

@Service
public class IssueService {

	@Autowired
	private JiraClientService restClient;

	public String createIssue(String projectKey, Long issueType, String issueSummary) {
		IssueRestClient issueClient = getRestClient().getIssueClient();
		IssueInput newIssue = new IssueInputBuilder(projectKey, issueType, issueSummary).build();
		return issueClient.createIssue(newIssue).claim().getKey();
	}

	public void updateIssueDescription(String issueKey, String newDescription) {
		IssueInput input = new IssueInputBuilder().setDescription(newDescription).build();
		getRestClient().getIssueClient().updateIssue(issueKey, input).claim();
	}

	public Issue getIssue(String issueKey) {
		return getRestClient().getIssueClient().getIssue(issueKey).claim();
	}

	public void voteForAnIssue(Issue issue) {
		getRestClient().getIssueClient().vote(issue.getVotesUri()).claim();
	}

	public int getTotalVotesCount(String issueKey) {
		BasicVotes votes = getIssue(issueKey).getVotes();
		return votes == null ? 0 : votes.getVotes();
	}

	public void addComment(Issue issue, String commentBody) {
		getRestClient().getIssueClient().addComment(issue.getCommentsUri(), Comment.valueOf(commentBody));
	}

	private JiraRestClient getRestClient() {
		return restClient.getJiraRestClient();
	}
}
