package com.capitalone.dashboard.client;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.repository.FeatureCollectorRepository;
import com.capitalone.dashboard.repository.ScopeRepository;
import com.capitalone.dashboard.util.FeatureSettings;

public class ProjectDataClient extends BaseClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDataClient.class);

	private final FeatureSettings featureSettings;
	private final ScopeRepository projectRepo;
	private final FeatureCollectorRepository featureCollectorRepository;

	public ProjectDataClient(FeatureSettings featureSettings, ScopeRepository projectRepository,
			FeatureCollectorRepository featureCollectorRepository) {
		LOGGER.debug("Constructing data collection for the feature widget, story-level data...");

		this.featureSettings = featureSettings;
		this.projectRepo = projectRepository;
		this.featureCollectorRepository = featureCollectorRepository;
	}

	protected void updateMongoInfo(JSONArray tmpMongoDetailArray) {
	}

	public String getMaxChangeDate() {
		return null;
	}

	public void updateProjectInformation() throws HygieiaException {
	}

	public void updateObjectInformation(String query) throws HygieiaException {
	}

	protected void removeExistingEntity(String localId) {
	}

}
