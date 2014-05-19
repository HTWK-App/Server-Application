package com.htwk.app.repository;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.htwk.app.SpringWebContextTests;

public class TimetableRepositoryTest extends SpringWebContextTests {
	private MockRestServiceServer mockServer;

	@Inject
	private TimetableRepository service;

	private String calendarResponse;

	@Before
	public void setUp() throws IOException {
		File testFile = new File("src/test/resources/timetable/studienjahr.xml");
		calendarResponse = Files.toString(testFile, Charsets.ISO_8859_1);

		mockServer = MockRestServiceServer.createServer(service.getTemplate());
	}

	@Test
	public void testAddReturnCorrectResponse() throws Exception {
		mockServer.expect(requestTo(service.getTimetableUrl() + service.getTimetableCal()))
				.andExpect(method(HttpMethod.GET)).andRespond(withSuccess(calendarResponse, MediaType.APPLICATION_XML)); // (2)
		service.getCalendar();
		mockServer.verify(); // (4)
	}
}
