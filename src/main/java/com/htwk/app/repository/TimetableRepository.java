package com.htwk.app.repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.xmlpull.v1.XmlPullParserException;

import com.htwk.app.model.impl.Day;
import com.htwk.app.model.timetable.Faculty;
import com.htwk.app.model.timetable.Subject;
import com.htwk.app.repository.helper.impl.TimetableConverter;

@Repository
public class TimetableRepository {

	private static final Logger logger = LoggerFactory.getLogger(TimetableRepository.class);

	RestTemplate restTemplate = null;
	ResponseEntity<String> response = null;
	HttpHeaders headers = null;
	URI uri = null;
	TimetableConverter conv = null;

	@Value("${timetable.url}")
	private String timetableUrl;

	@Value("${timetable.report}")
	private String timetableReport;

	@Value("${timetable.cal}")
	private String timetableCal;

	@Value("${timetable.semgr}")
	private String timetableSemGroup;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.add("Content-Type", "text/xml;charset=UTF-8");
		conv = new TimetableConverter();
	}

	
	public List<Faculty> getSemGroups(String semester) throws InvalidAttributesException, IOException,
			URISyntaxException {
		timetableSemGroup = MessageFormat.format(timetableSemGroup, semester);

		uri = new URI(timetableUrl + timetableSemGroup);
		logger.debug("getData from URI: " + uri.toString());
		response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

		if (response != null) {
			return conv.getSemGroup(response.getBody());
			// return response.getBody();
		}

		return null;
	}

	public Faculty getSemGroups(String semester, String fak) throws InvalidAttributesException, IOException,
			URISyntaxException {
		for (Faculty fac : getSemGroups(semester)) {
			if (fac.getId().equalsIgnoreCase(fak)) {
				return fac;
			}
		}
		return null;
	}

	public Map<String, String> getCalendar() throws InvalidAttributesException, URISyntaxException,
			XmlPullParserException, IOException {
		uri = new URI(timetableUrl + timetableCal);
		logger.debug("getData from URI: " + uri.toString());
		response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

		if (response != null) {
			return conv.getCal(response.getBody());
			// return response.getBody();
		}
		return null;
	}

	public List<Day<Subject>> getTimetable(String semester, String semgroup) throws InvalidAttributesException,
			URISyntaxException, XmlPullParserException, IOException {
		String kw = getCalendar().get("all");
		return getTimetable(semester, semgroup, kw);
	}

	public List<Day<Subject>> getTimetable(String semester, String semgroup, String kw)
			throws InvalidAttributesException, URISyntaxException, RestClientException, XmlPullParserException,
			IOException {
		if (getCalendar().containsKey(kw)) {

			timetableReport = MessageFormat.format(timetableReport, semester, semgroup, kw, "");
			uri = new URI(timetableUrl + timetableReport);
			logger.debug("getData from URI: " + uri.toString());
			response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

			if (response != null) {
				return conv.getTimetable(response.getBody());

			}
		}
		return null;
	}

	public Day<Subject> getTimetable(String semester, String semgroup, String kw, int day)
			throws InvalidAttributesException, URISyntaxException, RestClientException, XmlPullParserException,
			IOException {
		day = day - 1;
		List<Day<Subject>> days = getTimetable(semester, semgroup, kw);
		if (days!= null && days.size() > 0) {
			return days.get(day);
		}
		return null;
	}
}
