package com.htwk.app.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.htwk.app.SpringWebContextTests;
import com.htwk.app.repository.TimetableRepository;


public class TimetableControllerTest extends SpringWebContextTests{

	@InjectMocks
	private TimetableController ctrl;
	
	@InjectMocks
	private TimetableRepository repo;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		// Process mock annotations
		MockitoAnnotations.initMocks(this);

		// Setup Spring test in standalone mode
		this.mockMvc = MockMvcBuilders.standaloneSetup(ctrl).build();
	}

	@Test
	public void testDefaultReaction() throws Exception {
		Assert.assertEquals(ctrl.home() , "");
		Assert.assertEquals(ctrl.redirectHome(), "redirect:/timetable");

		MvcResult result = mockMvc.perform(get("/timetable"))
	            .andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8")).andReturn();
		
		System.out.println(new String(result.getResponse().getContentAsByteArray()));
	}

}
