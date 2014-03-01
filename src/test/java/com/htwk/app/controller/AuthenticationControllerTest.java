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
import org.springframework.test.util.ReflectionTestUtils;

import com.google.gson.Gson;
import com.htwk.app.model.impl.Credentials;
import com.htwk.app.model.impl.EncryptedCredentials;
import com.htwk.app.service.AuthenticationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class AuthenticationControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationControllerTest.class);
	
	private final String SALT = "42dcc1d36833cf627deb6fed5839023a0835cc5c5a31cd205adbe082939e93e5";
	
	private final String SECRET = "8a30482cb3be8b58";

	private AuthenticationController ctrl;
	
	private AuthenticationService service;
	
	@Before
	public void init()
	{
		ctrl = new AuthenticationController();
		
		AuthenticationService dummy = new AuthenticationService();
		ReflectionTestUtils.setField(dummy, "salt", SALT);
		ReflectionTestUtils.setField(dummy, "secret", SECRET);
		dummy.init();
		ReflectionTestUtils.setField(ctrl, "service", dummy);
		
		service = new AuthenticationService();
		ReflectionTestUtils.setField(service, "salt", SALT);
		ReflectionTestUtils.setField(service, "secret", SECRET);
		service.init();
	}
	
	@Test
	public void testCrypting()
	{
		String username = "test@htwk-leipzig.de";
		String password = "4rCa2oad38x";
		
		Credentials c = new Credentials(username, password);
		EncryptedCredentials r = new EncryptedCredentials();
		
		r = ctrl.encryptCredentials(username, password);
		System.out.println(new Gson().toJson(r));
		c = ctrl.decryptCredentials(r.getEncryptedCredentials(), r.getSalt());
		
		Assert.assertEquals(username, c.getUsername());
		Assert.assertEquals(password, c.getPassword());
	}
}
