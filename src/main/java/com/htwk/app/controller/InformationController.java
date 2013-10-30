package com.htwk.app.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.htwk.app.model.info.Staff;
import com.htwk.app.repository.InformationRepository;

@Controller
@RequestMapping(value = "/info")
public class InformationController {

	private static final Logger logger = LoggerFactory.getLogger(InformationController.class);

	@Autowired
	private InformationRepository repo;

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String home() {
		return "";
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/info/";
	}

	@RequestMapping(value = "/staff", method = RequestMethod.GET)
	public @ResponseBody
	List<Staff> getProf() throws InvalidAttributesException, URISyntaxException {
		return repo.getStaff();
	}

	@RequestMapping(value = "/staff/{cuid}", method = RequestMethod.GET)
	public @ResponseBody
	Staff getStaff(@PathVariable(value = "cuid") String cuid) throws InvalidAttributesException, URISyntaxException {
		return repo.getStaff(cuid);
	}
	
	@RequestMapping(value = "/staff/{cuid}/detail", method = RequestMethod.GET)
	public @ResponseBody
	Staff getStaffDetailed(@PathVariable(value = "cuid") String cuid) throws InvalidAttributesException,
			URISyntaxException, IOException, ParseException {
		return repo.getStaffDetailed(cuid);
	}

}
