package com.htwk.app.utils;

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
public class MailUtilsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MailUtilsTest.class);
	
	@Before
	public void init()
	{
	}
	
	@Test
	public void testDefault()
	{
		String mail = "javascript:linkTo_UnCryptMailto('jxfiql7glbod+yibvjbeiXeqth:ibfmwfd+ab');";
		
		try {
			mail = MailUtils.decryptMail(mail);
			mail = mail.substring(40, mail.length() - 3);
		} catch (Exception e) {
			Assert.fail();
			logger.info("Unexpected Exception occurred!");
		}
		
		Assert.assertEquals("joerg.bleymehl@htwk-leipzig.de", mail);
	}
}
