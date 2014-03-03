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
public class UtilsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(UtilsTest.class);
	
	@Before
	public void init()
	{
	}
	
	@Test
	public void testDefault()
	{		
		try {
			Assert.assertEquals("1,0 KiB", Utils.toReadable(1024, false));
			Assert.assertEquals("1,0 kB", Utils.toReadable(1024, true));
			Assert.assertEquals("512 B", Utils.toReadable(512, false));
			Assert.assertEquals("0 B", Utils.toReadable(-3, false));
			Assert.assertEquals("1,0 MiB", Utils.toReadable(1048576, false));
		} catch (Exception e) {
			Assert.fail();
			logger.info("Unexpected Exception occurred!");
		}
	}
}
