/**
 * 
 */
package com.capitalone.dashboard.rest.client;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import com.capitalone.dashboard.misc.HygieiaException;

public class YouTrackRestApiImpl implements YouTrackRestApi {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final String baseUrl;

	private final RestOperations restOperations;

	private List<String> cookies;

	public YouTrackRestApiImpl(String baseUrl, RestOperations restOperations) {
		this.restOperations = restOperations;
		this.baseUrl = baseUrl;
	}

	@Override
	public void login(String baseUrl, String userName, String password) throws HygieiaException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl).path("rest/user/login")
				.queryParam("login", userName).queryParam("password", password);
		try {
			ResponseEntity<String> response = restOperations.exchange(builder.build().encode().toUri(), HttpMethod.POST,
					entity, String.class);

			logger.debug("Login success with base url %s", baseUrl);
			HttpHeaders resHeaders = response.getHeaders();
			List<String> cookieeList = resHeaders.get("Set-Cookie");
			if (cookieeList != null && !cookieeList.isEmpty()) {
				cookies = cookieeList;
			}
		} catch (RestClientException e) {
			throw new HygieiaException(e);
		}
	}

	@Override
	public JSONArray getTeams() throws HygieiaException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		if (cookies != null) {
			for (String cookie : cookies) {
				headers.set(HttpHeaders.COOKIE, cookie);
			}
		}
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl).path("rest/project/all");
		try {
			ResponseEntity<String> response = restOperations.exchange(builder.build().encode().toUri(), HttpMethod.GET,
					entity, String.class);
			String jsonStr = response.getBody();
			JSONParser jsonParser = new JSONParser();
			return (JSONArray) jsonParser.parse(jsonStr);
		} catch (RestClientException | ParseException e) {
			throw new HygieiaException(e);
		}
	}

}
