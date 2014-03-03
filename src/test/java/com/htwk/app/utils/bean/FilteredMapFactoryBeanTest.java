package com.htwk.app.utils.bean;

import java.util.HashMap;
import java.util.Map;

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
public class FilteredMapFactoryBeanTest {
	
	private static final Logger logger = LoggerFactory.getLogger(FilteredMapFactoryBeanTest.class);

	private FilteredMapFactoryBean<Integer> bean;
	
	@Before
	public void init()
	{
		bean = new FilteredMapFactoryBean<Integer>();
	}
	
	@Test
	public void testDefault()
	{
		Map<String, Integer> m = new HashMap<String, Integer>();
		
		m.put("test", 1);
		m.put("abc", 2);
		
		try {
			bean.setInput(m);
			bean.setKeyFilterPrefix("test");
			
			Map<String, Integer> m2 = bean.createInstance();
			
			Assert.assertNotNull(m2);
			Assert.assertTrue(m.size()==m2.size()+1);
			Assert.assertTrue(1==m2.get("test"));
		} catch (Exception e) {
			Assert.fail();
			logger.info("Unexpected Exception occurred!");
		}
		
		Assert.assertEquals(Map.class, bean.getObjectType());
	}
}
