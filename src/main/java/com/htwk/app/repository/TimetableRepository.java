package com.htwk.app.repository;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.xmlpull.v1.XmlPullParserException;

import com.google.common.cache.Cache;
import com.htwk.app.model.impl.Day;
import com.htwk.app.model.timetable.Faculty;
import com.htwk.app.model.timetable.Subject;
import com.htwk.app.repository.helper.impl.TimetableConverter;

@Repository
public class TimetableRepository {

	private static final Logger logger = LoggerFactory.getLogger(TimetableRepository.class);

	private RestTemplate restTemplate = null;
	private ResponseEntity<String> response = null;
	private HttpHeaders headers = null;
	private TimetableConverter conv = null;
	private Cache<String, Object> cache;

	@Value("${timetable.url}")
	private String timetableUrl;

	@Value("${timetable.report}")
	private String timetableReport;

	@Value("${timetable.cal}")
	private String timetableCal;

	@Value("${timetable.semgr}")
	private String timetableSemGroup;

	@Value("${timetable.profs}")
	private String timetableProfs;

	@Autowired
	private CacheManager cacheManager;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.add("Content-Type", "text/xml;charset=UTF-8");
		conv = new TimetableConverter();
		cache = (Cache<String, Object>) cacheManager.getCache("timeCache").getNativeCache();
	}

	public List<Faculty> getSemGroups(String semester) throws IOException {
		Object semGroups = cache.getIfPresent(semester);
		if (semGroups != null) {
			return conv.getSemGroup(semGroups.toString());
		}
		timetableSemGroup = MessageFormat.format(timetableSemGroup, semester);

		response = restTemplate.exchange(timetableUrl + timetableSemGroup, HttpMethod.GET, new HttpEntity<Object>(
				headers), String.class);
		if (response.hasBody()) {
			cache.put(semester, response.getBody().toString());
			return conv.getSemGroup(response.getBody());
		}

		return null;
	}

	public Faculty getSemGroups(String semester, String fak) throws IOException {
		for (Faculty fac : getSemGroups(semester)) {
			if (fac.getId().equalsIgnoreCase(fak)) {
				return fac;
			}
		}
		return null;
	}

	public Map<String, String> getCalendar() throws XmlPullParserException, IOException {
		Object cal = cache.getIfPresent("cal");
		if (cal != null) {
			return conv.getCal(cal.toString());
		}

		String uri = timetableUrl + timetableCal;
		logger.debug("getData from URI: " + uri);
		response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

		if (response != null) {
			cache.put("cal", response.getBody().toString());
			return conv.getCal(response.getBody());
		}
		return null;
	}

	public List<Day<Subject>> getTimetable(String semester, String semgroup) throws XmlPullParserException, IOException {
		String kw = getCalendar().get("all");
		if (getCalendar().containsKey(kw)) {

			String uri = timetableUrl + MessageFormat.format(timetableReport, semester, semgroup, kw, "");
			String profsUri = timetableUrl + MessageFormat.format(timetableProfs, semester);
			logger.debug("getData from URI: " + uri + profsUri);
			response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);
			ResponseEntity<String> profsContent = restTemplate.exchange(profsUri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);
			if (response.hasBody() && profsContent.hasBody()) {
				return conv.getTimetable(response.getBody(), profsContent.getBody());
			}
		}
		return null;
	}

	public List<Day<Subject>> getTimetable(String semester, String semgroup, String[] suid) throws RestClientException,
			XmlPullParserException, IOException {
		if (suid == null || suid.length == 0) {
			return getTimetable(semester, semgroup);
		}
		List<Day<Subject>> timetable = getTimetable(semester, semgroup);
		for (Iterator<Day<Subject>> day = timetable.iterator(); day.hasNext();) {
			List<Subject> subjList = day.next().getDayContent();
			for (Iterator<Subject> subj = subjList.iterator(); subj.hasNext();) {
				if (!ArrayUtils.contains(suid, subj.next().getSuid())) {
					subj.remove();
				}
			}
		}
		return timetable;

	}

	public List<Day<Subject>> getTimetable(String semester, String semgroup, String kw) throws RestClientException,
			XmlPullParserException, IOException {

		List<Day<Subject>> timetable = getTimetable(semester, semgroup);
		for (Iterator<Day<Subject>> day = timetable.iterator(); day.hasNext();) {
			List<Subject> subjList = day.next().getDayContent();
			for (Iterator<Subject> subj = subjList.iterator(); subj.hasNext();) {
				if (!ArrayUtils.contains(subj.next().getKw(), kw)) {
					subj.remove();
				}
			}
		}
		return timetable;

	}

	public List<Day<Subject>> getTimetable(String semester, String semgroup, String kw, String[] suid)
			throws RestClientException, XmlPullParserException, IOException {
		if (suid == null || suid.length == 0) {
			return getTimetable(semester, semgroup, kw);
		}
		List<Day<Subject>> timetable = getTimetable(semester, semgroup, kw);
		for (Iterator<Day<Subject>> day = timetable.iterator(); day.hasNext();) {
			List<Subject> subjList = day.next().getDayContent();
			for (Iterator<Subject> subj = subjList.iterator(); subj.hasNext();) {
				if (!ArrayUtils.contains(suid, subj.next().getSuid())) {
					subj.remove();
				}
			}
		}
		return timetable;

	}

	public Day<Subject> getTimetable(String semester, String semgroup, String kw, int day) throws RestClientException,
			XmlPullParserException, IOException {
		day = day - 1;
		List<Day<Subject>> days = getTimetable(semester, semgroup, kw);
		if (days != null && days.size() > 0) {
			return days.get(day);
		}
		return null;
	}

	public Map<String, String> getCourse(String semester, String semgroup) throws XmlPullParserException, IOException {
		Map<String, String> response = new TreeMap<String, String>();
		for (Day<Subject> day : getTimetable(semester, semgroup)) {
			for (Subject subj : day.getDayContent()) {
				response.put(subj.getSuid(), subj.getDescription());
			}
		}
		return response;
	}

	public Subject getCourse(String semester, String semgroup, String id) throws XmlPullParserException, IOException {
		for (Day<Subject> day : getTimetable(semester, semgroup)) {
			for (Subject subj : day.getDayContent()) {
				if (subj.getSuid().equals(id)) {
					return subj;
				}
			}
		}
		return null;
	}
}
