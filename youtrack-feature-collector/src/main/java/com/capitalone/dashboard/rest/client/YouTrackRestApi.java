/**
 * 
 */
package com.capitalone.dashboard.rest.client;

import java.util.List;

import com.capitalone.dashboard.json.AgileBoardInfo;
import com.capitalone.dashboard.json.ProjectInfo;
import com.capitalone.dashboard.json.Sprint;
import com.capitalone.dashboard.json.YouTrackIssue;
import com.capitalone.dashboard.misc.HygieiaException;

public interface YouTrackRestApi {

	void login(String userName, String password) throws HygieiaException;

	List<ProjectInfo> getTeams() throws HygieiaException;

	List<YouTrackIssue> getYouTrackIssues(String filterString, String maxResults) throws HygieiaException;

	// /rest/issue/byproject/{project}?{after}&{max}&{updatedAfter}
	List<YouTrackIssue> getYouTrackIssuesByProject(String projectName, int after, int max, long updatedAfter)
			throws HygieiaException;

	List<AgileBoardInfo> getAgileInfos() throws HygieiaException;

	List<Sprint> getSprints(String agileName) throws HygieiaException;

}
