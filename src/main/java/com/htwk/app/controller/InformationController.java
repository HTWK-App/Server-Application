package com.htwk.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.htwk.app.repository.InformationRepository;

@Controller
@RequestMapping(value="/info")
public class InformationController {

	private static final Logger logger = LoggerFactory.getLogger(InformationController.class);
	
	@Autowired
	private InformationRepository repo;
	
	@RequestMapping(value="/", method = RequestMethod.GET,  produces="application/json")
	public @ResponseBody String home(){
		return "";
	}
	
	@RequestMapping(value="", method = RequestMethod.GET)
	public String redirectHome(){
		return "redirect:/";
	}
}
