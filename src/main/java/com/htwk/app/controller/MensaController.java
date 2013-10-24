package com.htwk.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.htwk.app.repository.MensaRepository;

@Controller
@RequestMapping(value="/mensa")
public class MensaController {

	@Autowired
	private MensaRepository repo;
	
	@RequestMapping(value="", method = RequestMethod.GET,  produces="application/json")
	public @ResponseBody ResponseEntity home(){
		HttpHeaders responseHeaders = new HttpHeaders();  
        responseHeaders.set("MyResponseHeader", "MyValue");
        return new ResponseEntity("Hello World", responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String redirectHome(){
		return "redirect:";
	}
}
