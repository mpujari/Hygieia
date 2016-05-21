package com.capitalone.dashboard.client;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import com.capitalone.dashboard.util.ClientUtil;

public class BaseClient {

	protected String getJSONDateString(JSONObject obj, String field) {
        return getDateString(getJSONString(obj, field));
    }

    protected String getJSONString(JSONObject obj, String field) {
        return ClientUtil.sanitizeResponse((String) obj.get(field));
    }
    
	protected String getDateString(String dateTimeStr) {
		return StringUtils.isNotBlank(dateTimeStr) ? getDateString(Long.valueOf(dateTimeStr)) : "";
	}

	protected String getDateString(long dateTime) {
		return ClientUtil.toCanonicalDate(dateTime);
	}

	protected String getString(JSONObject obj, String field) {
		return ClientUtil.sanitizeResponse((String) obj.get(field));
	}
}
