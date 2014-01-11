package com.htwk.app.cache;

import static org.hamcrest.CoreMatchers.*;
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

import com.google.common.base.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class GuavaCacheTest {

	private static final Logger logger = LoggerFactory.getLogger(GuavaCacheTest.class);
	
	private final String CACHE_NAME = "Testcache";
	
	private GuavaCache cache;
	
	private com.google.common.cache.Cache<Object, Optional<Object>> store;
	
	@Before
	public void init()
	{
		// TODO Mocking Store
		cache = new GuavaCache(CACHE_NAME, store);
	}
	
	@Test
	public void testName()
	{
		Assert.assertEquals(CACHE_NAME, cache.getName());
	}
	
	@Test
	public void testStore()
	{
		Assert.assertNotNull(cache.getNativeCache());
		Assert.assertThat(cache.getNativeCache(), instanceOf(com.google.common.cache.Cache.class));
	}
	
	@Test
	public void testSetAndGet()
	{
		// String - String
		cache.put("k1", "Test");
		Assert.assertEquals("Test", cache.get("k1"));
		
		// Integer - String
		cache.put(4, "Test2");
		Assert.assertEquals("Test2", cache.get(4));
		
		// String - Integer
		cache.put("k3", 42);
		Assert.assertEquals(42, cache.get("k3"));
		
		// String - Object
		cache.put("k4", logger);
		Assert.assertEquals(logger, cache.get("k4"));
		
		// Integer - Integer
		cache.put(23, 42);
		Assert.assertEquals(42, cache.get(23));
	}
	
	@Test
	public void testEvict()
	{
		String t1 = "K1";
		String t2 = "K2";
		
		cache.put(t1, "Test");
		
		cache.evict(t1);
		
		try {
			cache.evict(t2);
			Assert.fail();
		} catch (Exception e) {
			logger.info("Expected Exception was thrown!");
		}
	}
	
	@Test
	public void testClear()
	{
		cache.clear();
	}
	
	
}
