package com.capitalone.dashboard.rest.client;

public final class YouTrackRestApiConstant {

	public static final String USER_LOGIN = "rest/user/login";

	public static final String PROJECT_ALL = "rest/project/all";

	public static final String JIRA_ALL = "rest/issue";

	public static final String AGILE_BOARD_ALL = "rest/admin/agile"; // rest/admin/agile

	public static final String AGILE_INFO_ALL = "rest/agile/agiles";

	// {1} is agileboard name to be given
	public static final String AGILE_SPRINTS = "/rest/agile/{1}/sprints";

	public static final String ISSUES_BY_PROJECT = "/rest/issue/byproject/{project}";

	private YouTrackRestApiConstant() {
	}

}
