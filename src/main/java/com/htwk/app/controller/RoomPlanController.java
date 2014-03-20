package com.htwk.app.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.management.InvalidAttributeValueException;
import javax.naming.directory.InvalidAttributesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.ListMultimap;
import com.htwk.app.model.room.Room;
import com.htwk.app.repository.RoomPlanRepository;
import com.htwk.app.repository.TimetableRepository;

@Controller
@RequestMapping(value = "/room")
public class RoomPlanController {

	private static final Logger logger = LoggerFactory.getLogger(RoomPlanController.class);

	@Autowired
	private RoomPlanRepository repo;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/room";
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	String home() throws InvalidAttributesException, IOException {
		return "";
	}

	@RequestMapping(value = "/{semester}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	ResponseEntity getRooms(@PathVariable(value = "semester") String semester) throws IOException, URISyntaxException,
			InvalidAttributeValueException {
		ListMultimap<String, Room> rooms = repo.getRooms(semester);
		if (rooms == null) {
			throw new InvalidAttributeValueException("invalid semester");
		}
		return new ResponseEntity(rooms.asMap(), HttpStatus.OK);
	}

	@RequestMapping(value = "/{semester}/{kw}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	ResponseEntity getRoomPlan(@PathVariable(value = "semester") String semester, @PathVariable(value = "kw") String kw)
			throws InvalidAttributeValueException, IOException, URISyntaxException, ParseException {
		Object obj = repo.getFreeRoom(semester, kw);
		if (obj == null) {
			throw new InvalidAttributeValueException("invalid semester");
		}
		return new ResponseEntity(obj, HttpStatus.OK);

	}

	@RequestMapping(value = "/{semester}/{kw}/{day}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	ResponseEntity getRoomPlan(@PathVariable(value = "semester") String semester,
			@PathVariable(value = "kw") String kw, @PathVariable(value = "day") int day)
			throws InvalidAttributeValueException, IOException, URISyntaxException, ParseException {
		Object obj = repo.getFreeRoom(semester, kw, day);
		if (obj == null) {
			throw new InvalidAttributeValueException("invalid semester");
		}
		return new ResponseEntity(obj, HttpStatus.OK);

	}
}
