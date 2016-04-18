package com.capitalone.dashboard.client;

import java.util.List;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.Feature;
import com.capitalone.dashboard.repository.FeatureCollectorRepository;
import com.capitalone.dashboard.repository.FeatureRepository;
import com.capitalone.dashboard.util.FeatureCollectorConstants;
import com.capitalone.dashboard.util.FeatureSettings;
import com.capitalone.dashboard.util.YouTrackConstants;

public class StoryDataClient extends BaseClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(StoryDataClient.class);

	private final FeatureSettings featureSettings;
	private final FeatureCollectorRepository featureCollectorRepository;
	private final FeatureRepository featureRepo;

	public StoryDataClient(FeatureSettings featureSettings, FeatureRepository featureRepository,
			FeatureCollectorRepository featureCollectorRepository) {
		LOGGER.debug("Constructing data collection for the feature widget, story-level data...");

		this.featureSettings = featureSettings;
		this.featureRepo = featureRepository;
		this.featureCollectorRepository = featureCollectorRepository;
	}

	protected void updateMongoInfo(JSONArray tmpMongoDetailArray) {
	}

	public void updateStoryInformation() throws HygieiaException {
	}

	protected void removeExistingEntity(String localId) {
		if (StringUtils.isEmpty(localId))
			return;
		List<Feature> listOfFeature = featureRepo.getFeatureIdById(localId);

		if (CollectionUtils.isEmpty(listOfFeature))
			return;
		featureRepo.delete(listOfFeature);
	}

	public String getMaxChangeDate() {
		Collector col = featureCollectorRepository.findByName(YouTrackConstants.YOUTRACK);
		if (col == null)
			return "";
		if (StringUtils.isEmpty(featureSettings.getDeltaStartDate()))
			return "";

		List<Feature> response = featureRepo
				.findTopByCollectorIdAndChangeDateGreaterThanOrderByChangeDateDesc(col.getId(),
						featureSettings.getDeltaStartDate());
		if (!CollectionUtils.isEmpty(response))
			return response.get(0).getChangeDate();
		return "";
	}

	public void updateObjectInformation(String query) throws HygieiaException {
	}

}
