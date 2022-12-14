package com.jas.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jas.pojo.MailDetails;
import com.jas.service.MailService;

@Component
@Scope("prototype")
public class MailThread implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(MailThread.class);

	private MailDetails mail;

	@Autowired
	MailService mailServ;

	public MailDetails getMail() {
		return mail;
	}

	public void setMail(MailDetails mail) {
		this.mail = mail;
	}

	@Override
	public void run() {
		if (mail == null) {
			throw new NullPointerException("mail is null in " + this.getClass().getName() + ". Cannot send mail");
		}
		logger.debug("Sending mail[" + mail.hashCode() + "] to [" + mail.getRecipient() + "]");
		mailServ.sendMail(mail);
		logger.debug("Sent mail[" + mail.hashCode() + "] to [" + mail.getRecipient() + "]");
		this.mail = null;
	}

}
