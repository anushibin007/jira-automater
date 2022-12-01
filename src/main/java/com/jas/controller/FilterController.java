package com.jas.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atlassian.jira.rest.client.api.domain.Filter;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.jas.service.FilterService;
import com.jas.service.MailService;
import com.jas.service.NotifyService;

@RestController
public class FilterController {

	Logger logger = LoggerFactory.getLogger(FilterController.class);

	@Autowired
	FilterService filterServ;

	@Autowired
	MailService mailServ;

	@Autowired
	NotifyService notifyServ;

	@RequestMapping(value = "/filter", method = RequestMethod.GET)
	public Filter getFilter(@RequestParam("filterID") long filterID) {
		return filterServ.getFilter(filterID);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public SearchResult getSearch(@RequestParam("filterID") long filterID) {
		return filterServ.getFilterResult(filterID);
	}

	@RequestMapping(value = "/notify", method = RequestMethod.GET)
	public Map<String, List<String>> notify(@RequestParam("filterID") long filterID) {
		return notifyServ.notifyFilterSatisfiers(filterID);
	}

	@RequestMapping(value = "/notifyAllFilterSatisfiers", method = RequestMethod.GET)
	public String notifyAllFilterSatisfiers() {
		return notifyServ.notifyAllFilterSatisfiers();
	}

}
