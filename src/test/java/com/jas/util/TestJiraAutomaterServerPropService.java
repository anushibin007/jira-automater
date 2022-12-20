package com.jas.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestJiraAutomaterServerPropService {

	@Autowired
	JiraAutomaterServerPropService bean;

	@Test
	public void verifyValueInjectionNotNull() {
		assertNotEquals(null, bean.getUrl());
		assertNotEquals(null, bean.getUsername());
		assertNotEquals(null, bean.getPassword());
	}

	@Test
	public void verifyValueInjectionNotEmpty() {
		assertNotEquals("", bean.getUrl());
		assertNotEquals("", bean.getUsername());
		assertNotEquals("", bean.getPassword());
	}

	@Test
	public void verifyValueInjectionPositive() {
		assertEquals("test-jira-server-url", bean.getUrl());
		assertEquals("test-jira-server-username", bean.getUsername());
		assertEquals("test-jira-server-password", bean.getPassword());
	}
}
