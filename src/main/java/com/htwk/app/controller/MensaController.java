package com.htwk.app.controller;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.htwk.app.model.impl.Day;
import com.htwk.app.model.mensa.Meal;
import com.htwk.app.repository.MensaRepository;

@Controller
@RequestMapping(value = "/mensa")
public class MensaController {

	private static final Logger logger = LoggerFactory.getLogger(MensaController.class);

	@Autowired
	private MensaRepository repo;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/mensa";
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody
	Map<String, Object> home() {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		Map<String, Object> location = new TreeMap<String, Object>();
		location.put("Cafeteria Dittrichring", 153);
		location.put("Cafeteria Koburger Straﬂe", 121);
		location.put("Cafeteria Philipp-Rosenthal-Straﬂe", 127);
		location.put("Cafeteria W‰chterstraﬂe", 129);
		location.put("Mensa Academica", 118);
		location.put("Mensa am Park", 106);
		location.put("Mensa am Elsterbecken", 115);
		location.put("Mensaria Liebigstraﬂe", 162);
		location.put("Mensa Peterssteinweg", 111);
		location.put("Mensa Schˆnauer Straﬂe", 140);
		location.put("Mensa Tierklinik", 170);
		
		response.put("location", location);
		return response;
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/{location}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	Day<Meal> getMenuByLocation(@PathVariable(value = "location") String location) {
		return repo.get(location);
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/{location}/{date}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	Day<Meal> getMenuByLocationAndDate(@PathVariable(value = "location") String location,
			@PathVariable(value = "date") String date) {
		return repo.get(location, date);
	}
}