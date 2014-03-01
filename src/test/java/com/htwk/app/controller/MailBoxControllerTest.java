package com.htwk.app.controller;

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
public class MailBoxControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MailBoxControllerTest.class);

	private MailBoxController ctrl;
	
	@Before
	public void init()
	{
		ctrl = new MailBoxController();
	}
	
	@Test
	public void testDefaultReaction()
	{
		Assert.assertTrue(""==ctrl.home());
		Assert.assertTrue("redirect:/mailbox"==ctrl.redirectHome());
		
		try {
			ctrl.getMails(enryptedCredentials, salt);
			ctrl.getMails(enryptedCredentials, salt, offset);
		} catch (Exception e) {
			Assert.fail();
		}
	}
}
