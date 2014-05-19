package com.htwk.app.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.htwk.app.utils.PushStatus;

@Service
public class GCMService {

	@Value("${gcm.uri}")
	private String gcmUri;

	@Value("${gcm.apikey}")
	private String gcmApiKey;

	private RestTemplate restTemplate = null;
	private HttpHeaders headers = null;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.add("Content-Type", "application/json;charset=UTF-8");
		headers.add("Authorization", "key=" + gcmApiKey);
	}

	public String test(String regId, PushStatus status) {
		JsonObject payload = new JsonObject();
		JsonObject payloadData = new JsonObject();

		switch (status) {
		case NEW_MAILS: {
			payloadData.addProperty("title", "New Mails");
			payloadData.addProperty("message", "please check your mailbox");
			payloadData.addProperty("status", status.status());
			payloadData.addProperty("site", "mail");
			break;
		}
		case NEW_NEWS: {
			payloadData.addProperty("title", "New News");
			payloadData.addProperty("message", "please check the current news");
			payloadData.addProperty("status", status.status());
			payloadData.addProperty("site", "news");
			break;
		}
		default:
			break;
		}
		payload.add("data", payloadData);
		JsonArray regIds = new JsonArray();
		regIds.add(new JsonPrimitive(regId));
		payload.add("registration_ids", regIds);

		HttpEntity<String> request = new HttpEntity<String>(payload.toString(), headers);
		ResponseEntity<String> response = restTemplate.exchange(gcmUri, HttpMethod.POST, request, String.class);
		if (response.getBody() != null) {
			return response.getBody();
		}
		return null;
	}
}
