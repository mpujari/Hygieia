package com.capitalone.dashboard.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "feature")
public class FeatureSettings {

	private String youtrackBaseUrl;
	private String login;
	private String password;
	private String cron;
	private int pageSize;
	private String deltaStartDate;
	private String deltaCollectorItemStartDate;
	private String masterStartDate;
	private String queryFolder;
	private String storyQuery;
	private String epicQuery;
	private String projectQuery;
	private String memberQuery;
	private String sprintQuery;
	private String teamQuery;
	private String trendingQuery;
	private int sprintDays;
	private int sprintEndPrior;
	private int scheduledPriorMin;

	public String getYoutrackBaseUrl() {
		return youtrackBaseUrl;
	}

	public void setYoutrackBaseUrl(String youtrackBaseUrl) {
		this.youtrackBaseUrl = youtrackBaseUrl;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getDeltaStartDate() {
		return deltaStartDate;
	}

	public void setDeltaStartDate(String deltaStartDate) {
		this.deltaStartDate = deltaStartDate;
	}

	public void setDeltaCollectorItemStartDate(String deltaCollectorItemStartDate) {
		this.deltaCollectorItemStartDate = deltaCollectorItemStartDate;
	}

	public String getDeltaCollectorItemStartDate() {
		return this.deltaCollectorItemStartDate;
	}

	public String getMasterStartDate() {
		return masterStartDate;
	}

	public void setMasterStartDate(String masterStartDate) {
		this.masterStartDate = masterStartDate;
	}

	public String getQueryFolder() {
		return queryFolder;
	}

	public void setQueryFolder(String queryFolder) {
		this.queryFolder = queryFolder;
	}

	public String getStoryQuery() {
		return storyQuery;
	}

	public void setStoryQuery(String storyQuery) {
		this.storyQuery = storyQuery;
	}

	public String getEpicQuery() {
		return epicQuery;
	}

	public void setEpicQuery(String epicQuery) {
		this.epicQuery = epicQuery;
	}

	public String getProjectQuery() {
		return projectQuery;
	}

	public void setProjectQuery(String projectQuery) {
		this.projectQuery = projectQuery;
	}

	public String getMemberQuery() {
		return memberQuery;
	}

	public void setMemberQuery(String memberQuery) {
		this.memberQuery = memberQuery;
	}

	public String getSprintQuery() {
		return sprintQuery;
	}

	public void setSprintQuery(String sprintQuery) {
		this.sprintQuery = sprintQuery;
	}

	public String getTeamQuery() {
		return teamQuery;
	}

	public void setTeamQuery(String teamQuery) {
		this.teamQuery = teamQuery;
	}

	public String getTrendingQuery() {
		return trendingQuery;
	}

	public void setTrendingQuery(String trendingQuery) {
		this.trendingQuery = trendingQuery;
	}

	public int getSprintDays() {
		return sprintDays;
	}

	public void setSprintDays(int sprintDays) {
		this.sprintDays = sprintDays;
	}

	public int getSprintEndPrior() {
		return sprintEndPrior;
	}

	public void setSprintEndPrior(int sprintEndPrior) {
		this.sprintEndPrior = sprintEndPrior;
	}

	public int getScheduledPriorMin() {
		return scheduledPriorMin;
	}

	public void setScheduledPriorMin(int scheduledPriorMin) {
		this.scheduledPriorMin = scheduledPriorMin;
	}

}
