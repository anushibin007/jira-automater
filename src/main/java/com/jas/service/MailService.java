package com.jas.service;

import java.util.HashSet;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jas.pojo.MailDetails;
import com.jas.util.FileUtils;

@Service
public class MailService {

	Logger logger = LoggerFactory.getLogger(MailService.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	private boolean MAIL_FLAG = true;

	public static Set<String> safeRecipients = new HashSet<>(FileUtils.fileToStringArray("mail-recipients.properties"));

	public String sendMail(MailDetails details) {
		if (MAIL_FLAG) {
			if (details != null) {
				String recipient = details.getRecipient();
				if (recipient != null && safeRecipients.contains(recipient)) {
					logger.debug("Sending mail at " + System.currentTimeMillis());
					try {

						// Creating a simple mail message
						MimeMessage mimeMessage = javaMailSender.createMimeMessage();

						MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, true);

						// Setting up necessary details
						mailMessage.setFrom(sender);
						mailMessage.setTo(recipient);
						mailMessage.setText(details.getMsgBody(), true);
						mailMessage.setSubject(details.getSubject());

						// Sending the mail
						javaMailSender.send(mimeMessage);
						return "Mail Sent Successfully...";
					}

					// Catch block to handle the exceptions
					catch (Exception e) {
						e.printStackTrace();
						return "Error while Sending Mail: " + e.getMessage();
					}
				} else {
					String warnMsg = "recipient[" + recipient + "] was not in safe sender list. Not sending mail.";
					logger.warn(warnMsg);
					return warnMsg;
				}
			} else {
				return "details was null";
			}
		} else {
			String warnMsg = "Mail not send because MAIL_FLAG is false";
			logger.warn(warnMsg);
			return warnMsg;
		}
	}
}
