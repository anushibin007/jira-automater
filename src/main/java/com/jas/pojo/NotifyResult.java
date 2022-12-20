package com.jas.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyResult {
	/**
	 * Think of it like this JSON:
	 * 
	 * <pre>
	 * {
			"user1":{
				"1998785":[
					"PROJ-12745",
					"PROJ-12547"
				],
				"2048798":[
					"PROJ-9948",
					"PROJ-5078"
				]
			},
			"user2":{
				"1998785":[
					"PROJ-17987",
					"PROJ-8978"
				],
				"2048798":[
					"PROJ-1278",
					"PROJ-9878"
				]
			}
		}
	 * </pre>
	 */
	private Map<String, Map<String, List<String>>> userToFilterResultsMap = new HashMap<>();

	Logger logger = LoggerFactory.getLogger(NotifyResult.class);

	public Map<String, Map<String, List<String>>> getUserToFilterResultsMap() {
		return userToFilterResultsMap;
	}

	public void appendToMap(String user, String filterId, String issue) {
		if (user == null || issue == null) {
			logger.debug(
					"Skipped as one of userToIssuesMap[<masked>], user[" + user + "], issue[" + issue + "] was null");
			return;
		}
		Map<String, List<String>> existingIssuesForAnUser = userToFilterResultsMap.get(user);
		if (existingIssuesForAnUser == null) { // this means that the user itself is not present already

			// Build a new first filter result for the user
			List<String> firstFilterResult = new ArrayList<>(Arrays.asList(issue));

			// Associate the filter ID to the filter result
			Map<String, List<String>> filterIdToFilterResult = new HashMap<>();
			filterIdToFilterResult.put(filterId, firstFilterResult);

			// Push the new user to the cache
			userToFilterResultsMap.put(user, filterIdToFilterResult);
		} else { // the user is there
			List<String> existingFilterResultsForAFilterId = existingIssuesForAnUser.get(filterId);
			if (existingFilterResultsForAFilterId == null) { // the filter ID does not exist for this user

				// Build a new first filter result for the user
				List<String> firstFilterResult = new ArrayList<>(Arrays.asList(issue));

				// Add this to the existing user
				existingIssuesForAnUser.put(filterId, firstFilterResult);
			} else {
				existingFilterResultsForAFilterId.add(issue);
			}
		}
	}

}
