package com.htwk.app.controller;

import java.text.ParseException;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.naming.directory.InvalidAttributeValueException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.htwk.app.model.impl.Day;
import com.htwk.app.model.mensa.Meal;
import com.htwk.app.repository.MensaRepository;

@Controller
@RequestMapping(value = "/mensa")
public class MensaController {

	private static final Logger logger = LoggerFactory.getLogger(MensaController.class);

	@Autowired
	private MensaRepository repo;
	
	private Map<String, Object> locations;
	
	@PostConstruct
	public void init(){
		locations = new TreeMap<String, Object>();
		locations.put("Cafeteria Dittrichring", 153);
		locations.put("Cafeteria Koburger Straße", 121);
		locations.put("Cafeteria Philipp-Rosenthal-Straße", 127);
		locations.put("Cafeteria Wächterstraße", 129);
		locations.put("Mensa Academica", 118);
		locations.put("Mensa am Park", 106);
		locations.put("Mensa am Elsterbecken", 115);
		locations.put("Mensaria Liebigstraße", 162);
		locations.put("Mensa Peterssteinweg", 111);
		locations.put("Mensa Schönauer Straße", 140);
		locations.put("Mensa Tierklinik", 170);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/mensa";
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody
	Map<String, Object> home() {
		Map<String, Object> response = new TreeMap<String, Object>();

		response.put("location", locations);
		return response;
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/{location}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	Day<Meal> getMenuByLocation(@PathVariable(value = "location") String location) throws ParseException,
			InvalidAttributeValueException {
		if(!locations.containsKey(location)){
			throw new InvalidAttributeValueException("no valid location was chosen");
		}
		return repo.get(location);
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/{location}/{date}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	Day<Meal> getMenuByLocationAndDate(@PathVariable(value = "location") String location,
			@PathVariable(value = "date") String date) throws ParseException, InvalidAttributeValueException {
		if(!locations.containsKey(location)){
			throw new InvalidAttributeValueException("no valid location was chosen");
		}
		return repo.get(location, date);
	}
}