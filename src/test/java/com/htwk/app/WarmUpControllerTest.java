package com.htwk.app;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class WarmUpControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(WarmUpControllerTest.class);
	
	private final String DEFAULT_RESULT = "home";

	private WarmUpController wc;
	
	@Before
	public void init() {
		wc = new WarmUpController();
	}
	
	@Test
	public void testDefaultReaction()
	{
		// TODO Mocking Request
		HttpRequest req;   
		
		try {
			Assert.assertEquals(DEFAULT_RESULT, wc.home(req));
		} catch (Exception e) {
			logger.info("Unexpected Exception was thrown!");
			Assert.fail();
		}
	}
}
