package com.htwk.app.warmup.worker;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.htwk.app.model.timetable.Course;
import com.htwk.app.model.timetable.Faculty;
import com.htwk.app.repository.TimetableRepository;
import com.htwk.app.warmup.WarmUp;

@Component
@Qualifier("timetableWarmUp")
public class TimetableWarmUp implements WarmUp {

	private static final Logger logger = LoggerFactory.getLogger(TimetableWarmUp.class);

	@Value("${server.protocol}")
	private String serverProtocol;

	@Value("${server.host}")
	private String serverHost;

	@Value("${server.port}")
	private String serverPort;

	private String servletPath;

	@Autowired
	ServletContext context;

	private RestTemplate restTemplate = null;
	private String serverPath = null;

	@Autowired
	private TimetableRepository repo;

	@PostConstruct
	public void init() throws UnknownHostException {
		restTemplate = new RestTemplate();
		servletPath = context.getContextPath().toString();
		serverPath = serverProtocol + "://" + serverHost + ":" + serverPort + servletPath;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void warmUp() throws IOException, ParseException {
		restTemplate.getForEntity(serverPath + "/timetable/cal", String.class);

		String semester = repo.getCalendar().get("semester");
		restTemplate.getForEntity(serverPath + "/timetable/" + semester, String.class);

		for (Faculty fac : repo.getSemGroups(semester)) {
			String facultyUrl = serverPath + "/timetable/" + semester + "/" + fac.getId();
			restTemplate.getForEntity(facultyUrl, String.class);
			for (Course course : fac.getCourses()) {
				for (String semgroup : course.getSemGroups()) {
					String semgroupAddition = "?semgroup=" + semgroup;
					restTemplate.getForEntity(facultyUrl + semgroupAddition, String.class);
					restTemplate.getForEntity(facultyUrl + "/courses" + semgroupAddition, String.class);

				}
			}
		}
		logger.debug("warmed up TimetableRepository");

	}
}
