package com.capitalone.dashboard.collector;

import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.util.YouTrackConstants;

/**
 * TODO java doc
 */
public class YouTrackCollector extends Collector {

	public static YouTrackCollector prototype() {
		YouTrackCollector protoType = new YouTrackCollector();
		protoType.setName(YouTrackConstants.YOUTRACK);
		protoType.setOnline(true);
		protoType.setEnabled(true);
		protoType.setCollectorType(CollectorType.ScopeOwner);
		protoType.setLastExecuted(System.currentTimeMillis());
		return protoType;
	}

}
