package com.capitalone.dashboard.client;

import java.util.List;

import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.ScopeOwnerCollectorItem;
import com.capitalone.dashboard.repository.FeatureCollectorRepository;
import com.capitalone.dashboard.repository.ScopeOwnerRepository;
import com.capitalone.dashboard.rest.client.YouTrackRestApi;
import com.capitalone.dashboard.util.FeatureSettings;
import com.capitalone.dashboard.util.YouTrackConstants;

public class TeamDataClient extends BaseClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(TeamDataClient.class);
	@SuppressWarnings("unused")
	private final FeatureSettings featureSettings;
	private final ScopeOwnerRepository teamRepo;
	private final FeatureCollectorRepository featureCollectorRepository;
	private final YouTrackRestApi youTrackRestApi;
	private ObjectId oldTeamId;
	private boolean oldTeamEnabledState;

	public TeamDataClient(FeatureCollectorRepository featureCollectorRepository, FeatureSettings featureSettings,
			ScopeOwnerRepository teamRepository, YouTrackRestApi youTrackRestApi) {
		LOGGER.debug("Constructing data collection for the feature widget, story-level data...");
		this.featureSettings = featureSettings;
		this.featureCollectorRepository = featureCollectorRepository;
		this.teamRepo = teamRepository;
		this.youTrackRestApi = youTrackRestApi;
		this.teamRepo.delete("Closed");
	}

	public void updateTeamInformation() throws HygieiaException {
		JSONArray jsonArray = youTrackRestApi.getTeams();
		updateMongoInfo(jsonArray);
	}

	private void updateMongoInfo(JSONArray tmpMongoDetailArray) {
		for (Object obj : tmpMongoDetailArray) {
			JSONObject dataMainObj = (JSONObject) obj;
			ScopeOwnerCollectorItem team = new ScopeOwnerCollectorItem();

			String teamId = getJSONString(dataMainObj, "shortName");
			/*
			 * Removing any existing entities where they exist in the local DB
			 * store...
			 */
			boolean deleted = this.removeExistingEntity(teamId);

			/*
			 * Team Data
			 */
			// Id
			if (deleted) {
				team.setId(this.getOldTeamId());
				team.setEnabled(this.isOldTeamEnabledState());
			}

			// collectorId
			team.setCollectorId(featureCollectorRepository.findByName(YouTrackConstants.YOUTRACK).getId());

			// teamId
			team.setTeamId(teamId);
			// name
			team.setName(getJSONString(dataMainObj, "name"));
			// changeDate - does not exist for youtrack
			team.setChangeDate("");
			// assetState - does not exist for youtrack
			team.setAssetState("Active");
			// isDeleted - does not exist for youtrack
			team.setIsDeleted("False");
			// Saving back to MongoDB
			teamRepo.save(team);
		}
	}

	private Boolean removeExistingEntity(String localId) {
		if (StringUtils.isEmpty(localId)) {
			return false;
		}
		List<ScopeOwnerCollectorItem> teamIdList = teamRepo.getTeamIdById(localId);
		if (CollectionUtils.isEmpty(teamIdList)) {
			return false;
		}
		ScopeOwnerCollectorItem socItem = teamIdList.get(0);
		if (!localId.equalsIgnoreCase(socItem.getTeamId())) {
			return false;
		}
		this.setOldTeamId(socItem.getId());
		this.setOldTeamEnabledState(socItem.isEnabled());
		teamRepo.delete(socItem.getId());
		return true;
	}

	private ObjectId getOldTeamId() {
		return oldTeamId;
	}

	private void setOldTeamId(ObjectId oldTeamId) {
		this.oldTeamId = oldTeamId;
	}

	private boolean isOldTeamEnabledState() {
		return oldTeamEnabledState;
	}

	private void setOldTeamEnabledState(boolean oldTeamEnabledState) {
		this.oldTeamEnabledState = oldTeamEnabledState;
	}
}
