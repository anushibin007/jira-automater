package com.jas.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailRecepientsService {

	@Value("#{'${jira.automater.jira-client.mail-recepients}'.split(',')}")
	private List<String> safeRecipients;

	public List<String> getSafeRecipients() {
		return safeRecipients;
	}

	public void setSafeRecipients(List<String> safeRecipients) {
		this.safeRecipients = safeRecipients;
	}

}
