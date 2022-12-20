package com.jas;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JiraAutomaterApplicationTests {

	Logger logger = LoggerFactory.getLogger(JiraAutomaterApplicationTests.class);

	@Test
	void contextLoads() {
		logger.debug("Context Loaded");
	}

}
