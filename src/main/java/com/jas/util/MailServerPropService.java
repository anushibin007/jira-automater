package com.jas.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailServerPropService {

	@Value("${spring.mail.host}")
	private String host;

	@Value("${spring.mail.port}")
	private String port;

	@Value("${spring.mail.username}")
	private String username;

	public void validate() {
		if (host == null || host.trim().isEmpty()) {
			throw new NullPointerException(
					"spring.mail.host is either not present or is empty in application.properties");
		}
		if (port == null || port.trim().isEmpty()) {
			throw new NullPointerException(
					"spring.mail.host is either not present or is empty in application.properties");
		}
		if (username == null || username.trim().isEmpty()) {
			throw new NullPointerException(
					"spring.mail.username is either not present or is empty in application.properties");
		}
	}
}
