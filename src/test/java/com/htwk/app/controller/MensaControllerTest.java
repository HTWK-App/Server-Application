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
public class MensaControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MensaControllerTest.class);

	private MensaController ctrl;
	
	@Before
	public void init()
	{
		ctrl = new MensaController();
	}
	
	@Test
	public void testDefaultReaction()
	{
		Assert.assertTrue(1==ctrl.home().size());
		Assert.assertTrue(((Map<String, Object>) ctrl.home().get("location")).size()==11);
		Assert.assertTrue("redirect:/mensa"==ctrl.redirectHome());
		
		try {
			ctrl.getMenuByLocation(116);
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			ctrl.getMenuByLocationAndDate(116, df.format(new Date()));
		} catch (Exception e) {
			Assert.fail();
		}
	}
}
