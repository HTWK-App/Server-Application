package com.htwk.app.repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.naming.directory.InvalidAttributesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.xmlpull.v1.XmlPullParserException;

import com.htwk.app.model.impl.Day;
import com.htwk.app.model.mensa.Meal;
import com.htwk.app.repository.helper.impl.MensaConverter;

@Repository
public class MensaRepository {

	private static final Logger logger = LoggerFactory.getLogger(MensaRepository.class);

	RestTemplate restTemplate = null;
	ResponseEntity<String> response = null;
	HttpHeaders headers = null;
	URI uri = null;
	MensaConverter conv = null;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.add("Content-Type", "text/xml;charset=UTF-8");
		headers.set("Authorization", "Basic " + mensaAuth);
		conv = new MensaConverter();
	}

	/**
	 * not publicated
	 */
	@Value("${mensa.auth}")
	private String mensaAuth;

	@Value("${mensa.url}")
	private String mensaUrl;

	public Day<Meal> get(String location, String date) throws InvalidAttributesException, IOException,
			URISyntaxException, XmlPullParserException {
		uri = new URI(mensaUrl + "?location=" + location + "&date=" + date);
		logger.debug("getData from URI: " + uri.toString());
		response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

		if (response != null) {
			return conv.getObject(response.getBody());
		}

		return null;
	}

	public Day<Meal> get(String location) throws InvalidAttributesException, IOException, URISyntaxException, XmlPullParserException {
		SimpleDateFormat formatdate = new SimpleDateFormat("yyyyMMdd");
		return get(location, formatdate.format(new Date()));
	}
}
