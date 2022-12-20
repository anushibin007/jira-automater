package com.jas.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestJiraAutomaterClientPropService {

	@Autowired
	JiraAutomaterClientPropService bean;

	private List<Object> expectedFilterIdsToWatch = null;
	private Map<String, String> expectedJql = null;

	TestJiraAutomaterClientPropService() {

		// Load the expected values

		expectedFilterIdsToWatch = new ArrayList<>();
		expectedFilterIdsToWatch.add("1021");
		expectedFilterIdsToWatch.add("1022");
		expectedFilterIdsToWatch.add("1023");
		expectedFilterIdsToWatch.add("1024");

		expectedJql = new HashMap<>();
		expectedJql.put("missing-story-points", "project = SAMPLE and StoryPoints is EMPTY");
		expectedJql.put("invalid-fix-version", "project = SAMPLE and fixVersion is EMPTY");
		expectedJql.put("1", "project = SAMPLE and assignee is currentUser()");
		expectedJql.put("2", "project = SAMPLE and reporter is currentUser()");
	}

	@Test
	public void verifyValueInjectionNotNull() {
		assertNotEquals(null, bean.getFilterIdsToWatch());
		assertNotEquals(null, bean.getJql());
	}

	@Test
	public void verifyValueInjectionNotEmpty() {
		assertNotEquals(0, bean.getFilterIdsToWatch().size());
		assertNotEquals(0, bean.getJql().size());
	}

	@Test
	public void verifyValueInjectionPositive() {
		assertEquals(expectedFilterIdsToWatch, bean.getFilterIdsToWatch());
		assertEquals(expectedJql, bean.getJql());
	}
}
