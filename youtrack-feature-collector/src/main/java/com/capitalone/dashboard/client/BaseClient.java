package com.capitalone.dashboard.client;

import org.json.simple.JSONObject;

import com.capitalone.dashboard.util.ClientUtil;

public class BaseClient {

	protected String getJSONDateString(JSONObject obj, String field) {
        return ClientUtil.toCanonicalDate(getJSONString(obj, field));
    }

    protected String getJSONString(JSONObject obj, String field) {
        return ClientUtil.sanitizeResponse((String) obj.get(field));
    }
}
