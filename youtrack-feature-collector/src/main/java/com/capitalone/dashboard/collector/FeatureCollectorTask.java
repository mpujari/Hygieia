package com.capitalone.dashboard.collector;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.capitalone.dashboard.client.TeamDataClient;
import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.FeatureCollector;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.FeatureCollectorRepository;
import com.capitalone.dashboard.repository.FeatureRepository;
import com.capitalone.dashboard.repository.ScopeOwnerRepository;
import com.capitalone.dashboard.repository.ScopeRepository;
import com.capitalone.dashboard.rest.client.YouTrackRestApi;
import com.capitalone.dashboard.rest.client.YouTrackRestApiImpl;
import com.capitalone.dashboard.util.FeatureSettings;
import com.capitalone.dashboard.util.Supplier;
import com.capitalone.dashboard.util.YouTrackConstants;

/**
 * Collects {@link FeatureCollector} data from feature content source system.
 *
 */
@Component
public class FeatureCollectorTask extends CollectorTask<FeatureCollector> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final FeatureSettings featureSettings;

	private final FeatureCollectorRepository featureRespository;

	private final FeatureRepository featureRepository;

	private final ScopeOwnerRepository teamRepository;

	private final ScopeRepository projectRepository;

	private final RestOperations restOperations;

	@Autowired
	public FeatureCollectorTask(TaskScheduler taskScheduler, FeatureSettings featureSettings,
			FeatureCollectorRepository featureRespository, FeatureRepository featureRepository,
			ScopeOwnerRepository teamRepository, ScopeRepository projectRepository,
			Supplier<RestOperations> restOperationsSupplier) {
		super(taskScheduler, YouTrackConstants.YOUTRACK);
		this.featureSettings = featureSettings;
		this.featureRespository = featureRespository;
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
		return featureRespository;
	}

	@Override
	public String getCron() {
		return featureSettings.getCron();
	}

	@Override
	public void collect(FeatureCollector collector) {
		String baseUrl = featureSettings.getYoutrackBaseUrl();
		String login = featureSettings.getLogin();
		String password = featureSettings.getPassword();

		YouTrackRestApi youTrackRestApi = new YouTrackRestApiImpl(baseUrl, restOperations);
		if (StringUtils.isNotBlank(login) && StringUtils.isNotBlank(password)) {
			try {
				youTrackRestApi.login(baseUrl, login, password);
			} catch (HygieiaException e) {
				logger.error("Log-in problem: " + e.getMessage(), e);
				return;
			}
		}

		TeamDataClient teamDataClient = new TeamDataClient(this.featureRespository, this.featureSettings,
				this.teamRepository, youTrackRestApi);
		try {
			teamDataClient.updateTeamInformation();
		} catch (HygieiaException he) {
			logger.error("Error in collecting Youtrack Data: [" + he.getErrorCode() + "] " + he.getMessage(), he);
		}
		logger.info("Feature Data Collection Finished");
	}

}
