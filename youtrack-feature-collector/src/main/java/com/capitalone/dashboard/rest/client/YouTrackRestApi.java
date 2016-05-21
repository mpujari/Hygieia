/**
 * 
 */
package com.capitalone.dashboard.rest.client;

import java.util.List;

import org.json.simple.JSONArray;

import com.capitalone.dashboard.json.AgileBoardInfo;
import com.capitalone.dashboard.json.Sprint;
import com.capitalone.dashboard.json.YouTrackIssue;
import com.capitalone.dashboard.misc.HygieiaException;

public interface YouTrackRestApi {

	void login(String userName, String password) throws HygieiaException;

	// TODO change the implementation to return Team object rather then Json
	JSONArray getTeams() throws HygieiaException;

	List<YouTrackIssue> getYouTrackIssues(String filterString, String maxResults) throws HygieiaException;

	List<AgileBoardInfo> getAgileInfos() throws HygieiaException;

	List<Sprint> getSprints(String agileName) throws HygieiaException;

}
