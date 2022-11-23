package com.jas;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.jas.util.JiraAutomaterPropService;
import com.jas.util.MailServerPropService;

@SpringBootApplication
public class JiraAutomaterApplication {

	@Autowired
	JiraAutomaterPropService jiraAutomaterPropService;

	@Autowired
	MailServerPropService mailServerPropService;

	public static void main(String[] args) {
		preInit();
		SpringApplication.run(JiraAutomaterApplication.class, args);
	}

	private static void preInit() {
		ObjectMapper mapper = new ObjectMapper();

		// Register JODA
		// https://stackoverflow.com/a/15605404/6922308
		mapper.registerModule(new JodaModule());
	}

	@PostConstruct
	public void postInit() {
		jiraAutomaterPropService.validate();
		mailServerPropService.validate();
	}
}
