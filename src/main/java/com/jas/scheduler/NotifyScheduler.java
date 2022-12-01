package com.jas.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jas.service.NotifyService;

@Component
public class NotifyScheduler {

	Logger logger = LoggerFactory.getLogger(NotifyScheduler.class);

	@Autowired
	NotifyService notifyServ;

	@Scheduled(cron = "${jira.automater.interval.cron}")
	public void notifyAllFilterSatisfiers() {
		logger.debug("Scheduler is invoking notifyAllFilterSatisfiers at " + System.currentTimeMillis());
		notifyServ.notifyAllFilterSatisfiers();
	}

}
