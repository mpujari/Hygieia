/**
 * 
 */
package com.capitalone.dashboard.rest.client;

import static com.capitalone.dashboard.rest.client.YouTrackRestApiConstant.AGILE_INFO_ALL;
import static com.capitalone.dashboard.rest.client.YouTrackRestApiConstant.AGILE_SPRINTS;
import static com.capitalone.dashboard.rest.client.YouTrackRestApiConstant.JIRA_ALL;
import static com.capitalone.dashboard.rest.client.YouTrackRestApiConstant.PROJECT_ALL;
import static com.capitalone.dashboard.rest.client.YouTrackRestApiConstant.USER_LOGIN;
import static org.springframework.http.HttpHeaders.COOKIE;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import com.capitalone.dashboard.json.AgileBoardInfo;
import com.capitalone.dashboard.json.Sprint;
import com.capitalone.dashboard.json.YouTrackIssue;
import com.capitalone.dashboard.misc.HygieiaException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

public class YouTrackRestApiImpl implements YouTrackRestApi {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final String baseUrl;

	private final RestOperations restOperations;

	private List<String> cookies;

	public YouTrackRestApiImpl(String baseUrl, RestOperations restOperations) {
		this.baseUrl = baseUrl;
		this.restOperations = restOperations;
	}

	@Override
	public void login(String userName, String password) throws HygieiaException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(APPLICATION_JSON));
		try {
			Map<String, Object[]> params = new HashMap<>();
			params.put("login", new Object[] { userName });
			params.put("password", new Object[] { password });
			ResponseEntity<String> response = callRestApi(USER_LOGIN, POST, params, String.class);
			logger.debug("Login success with base url %s", baseUrl);
			HttpHeaders resHeaders = response.getHeaders();
			cookies = resHeaders.get(SET_COOKIE);
		} catch (RestClientException e) {
			throw new HygieiaException(e);
		}
	}

	@Override
	public JSONArray getTeams() throws HygieiaException {
		try {
			ResponseEntity<String> response = callRestApi(PROJECT_ALL, GET, String.class);
			String jsonStr = response.getBody();
			JSONParser jsonParser = new JSONParser();
			return (JSONArray) jsonParser.parse(jsonStr);
		} catch (RestClientException | ParseException e) {
			throw new HygieiaException(e);
		}
	}

	@Override
	public List<YouTrackIssue> getYouTrackIssues(String filterString, String maxResults) throws HygieiaException {
		try {
			Map<String, Object[]> params = new HashMap<>();
			if (StringUtils.isNotBlank(filterString)) {
				params.put("filter", new Object[] { filterString });
			}
			if (StringUtils.isNotBlank(maxResults)) {
				params.put("max", new Object[] { maxResults });
			}
			ObjectMapper mapper = new ObjectMapper();
			ResponseEntity<String> responseEntity = callRestApi(JIRA_ALL, GET, params, String.class);
			YouTrackIssues issues = (YouTrackIssues) mapper.readValue(responseEntity.getBody().getBytes(),
					YouTrackIssues.class);
			return issues.getIssues();
		} catch (Exception e) {
			throw new HygieiaException(e);
		}
	}

	@Override
	public List<AgileBoardInfo> getAgileInfos() throws HygieiaException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ResponseEntity<String> responseEntity = callRestApi(AGILE_INFO_ALL, GET, String.class);
			return mapper.readValue(responseEntity.getBody().getBytes(), new TypeReference<List<AgileBoardInfo>>() {
			});
		} catch (Exception e) {
			throw new HygieiaException(e);
		}
	}

	@Override
	public List<Sprint> getSprints(String agileName) throws HygieiaException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String restApiUrl = AGILE_SPRINTS.replace("{1}", agileName);
			ResponseEntity<String> responseEntity = callRestApi(restApiUrl, GET, String.class);
			Sprints sprints = (Sprints) mapper.readValue(responseEntity.getBody().getBytes(), Sprints.class);
			return sprints.getSprint();
		} catch (Exception e) {
			throw new HygieiaException(e);
		}
	}

	private <T> ResponseEntity<T> callRestApi(String restPath, HttpMethod httpMethod, Class<T> classType)
			throws HygieiaException {
		return callRestApi(restPath, httpMethod, Maps.newHashMap(), classType);
	}

	private <T> ResponseEntity<T> callRestApi(String restPath, HttpMethod httpMethod, Map<String, Object[]> params,
			Class<T> classType) throws HygieiaException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(APPLICATION_JSON));
		if (cookies != null) {
			for (String cookie : cookies) {
				headers.set(COOKIE, cookie);
			}
		}
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl).path(restPath);
		for (Entry<String, Object[]> entry : params.entrySet()) {
			builder.queryParam(entry.getKey(), entry.getValue());
		}
		try {
			logger.info(builder.build().encode().toUri().toString());
			return restOperations.exchange(builder.build().encode().toUri(), httpMethod, entity, classType);
		} catch (Exception e) {
			throw new HygieiaException(e);
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	static class YouTrackIssues {

		@JsonProperty("issue")
		private List<YouTrackIssue> issues;

		public List<YouTrackIssue> getIssues() {
			return issues;
		}

		public void setIssues(List<YouTrackIssue> issues) {
			this.issues = issues;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	static class Sprints {
		@JsonProperty("sprint")
		private List<Sprint> sprint;

		public List<Sprint> getSprint() {
			return sprint;
		}

		public void setSprint(List<Sprint> sprint) {
			this.sprint = sprint;
		}
	}
}
