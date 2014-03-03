package com.htwk.app;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class ErrorControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ErrorControllerTest.class);
	
	private ErrorController ec;
	
	private HttpServletRequest req;
	
	@Before
	public void init() {
		 ec = new ErrorController();
		 // TODO Mocking Request
	}
	
	@Test
	public void testExceptionThrow()
	{
		try {
			ec.error404(req, new Exception());
			Assert.fail();
		} catch (Exception e) {
			logger.info("Expected Exception for 404 was thrown.");
		}
		
		try {
			ec.error500(req, new Exception());
			Assert.fail();
		} catch (Exception e) {
			logger.info("Expected Exception for 500 was thrown.");
		}
	}
}
