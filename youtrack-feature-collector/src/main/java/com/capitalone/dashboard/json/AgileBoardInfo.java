package com.capitalone.dashboard.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// /rest/agile/agiles
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgileBoardInfo {

	@JsonProperty("text")
	private String text;
	
	@JsonProperty("id")
	private String id;

	@JsonProperty("styleClass")
	private String styleClass;
	
	@JsonProperty("empty")
	private boolean empty;

	@JsonProperty("checked")
	private boolean checked;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
