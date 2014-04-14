package com.htwk.app.service;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.htwk.app.controller.InformationController;
import com.htwk.app.model.info.Staff;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class UpdateServiceTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private InformationController ctrl;
	private Staff staffMember;
	private Staff staffMember2;


	@Before
	public void setUp() throws Exception {
		ctrl = new InformationController();
		staffMember = new Staff();
		staffMember.setCuid("123");
		staffMember.setDescription("testest");
		staffMember.setEmail("123@htwk-leipzig.de");
		staffMember2 = new Staff();
		staffMember2.setCuid("123");
		staffMember2.setDescription("testest");
		staffMember2.setEmail("123@htwk-leipzig.de");
		
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void hashEqualsTest() {
		Assert.assertEquals(EqualsBuilder.reflectionEquals(staffMember, staffMember2), true);

		staffMember2.setCuid("1234");
		Assert.assertEquals(EqualsBuilder.reflectionEquals(staffMember, staffMember2), false);

	}

}
