package com.htwk.app;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.bind.annotation.ResponseStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class GlobalControllerExceptionHandlerTest {

	private static final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandlerTest.class);
	
	private GlobalControllerExceptionHandler gceh;
	
	private HttpServletRequest req;
	
	@Before
	public void init() {
		
		gceh = new GlobalControllerExceptionHandler();
		// TODO Mocking Request
	}
	
	@Test
	public void testExceptionThrow()
	{
		TestException ex = new TestException();
		
		try {
			gceh.defaultErrorHandler(req, ex);
			Assert.fail();
		} catch (Exception e) {
			logger.info("Expected Exception was thrown.");
		}
	}
	
	@Test
	public void testDefaultReaction()
	{
		Map<String, Object> m = new HashMap<String, Object>();
		Map<String, Object> r = new HashMap<String, Object>();
		Exception ex = new Exception("Test Exception for Testing ^^");
		
		m.put("url", req.getRequestURL());
		m.put("exception", ex.toString());
		m.put("message", ex.getMessage());
		
		try {
			r = gceh.defaultErrorHandler(req, ex);
			
			Assert.assertEquals(r, m);
		} catch (Exception e) {
			logger.info("Unexpected Exception was thrown!");
			Assert.fail();
		}
	}
	
	@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Testing...")
	private class TestException extends Exception {

		private static final long serialVersionUID = 1L;
	}
}
