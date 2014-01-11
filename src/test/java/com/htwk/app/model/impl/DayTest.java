package com.htwk.app.model.impl;

import java.util.ArrayList;
import java.util.List;

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
public class DayTest {
	
	private static final Logger logger = LoggerFactory.getLogger(DayTest.class);

	private Day<Integer> day;
	
	@Before
	public void init()
	{
		day = new Day<Integer>();
	}
	
	@Test
	public void testID()
	{
		day.setId("42");
		
		Assert.assertEquals("42", day.getId());
	}
	
	@Test
	public void testDayContent()
	{
		List<Integer> l = new ArrayList<Integer>();
		
		l.add(1);
		l.add(2);
		
		day.setDayContent(l);
		
		Assert.assertEquals(l.size(), day.getDayContent().size());
		Assert.assertEquals(l.get(0), day.getDayContent().get(0));
		Assert.assertEquals(l.get(1), day.getDayContent().get(1));
		
		try {
			day.getDayContent().get(2);
			Assert.fail();
		} catch (Exception e) {
			logger.info("Expected Exception was thrown.");
		}
	}
}
