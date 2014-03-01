package com.htwk.app.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
public class NewsControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(NewsControllerTest.class);

	private NewsController ctrl;
	
	@Before
	public void init()
	{
		ctrl = new NewsController();
	}
	
	@Test
	public void testDefaultReaction()
	{
		Assert.assertTrue(ctrl.home().size()>0);
		Assert.assertTrue(ctrl.redirectHome()=="redirect:/news");
		
		try {
			ctrl.home(key, limit, offset);
		} catch (Exception e) {
			Assert.fail();
		}
		
		try {
			ctrl.home(key, limit, offset);
			Assert.fail();
		} catch (Exception e) {}
	}
}
