package com.htwk.app.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

import javax.naming.directory.InvalidAttributesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;

import com.htwk.app.repository.WeatherRepository;

@Controller
@RequestMapping(value = "/weather")
public class WeatherController {

	private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

	@Autowired
	private WeatherRepository repo ;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/weather";
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	ResponseEntity getSemGroups(@RequestParam(value = "location", required=false, defaultValue="") String location, @RequestParam(value = "days", required=false, defaultValue="3") String days) throws IOException,
			URISyntaxException, InvalidAttributesException, RestClientException, ParseException {
		return new ResponseEntity(repo.getWeather(location, days), HttpStatus.OK);
	}

}
