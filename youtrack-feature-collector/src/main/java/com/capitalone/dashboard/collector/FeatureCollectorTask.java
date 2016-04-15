package com.capitalone.dashboard.collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.model.FeatureCollector;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.FeatureCollectorRespository;
import com.capitalone.dashboard.util.YouTrackConstants;
import com.capitalone.dashboard.util.FeatureSettings;

/**
 * Collects {@link FeatureCollector} data from feature content source system.
 *
 */
@Component
public class FeatureCollectorTask extends CollectorTask<FeatureCollector> {

	private final FeatureSettings featureSettings;

	private final FeatureCollectorRespository respository;

	@Autowired
	public FeatureCollectorTask(TaskScheduler taskScheduler, FeatureSettings featureSettings,
			FeatureCollectorRespository respository) {
		super(taskScheduler, YouTrackConstants.YOUTRACK);
		this.featureSettings = featureSettings;
		this.respository = respository;
	}

	@Override
	public FeatureCollector getCollector() {
		return FeatureCollector.prototype();
	}

	@Override
	public BaseCollectorRepository<FeatureCollector> getCollectorRepository() {
		return respository;
	}

	@Override
	public String getCron() {
		return featureSettings.getCron();
	}

	@Override
	public void collect(FeatureCollector collector) {
	}

}
