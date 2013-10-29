package com.htwk.app.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.naming.directory.InvalidAttributesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.xmlpull.v1.XmlPullParserException;

import com.htwk.app.model.impl.Day;
import com.htwk.app.model.timetable.Faculty;
import com.htwk.app.model.timetable.Subject;
import com.htwk.app.repository.TimetableRepository;

@Controller
@RequestMapping(value = "/timetable")
public class TimetableController {

	private static final Logger logger = LoggerFactory.getLogger(TimetableController.class);

	@Autowired
	private TimetableRepository repo;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/timetable/";
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	String home() throws InvalidAttributesException, IOException {
		return "";
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/{semester}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	List<Faculty> getSemGroups(@PathVariable(value = "semester") String semester) throws InvalidAttributesException,
			IOException, URISyntaxException {
		return repo.getSemGroups(semester);
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/{semester}/{fak}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	Faculty getSemGroupByFaculty(@PathVariable(value = "semester") String semester,
			@PathVariable(value = "fak") String fak) throws InvalidAttributesException, IOException, URISyntaxException {
		return repo.getSemGroups(semester, fak);
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/{semester}/{fak}/{semgroup}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	List<Day<Subject>> getTimetable(@PathVariable(value = "semester") String semester,
			@PathVariable(value = "fak") String fak, @PathVariable(value = "semgroup") String semgroup)
			throws InvalidAttributesException, IOException, URISyntaxException, XmlPullParserException {
		return repo.getTimetable(semester, semgroup);
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/{semester}/{fak}/{semgroup}/{kw}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	List<Day<Subject>> getTimetable(@PathVariable(value = "semester") String semester,
			@PathVariable(value = "fak") String fak, @PathVariable(value = "semgroup") String semgroup,
			@PathVariable(value = "kw") String kw) throws InvalidAttributesException, IOException, URISyntaxException, RestClientException, XmlPullParserException {
		return repo.getTimetable(semester, semgroup, kw);
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/{semester}/{fak}/{semgroup}/{kw}/{day}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	Day<Subject> getTimetable(@PathVariable(value = "semester") String semester,
			@PathVariable(value = "fak") String fak, @PathVariable(value = "semgroup") String semgroup,
			@PathVariable(value = "kw") String kw, @PathVariable(value = "day") int day)
			throws InvalidAttributesException, IOException, URISyntaxException, RestClientException, XmlPullParserException {
		return repo.getTimetable(semester, semgroup, kw, day);
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/cal", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	Map<String, String> getCalendar() throws InvalidAttributesException, IOException, URISyntaxException,
			XmlPullParserException {
		return repo.getCalendar();
	}

}
