package com.capitalone.dashboard.collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.YouTrackCollectorRespository;
import com.capitalone.dashboard.util.YouTrackConstants;

@Component
public class YouTrackCollectorTask extends CollectorTask<YouTrackCollector> {

	private YouTrackSettings youTrackSettings;

	private YouTrackCollectorRespository respository;

	@Autowired
	public YouTrackCollectorTask(TaskScheduler taskScheduler, YouTrackSettings youTrackSettings,
			YouTrackCollectorRespository respository) {
		super(taskScheduler, YouTrackConstants.YOUTRACK);
		this.youTrackSettings = youTrackSettings;
		this.respository = respository;
	}

	@Override
	public YouTrackCollector getCollector() {
		return YouTrackCollector.prototype();
	}

	@Override
	public BaseCollectorRepository<YouTrackCollector> getCollectorRepository() {
		return respository;
	}

	@Override
	public String getCron() {
		return youTrackSettings.getCron();
	}

	@Override
	public void collect(YouTrackCollector collector) {
	}

}
