package com.capitalone.dashboard.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// /rest/admin/agile
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgileBoard {

	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("query")
	private String query;
	@JsonProperty("completeBacklogHierarhy")
	private boolean completeBacklogHierarhy;
	@JsonProperty("sprints")
	private List<Sprint> sprints;
	@JsonProperty("projects")
	private List<Project> projects;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public boolean isCompleteBacklogHierarhy() {
		return completeBacklogHierarhy;
	}

	public void setCompleteBacklogHierarhy(boolean completeBacklogHierarhy) {
		this.completeBacklogHierarhy = completeBacklogHierarhy;
	}

	public List<Sprint> getSprints() {
		return sprints;
	}

	public void setSprints(List<Sprint> sprints) {
		this.sprints = sprints;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Sprint extends IdUrl {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Project extends IdUrl {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class IdUrl {
		@JsonProperty("id")
		private String id;
		@JsonProperty("url")
		private String url;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

}
