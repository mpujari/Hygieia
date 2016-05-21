package com.capitalone.dashboard.json;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YouTrackIssue {

	private String id;
	private String entityId;
	private String jiraId;

	@JsonProperty("field")
	private List<FieldValue> fieldValues = Lists.newArrayList();

	private Map<String, FieldValue> fieldValueMap;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getJiraId() {
		return jiraId;
	}

	public void setJiraId(String jiraId) {
		this.jiraId = jiraId;
	}

	public List<FieldValue> getFieldValues() {
		return fieldValues;
	}

	public void setFieldMap(List<FieldValue> fieldMap) {
		this.fieldValues = fieldMap;
		initFieldValueMap();
	}

	private void initFieldValueMap() {
		this.fieldValueMap = Maps.newHashMap();
		if (this.fieldValues == null) {
			return;
		}
		for (FieldValue fieldValue : this.fieldValues) {
			fieldValueMap.put(fieldValue.name, fieldValue);
		}
	}

	public Map<String, FieldValue> getFieldValueMap() {
		if (fieldValueMap == null) {
			initFieldValueMap();
		}
		return fieldValueMap;
	}

	public FieldValue getFieldValue(String fieldName) {
		return getFieldValueMap().get(fieldName);
	}

	public Object getFieldValueObj(String fieldName) {
		FieldValue fieldValue = getFieldValue(fieldName);
		return fieldValue != null ? fieldValue.getValue() : null;
	}

	public String getFieldValueStr(String fieldName) {
		return (String) getFieldValueObj(fieldName);
	}

	public Object getFieldValueFromArray(String fieldName, int index) {
		FieldValue state = this.getFieldValue(fieldName);
		if (state != null) {
			List<?> states = (List<?>) state.getValue();
			return !states.isEmpty() ? states.get(index) : null;
		}
		return null;
	}
}