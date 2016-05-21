package com.capitalone.dashboard.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.capitalone.dashboard.json.AgileBoardInfo;
import com.capitalone.dashboard.json.Sprint;
import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.rest.client.YouTrackRestApi;

public class SprintInfoClient {

	private final YouTrackRestApi youTrackRestApi;

	public SprintInfoClient(YouTrackRestApi youTrackRestApi) {
		this.youTrackRestApi = youTrackRestApi;
	}

	public Map<String, Sprint> getSprintNameMap() throws HygieiaException {
		Map<String, Sprint> sprintNameMap = new HashMap<>();
		List<AgileBoardInfo> agileInfos = youTrackRestApi.getAgileInfos();
		for (AgileBoardInfo boardInfo : agileInfos) {
			List<Sprint> sprints = youTrackRestApi.getSprints(boardInfo.getId());
			for (Sprint s : sprints) {
				sprintNameMap.put(s.getName().trim().toLowerCase(), s);
			}
		}
		return sprintNameMap;
	}

}
