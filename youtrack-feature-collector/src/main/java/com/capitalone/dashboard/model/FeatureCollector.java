package com.capitalone.dashboard.model;

import com.capitalone.dashboard.util.YouTrackConstants;

/**
 * TODO java doc
 */
public class FeatureCollector extends Collector {

	public static FeatureCollector prototype() {
		FeatureCollector protoType = new FeatureCollector();
		protoType.setName(YouTrackConstants.YOUTRACK);
		protoType.setOnline(true);
		protoType.setEnabled(true);
		protoType.setCollectorType(CollectorType.ScopeOwner);
		protoType.setLastExecuted(System.currentTimeMillis());
		return protoType;
	}

}
