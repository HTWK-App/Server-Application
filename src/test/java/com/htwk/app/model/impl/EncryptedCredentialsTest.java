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
public class EncryptedCredentialsTest {

	private static final Logger logger = LoggerFactory.getLogger(EncryptedCredentialsTest.class);
	
	private final String CREDS = "3462vfgtq32qdwas";
	private final String SALT = "2wefw35d5a42ds2rt";
	
	private EncryptedCredentials ec1;
	private EncryptedCredentials ec2;
	
	@Before
	public void init()
	{
		ec1 = new EncryptedCredentials();
		ec2 = new EncryptedCredentials(CREDS, SALT);
	}
	
	@Test
	public void testEncryptedCredentials()
	{
		ec1.setEncryptedCredentials(CREDS);
		
		Assert.assertEquals(ec1.getEncryptedCredentials(), CREDS);
		Assert.assertEquals(ec2.getEncryptedCredentials(), CREDS);
	}
	
	@Test
	public void testSalt()
	{
		ec1.setSalt(SALT);
		
		Assert.assertEquals(ec1.getSalt(), SALT);
		Assert.assertEquals(ec2.getSalt(), SALT);
	}
}
