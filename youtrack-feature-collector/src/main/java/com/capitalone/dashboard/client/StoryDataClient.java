package com.capitalone.dashboard.client;

import static com.capitalone.dashboard.util.YouTrackConstants.YOUTRACK;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.capitalone.dashboard.json.FeatureSettings;
import com.capitalone.dashboard.json.Sprint;
import com.capitalone.dashboard.json.YouTrackIssue;
import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.Feature;
import com.capitalone.dashboard.model.FeatureStatus;
import com.capitalone.dashboard.repository.FeatureCollectorRepository;
import com.capitalone.dashboard.repository.FeatureRepository;
import com.capitalone.dashboard.rest.client.YouTrackRestApi;
import com.capitalone.dashboard.util.CoreFeatureSettings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class StoryDataClient extends BaseClient {

	private static final String ESTIMATION_FIELD = "Estimation";

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final CoreFeatureSettings coreFeatureSettings;
	private final FeatureSettings featureSettings;
	private final FeatureCollectorRepository featureCollectorRepository;
	private final FeatureRepository featureRepo;
	private final YouTrackRestApi youTrackRestApi;
	private final Map<String, String> estimateNameMinMap = Maps.newHashMap();

	public StoryDataClient(CoreFeatureSettings coreFeatureSettings, FeatureSettings featureSettings,
			FeatureRepository featureRepository, FeatureCollectorRepository featureCollectorRepository,
			YouTrackRestApi youTrackRestApi) {
		logger.debug("Constructing data collection for the feature widget, story-level data...");
		this.coreFeatureSettings = coreFeatureSettings;
		this.featureSettings = featureSettings;
		this.featureRepo = featureRepository;
		this.featureCollectorRepository = featureCollectorRepository;
		this.youTrackRestApi = youTrackRestApi;
		init();
	}

	private void init() {
		List<String> estimateValues = featureSettings.getYoutrackEstimationFieldValues();
		if (estimateValues == null) {
			// don't do anything
			return;
		}
		for (String estimateValue : estimateValues) {
			if (StringUtils.isNotBlank(estimateValue)) {
				// eg. 'XXS (~ 0.5 days):240'
				String estimateName = StringUtils.substringBefore(estimateValue, ":");
				String minutes = StringUtils.substringAfter(estimateValue, ":");
				if (StringUtils.isNotBlank(minutes)) {
					estimateNameMinMap.put(estimateName.trim().toLowerCase(), minutes.trim());
				}
			}
		}
	}

	public void updateStoryInformation() {
		try {
			// 2008-01-01 .. Today [2008-01-01+..+Today]
			// TODO validate for maxChangeDate
			String maxChangeDate = "updated:" + getMaxChangeDate() + " .. Today";
			List<YouTrackIssue> youTrackIssues = youTrackRestApi.getYouTrackIssues(maxChangeDate,
					String.valueOf(Integer.MAX_VALUE));
			updateMongoInfo(youTrackIssues);
		} catch (Exception e) {
			logger.error("Error in collecting Youtrack Data: " + e.getMessage(), e);
		}
	}

	@SuppressWarnings({ "PMD.ExcessiveMethodLength", "PMD.NPathComplexity", "unchecked" })
	private void updateMongoInfo(List<YouTrackIssue> youTrackIssues) {
		if (youTrackIssues == null) {
			return; // nothing to do
		}
		Map<String, Sprint> sprintNameMap = getSprintInfo();
		for (YouTrackIssue trackIssue : youTrackIssues) {
			Feature feature = new Feature();

			feature.setsId(trackIssue.getId());

			// remove existing
			removeExistingEntity(trackIssue.getId());

			// collectorId
			feature.setCollectorId(featureCollectorRepository.findByName(YOUTRACK).getId());

			feature.setsNumber(trackIssue.getFieldValueStr("numberInProject"));

			// sName
			feature.setsName(trackIssue.getFieldValueStr("summary"));

			String statusStr = (String) trackIssue.getFieldValueFromArray("State", 0);
			FeatureStatus canonicalFeatureStatus = toCanonicalFeatureStatus(statusStr);
			// sStatus
			feature.setsStatus(canonicalFeatureStatus.getStatus());

			// sState
			feature.setsState(toCanonicalFeatureState(canonicalFeatureStatus));

			// sEstimate,*
			feature.setsEstimate(getsEstimate(trackIssue));

			// sChangeDate
			feature.setChangeDate(getDateString(trackIssue.getFieldValueStr("updated")));

			// IsDeleted - does not exist for YouTrack
			feature.setIsDeleted("False");

			// sProjectID // projectShortName
			String projectShortName = trackIssue.getFieldValueStr("projectShortName");
			feature.setsProjectID(projectShortName);

			// sProjectName
			feature.setsProjectName(projectShortName);

			// sProjectBeginDate - does not exist in YouTrack
			feature.setsProjectBeginDate("");

			// sProjectEndDate - does not exist in YouTrack
			feature.setsProjectEndDate("");

			// sProjectChangeDate - does not exist for this asset
			// level in YouTrack
			feature.setsProjectChangeDate("");

			// sProjectState - does not exist in YouTrack
			feature.setsProjectState("");

			// sProjectIsDeleted - does not exist in YouTrack
			feature.setsProjectIsDeleted("False");

			// sProjectPath - does not exist in YouTrack
			feature.setsProjectPath("");

			// TODO Epic
			feature.setsEpicID("");
			feature.setsEpicNumber("");
			feature.setsEpicName("");
			feature.setsEpicBeginDate("");
			feature.setsEpicEndDate("");
			feature.setsEpicType("");
			feature.setsEpicAssetState("");
			feature.setsEpicChangeDate("");
			// sEpicType - does not exist in YouTrack
			feature.setsEpicType("");
			// sEpicChangeDate - does not exist in YouTrack
			feature.setsEpicChangeDate("");
			// sEpicIsDeleted - does not exist in YouTrack
			feature.setsEpicIsDeleted("False");

			// Sprint
			List<String> sprints = (List<String>) trackIssue
					.getFieldValueObj(featureSettings.getYoutrackSprintFieldName());
			Sprint sprint = null;
			String latestSprint = null;
			if (sprints != null && !sprints.isEmpty()) {
				latestSprint = sprints.get(sprints.size() - 1);
				sprint = sprintNameMap.get(latestSprint.trim().toLowerCase());
			}
			if (sprint != null) {
				feature.setsSprintID(latestSprint);
				feature.setsSprintName(sprint.getName());
				feature.setsSprintBeginDate(sprint.getStartDate());
				feature.setsSprintEndDate(sprint.getFinishDate());
			} else {
				feature.setsSprintID(latestSprint);
				feature.setsSprintName("");
				feature.setsSprintBeginDate("");
				feature.setsSprintEndDate("");
			}
			// sSprintAssetState - does not exist in YouTrack
			feature.setsSprintAssetState("");
			// sSprintChangeDate - does not exist in YouTrack
			feature.setsSprintChangeDate("");
			// sSprintIsDeleted - does not exist in YouTrack
			feature.setsSprintIsDeleted("False");

			// sTeamID
			feature.setsTeamID(projectShortName);
			// sTeamName
			feature.setsTeamName(projectShortName);
			// sTeamChangeDate - not able to retrieve at this asset
			// level
			// from YouTrack
			feature.setsTeamChangeDate("");
			// sTeamAssetState
			feature.setsTeamAssetState("");
			// sTeamIsDeleted
			feature.setsTeamIsDeleted("False");

			Map<?, ?> assignees = (Map<?, ?>) trackIssue.getFieldValueFromArray("Assignee", 0);
			String assigneeFullName = "";
			String assigneeName = "";
			if (assignees != null) {
				assigneeFullName = (String) assignees.get("fullName");
				assigneeName = (String) assignees.get("value");
			}
			feature.setsOwnersShortName(Lists.newArrayList(assigneeName));
			feature.setsOwnersUsername(Lists.newArrayList(assigneeName));
			feature.setsOwnersID(Lists.newArrayList(assigneeName));
			feature.setsOwnersFullName(Lists.newArrayList(assigneeFullName));

			// sOwnersState - does not exist in YouTrack at this level
			List<String> assigneeActive = new ArrayList<String>();
			assigneeActive.add("Active");
			feature.setsOwnersState(assigneeActive);

			// sOwnersChangeDate - does not exist in YouTrack
			feature.setsOwnersChangeDate(Lists.newArrayList());
			// sOwnersIsDeleted - does not exist in YouTrack
			feature.setsOwnersIsDeleted(Lists.newArrayList());
			// System.out.println(assignees);
			featureRepo.save(feature);
		}
		logger.info("Size:{}", youTrackIssues.size());
	}

	private Map<String, Sprint> getSprintInfo() {
		SprintInfoClient sprintInfoClient = new SprintInfoClient(youTrackRestApi);
		try {
			return sprintInfoClient.getSprintNameMap();
		} catch (HygieiaException e) {
			logger.error("Error during collecting sprint information", e);
		}
		return Maps.newHashMap();// don't do anythibng
	}

	/**
	 * We first check for custom estimate field i.e.
	 * youtrackEstimationFieldName, if its found we use it or else we check for
	 * 'Estimation' field
	 * 
	 * If we get field specified by 'youtrackEstimationFieldName' then we depend
	 * on 'youtrackEstimationFieldValues' values to map custom estimation values
	 * to hours.
	 * 
	 * 'Estimation' field value will be in Minutes.
	 * 
	 * @param trackIssue
	 * @return
	 */
	private String getsEstimate(YouTrackIssue trackIssue) {
		String estimateFieldName = StringUtils.isNotBlank(featureSettings.getYoutrackEstimationFieldName())
				? featureSettings.getYoutrackEstimationFieldName() : ESTIMATION_FIELD;
		String estimate = (String) trackIssue.getFieldValueFromArray(estimateFieldName, 0);
		if (StringUtils.isBlank(estimate)) {
			estimate = (String) trackIssue.getFieldValueFromArray(ESTIMATION_FIELD, 0);
		}
		if (StringUtils.isNotBlank(estimate)) {
			if (ESTIMATION_FIELD.equals(estimateFieldName)) {
				// Estimation field contains minutes, convert it into hours
				estimate = (Integer.valueOf(estimate) / 60) + "";
			} else {
				estimate = estimateNameMinMap.get(StringUtils.trimToEmpty(estimate).toLowerCase());
			}
		}
		return StringUtils.isNotBlank(estimate) ? estimate : "";
	}

	private void removeExistingEntity(String localId) {
		if (StringUtils.isEmpty(localId)) {
			return;
		}
		List<Feature> listOfFeature = featureRepo.getFeatureIdById(localId);

		if (CollectionUtils.isEmpty(listOfFeature)) {
			return;
		}
		featureRepo.delete(listOfFeature);
	}

	private String getMaxChangeDate() {
		Collector col = featureCollectorRepository.findByName(YOUTRACK);
		String masterStartDate = featureSettings.getMasterStartDate();
		if (col != null) {
			List<Feature> response = featureRepo
					.findTopByCollectorIdAndChangeDateGreaterThanOrderByChangeDateDesc(col.getId(), masterStartDate);
			if (!CollectionUtils.isEmpty(response) && response.get(0).getChangeDate() != null) {
				// remove time info from '2016-05-08T04:25+0000'
				String changeDate = response.get(0).getChangeDate();
				changeDate = changeDate.substring(0, changeDate.indexOf('T'));
				return changeDate;
			}
		}
		return masterStartDate;
	}

	private FeatureStatus toCanonicalFeatureStatus(String nativeStatus) {
		List<String> todo = coreFeatureSettings.getTodoStatuses();
		List<String> doing = coreFeatureSettings.getDoingStatuses();
		List<String> done = coreFeatureSettings.getDoneStatuses();
		boolean alreadySet = false;
		FeatureStatus canonicalStatus = FeatureStatus.BACKLOG;

		if (!nativeStatus.isEmpty()) {
			// Map todo
			for (String status : todo) {
				if (status.equalsIgnoreCase(nativeStatus)) {
					canonicalStatus = FeatureStatus.BACKLOG;
					alreadySet = true;
					break;
				}
			}
			// Map doing
			if (!alreadySet) {
				for (String status : doing) {
					if (status.equalsIgnoreCase(nativeStatus)) {
						canonicalStatus = FeatureStatus.IN_PROGRESS;
						alreadySet = true;
						break;
					}
				}
			}
			// Map done
			if (!alreadySet) {
				for (String status : done) {
					if (status.equalsIgnoreCase(nativeStatus)) {
						canonicalStatus = FeatureStatus.DONE;
						alreadySet = true;
						break;
					}
				}
			}

			if (!alreadySet) {
				canonicalStatus = FeatureStatus.BACKLOG;
			}
		} else {
			canonicalStatus = FeatureStatus.BACKLOG;
		}

		return canonicalStatus;
	}

	private String toCanonicalFeatureState(FeatureStatus nativeStatus) {
		if (nativeStatus == FeatureStatus.IN_PROGRESS || nativeStatus == FeatureStatus.WAITING) {
			return "Active";
		}
		return nativeStatus.getStatus();
	}

}
