package com.htwk.app.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GCMService {

	@Value("${gcm.uri}")
	private String gcmUri;

	@Value("${gcm.id}")
	private String gcmId;

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

	public String test() {
		String payload = "{ \"data\": {"
				+ " \"score\": \"5x1\","
				+ " \"time\": \"15:10\" "
				+ "},\"registration_ids\": [\"APA91bHlj6r7kZGxhlKh9CzAJNc62sKG8urnVwrCsWJeqWF70St18UkDdrGAMVSBrev45T8oJdV2_voNH5mYCWtJy_Y5NHv1TNtVibSOP_uiiOU3ol5FxpC4rL_EUL1zlP5QYge-Fo5R9yS1mLjOS7Hfiz2ccam_pw\"]}";
		HttpEntity<String> request = new HttpEntity<String>(payload, headers);
		ResponseEntity<String> response = restTemplate.exchange(gcmUri, HttpMethod.POST, request, String.class);
		if (response.getBody() != null) {
			return response.getBody();
		}
		return null;
	}
}
