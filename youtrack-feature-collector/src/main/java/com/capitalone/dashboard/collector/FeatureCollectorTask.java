package com.capitalone.dashboard.collector;

import static com.capitalone.dashboard.util.YouTrackConstants.YOUTRACK;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.capitalone.dashboard.client.ProjectDataClient;
import com.capitalone.dashboard.client.StoryDataClient;
import com.capitalone.dashboard.client.TeamDataClient;
import com.capitalone.dashboard.json.FeatureSettings;
import com.capitalone.dashboard.model.FeatureCollector;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.FeatureCollectorRepository;
import com.capitalone.dashboard.repository.FeatureRepository;
import com.capitalone.dashboard.repository.ScopeOwnerRepository;
import com.capitalone.dashboard.repository.ScopeRepository;
import com.capitalone.dashboard.rest.client.YouTrackRestApi;
import com.capitalone.dashboard.rest.client.YouTrackRestApiImpl;
import com.capitalone.dashboard.util.CoreFeatureSettings;
import com.capitalone.dashboard.util.Supplier;

/**
 * Collects {@link FeatureCollector} data from feature content source system.
 *
 */
@Component
public class FeatureCollectorTask extends CollectorTask<FeatureCollector> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final CoreFeatureSettings coreFeatureSettings;
	private final FeatureSettings featureSettings;

	private final FeatureCollectorRepository featureCollectorRepository;

	private final FeatureRepository featureRepository;

	private final ScopeOwnerRepository teamRepository;

	private final ScopeRepository projectRepository;

	private final RestOperations restOperations;

	@Autowired
	public FeatureCollectorTask(CoreFeatureSettings coreFeatureSettings, TaskScheduler taskScheduler,
			FeatureSettings featureSettings, FeatureCollectorRepository featureCollectorRepository,
			FeatureRepository featureRepository, ScopeOwnerRepository teamRepository, ScopeRepository projectRepository,
			Supplier<RestOperations> restOperationsSupplier) {
		super(taskScheduler, YOUTRACK);
		this.coreFeatureSettings = coreFeatureSettings;
		this.featureSettings = featureSettings;
		this.featureCollectorRepository = featureCollectorRepository;
		this.featureRepository = featureRepository;
		this.teamRepository = teamRepository;
		this.projectRepository = projectRepository;
		this.restOperations = restOperationsSupplier.get();
	}

	@Override
	public FeatureCollector getCollector() {
		return FeatureCollector.prototype();
	}

	@Override
	public BaseCollectorRepository<FeatureCollector> getCollectorRepository() {
		return featureCollectorRepository;
	}

	@Override
	public String getCron() {
		return featureSettings.getCron();
	}

	@Override
	public void collect(FeatureCollector collector) {
		String baseUrl = featureSettings.getYoutrackBaseUrl();
		YouTrackRestApi restApi = new YouTrackRestApiImpl(baseUrl, restOperations);
		boolean loginSucess = login(restApi);
		if (!loginSucess) {
			return;// don't do anythibng
		}

		TeamDataClient teamDataClient = new TeamDataClient(featureCollectorRepository, teamRepository, restApi);
		// update
		teamDataClient.updateTeamInformation();

		ProjectDataClient projectDataClient = new ProjectDataClient(projectRepository, featureCollectorRepository,
				restApi);
		// update
		projectDataClient.updateProjectInformation();

		StoryDataClient storyDataClient = new StoryDataClient(coreFeatureSettings, featureSettings, featureRepository,
				featureCollectorRepository, restApi);
		storyDataClient.updateStoryInformation();

		logger.info("Feature Data Collection Finished");
	}

	private boolean login(YouTrackRestApi restApi) {
		String login = featureSettings.getLogin();
		String password = featureSettings.getPassword();
		if (StringUtils.isNotBlank(login) && StringUtils.isNotBlank(password)) {
			try {
				restApi.login(login, password);
			} catch (Exception e) {
				logger.error("Log-in problem: " + e.getMessage(), e);
				return false;
			}
		}
		return true;
	}

}
