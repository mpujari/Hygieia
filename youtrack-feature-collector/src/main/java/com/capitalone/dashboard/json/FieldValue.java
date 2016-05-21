package com.capitalone.dashboard.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldValue {
	String name;

	@JsonProperty("value")
	private Object value;

	@JsonProperty("valueId")
	private Object valueId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValueId() {
		return valueId;
	}

	public void setValueId(Object valueId) {
		this.valueId = valueId;
	}

}