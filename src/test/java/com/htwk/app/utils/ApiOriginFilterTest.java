package com.htwk.app.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class ApiOriginFilterTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiOriginFilterTest.class);

	private ApiOriginFilter util;
	
	@Before
	public void init()
	{
		util = new ApiOriginFilter();
	}
	
	@Test
	public void testDefault()
	{
		MockHttpServletRequest req = new MockHttpServletRequest();
		MockHttpServletResponse res = new MockHttpServletResponse();
		MockFilterChain chain = new MockFilterChain();
		
		try {
			util.init(null);
			util.doFilter(req, res, chain);
			util.destroy();
		} catch (Exception e) {
			Assert.fail();
			logger.info("Unexpected Exception occurred!");
		}
		
		Assert.assertFalse("*"==req.getHeader("Access-Control-Allow-Origin"));
		Assert.assertFalse("GET, POST, DELETE, PUT"==req.getHeader("Access-Control-Allow-Methods"));
		Assert.assertFalse("Content-Type"==req.getHeader("Access-Control-Allow-Headers"));
		
		Assert.assertTrue("*"==res.getHeader("Access-Control-Allow-Origin"));
		Assert.assertTrue("GET, POST, DELETE, PUT"==res.getHeader("Access-Control-Allow-Methods"));
		Assert.assertTrue("Content-Type"==res.getHeader("Access-Control-Allow-Headers"));
	}
}
