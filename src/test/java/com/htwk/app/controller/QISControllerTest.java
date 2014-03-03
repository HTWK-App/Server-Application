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
public class QISControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(QISControllerTest.class);

	private QISController ctrl;
	
	@Before
	public void init()
	{
		ctrl = new QISController();
	}
	
	@Test
	public void testDefaultReaction()
	{
		Assert.assertTrue(ctrl.home()=="");
		Assert.assertTrue(ctrl.redirectHome()=="redirect:/qis");
		
		try {
			ctrl.getGISData(enryptedCredentials, salt);
		} catch (Exception e) {
			Assert.fail();
		}
		
		try {
			ctrl.getGISData(enryptedCredentials, salt);
			Assert.fail();
		} catch (Exception e) {}
	}
}
