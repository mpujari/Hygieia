package com.capitalone.dashboard.json;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "feature")
public class FeatureSettings {

	private String youtrackBaseUrl;

	private String login;

	private String password;

	private String cron;

	private String masterStartDate;

	@Value("${useDefault:Sprint}")
	private String youtrackSprintFieldName;

	@Value("${useDefault:Estimation}")
	private String youtrackEstimationFieldName;

	private List<String> youtrackEstimationFieldValues;

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

	public String getMasterStartDate() {
		return masterStartDate;
	}

	public void setMasterStartDate(String masterStartDate) {
		this.masterStartDate = masterStartDate;
	}

	public String getYoutrackSprintFieldName() {
		return youtrackSprintFieldName;
	}

	public void setYoutrackSprintFieldName(String youtrackSprintFieldName) {
		this.youtrackSprintFieldName = youtrackSprintFieldName;
	}

	public String getYoutrackEstimationFieldName() {
		return youtrackEstimationFieldName;
	}

	public void setYoutrackEstimationFieldName(String youtrackEstimationFieldName) {
		this.youtrackEstimationFieldName = youtrackEstimationFieldName;
	}

	public List<String> getYoutrackEstimationFieldValues() {
		return youtrackEstimationFieldValues;
	}

	public void setYoutrackEstimationFieldValues(List<String> youtrackEstimationFieldValues) {
		this.youtrackEstimationFieldValues = youtrackEstimationFieldValues;
	}

}
