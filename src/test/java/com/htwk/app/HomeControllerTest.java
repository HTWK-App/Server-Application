package com.htwk.app;

import java.util.Locale;

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
import org.springframework.ui.Model;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class HomeControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(HomeControllerTest.class);
	
	private final String DEFAULT_RESULT = "home";
	
	private HomeController hc;
	
	@Before
	public void init() {
		hc = new HomeController();
	}
	
	@Test
	public void testDefaultReaction()
	{
		// TODO Mocking Locale & Model
		Locale locale;
		Model model;
		
		Assert.assertEquals(DEFAULT_RESULT, hc.home(locale, model));
	}
}
