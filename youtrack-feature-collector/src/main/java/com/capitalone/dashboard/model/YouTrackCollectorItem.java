package com.capitalone.dashboard.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class YouTrackCollectorItem extends CollectorItem {

	private static final String REPO_URL = "repoUrl";

	private static final String USER_ID = "userID";

	private static final String PASSWORD = "password";

	public String getUserId() {
		return (String) getOptions().get(USER_ID);
	}

	public void setUserId(String userId) {
		getOptions().put(USER_ID, userId);
	}

	public String getPassword() {
		return (String) getOptions().get(PASSWORD);
	}

	public void setPassword(String password) {
		getOptions().put(PASSWORD, password);
	}

	public String getRepoUrl() {
		return (String) getOptions().get(REPO_URL);
	}

	public void setRepoUrl(String instanceUrl) {
		getOptions().put(REPO_URL, instanceUrl);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof YouTrackCollectorItem)) {
			return false;
		}
		YouTrackCollectorItem that = (YouTrackCollectorItem) obj;
		return new EqualsBuilder().append(this.getRepoUrl(), that.getRepoUrl()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getRepoUrl()).toHashCode();
	}

}
