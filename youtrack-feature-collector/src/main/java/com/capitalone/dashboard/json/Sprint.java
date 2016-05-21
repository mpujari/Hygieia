package com.capitalone.dashboard.json;

import com.capitalone.dashboard.util.ClientUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sprint {

	@JsonProperty("name")
	private String name;

	@JsonProperty("string")
	private String start;

	private String startDate;

	@JsonProperty("finish")
	private String finish;

	private String finishDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
		setStartDate(start);
	}

	public String getStartDate() {
		return startDate;
	}

	private void setStartDate(String start) {
		this.startDate = ClientUtil.sprintDateCanonicalDate(start);
	}

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
		setFinishDate(finish);
	}

	public String getFinishDate() {
		return finishDate;
	}

	private void setFinishDate(String finish) {
		this.finishDate = ClientUtil.sprintDateCanonicalDate(finish);
	}

}
