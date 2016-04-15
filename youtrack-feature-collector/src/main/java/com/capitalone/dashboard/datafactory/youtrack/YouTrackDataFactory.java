package com.capitalone.dashboard.datafactory.youtrack;

import org.json.simple.JSONArray;

import com.capitalone.dashboard.datafactory.DataFactory;
import com.capitalone.dashboard.misc.HygieiaException;


/**
 * Interface for YouTrack data connection. An implemented class should be able
 * to create a formatted request, and retrieve a response in JSON syntax from
 * that request to YouTrack.
 *
 */
public interface YouTrackDataFactory extends DataFactory {

	String buildBasicQuery(String query);

	String buildPagingQuery(int inPageIndex);

	JSONArray getPagingQueryResponse() throws HygieiaException;

	JSONArray getQueryResponse() throws HygieiaException;

	String getBasicQuery();

	String getPagingQuery();

	int getPageIndex();

}
