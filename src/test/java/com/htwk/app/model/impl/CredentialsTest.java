package com.htwk.app.model.impl;

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
public class CredentialsTest {

	private static final Logger logger = LoggerFactory.getLogger(CredentialsTest.class);
	
	private final String USERNAME = "Test";
	
	private final String PASSWORD = "12345";
	
	private Credentials c1;
	
	private Credentials c2;
	
	@Before
	public void init()
	{
		c1 = new Credentials();
		c2 = new Credentials(USERNAME, PASSWORD);
	}
	
	@Test
	public void testUsername()
	{
		c1.setUsername(USERNAME);
		
		Assert.assertEquals(USERNAME, c1.getUsername());
		Assert.assertEquals(USERNAME, c2.getUsername());
	}
	
	@Test
	public void testPassword()
	{
		c1.setPassword(PASSWORD);
		
		Assert.assertEquals(PASSWORD, c1.getPassword());
		Assert.assertEquals(PASSWORD, c2.getPassword());
	}
}
