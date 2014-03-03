package com.htwk.app.cache;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class CacheConfigTest {

	private static final Logger logger = LoggerFactory.getLogger(CacheConfigTest.class);
	
	private CacheConfig cfg;
	
	@Before
	public void init() {
		cfg = new CacheConfig();
	}
	
	@Test
	public void testDefaultReaction()
	{
		Assert.assertThat(cfg.cacheManager(), instanceOf(CacheManager.class));
	}
}
