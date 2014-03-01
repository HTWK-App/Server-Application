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
public class UrlUtilsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(UrlUtilsTest.class);
	
	@Before
	public void init()
	{
	}
	
	@Test
	public void testDefault()
	{
		String url = "http://www.htwk-leipzig.de/de/studierende/";
		
		try {
			Assert.assertEquals(url, UrlUtils.getHtwkUrl(url));
			Assert.assertEquals(url, UrlUtils.getHtwkUrl("de/studierende/"));
			Assert.assertNotEquals(url, UrlUtils.getHtwkUrl("/de/studierende/"));
		} catch (Exception e) {
			Assert.fail();
			logger.info("Unexpected Exception occurred!");
		}
	}
}
