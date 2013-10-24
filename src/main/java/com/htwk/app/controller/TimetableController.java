package com.htwk.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.htwk.app.repository.TimetableRepository;

@Controller
@RequestMapping(value="/timetable")
public class TimetableController {

	@Autowired
	private TimetableRepository repo;
	
	@RequestMapping(value="", method = RequestMethod.GET,  produces="application/json")
	public @ResponseBody String home(){
		return "";
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String redirectHome(){
		return "redirect:";
	}

}
