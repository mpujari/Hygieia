/**
 * 
 */
package com.capitalone.dashboard.rest.client;

import org.json.simple.JSONArray;

import com.capitalone.dashboard.misc.HygieiaException;

public interface YouTrackRestApi {

	void login(String baseUrl, String userName, String password) throws HygieiaException;

	JSONArray getTeams() throws HygieiaException;
	
}
