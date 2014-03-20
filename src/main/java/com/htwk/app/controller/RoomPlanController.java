package com.htwk.app.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
			InvalidAttributesException {
		ListMultimap<String, Room> rooms = repo.getRooms(semester);
		return new ResponseEntity(rooms.asMap(), HttpStatus.OK);
	}

	@RequestMapping(value = "/{semester}/{kw}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	ResponseEntity getRoomPlan(@PathVariable(value = "semester") String semester,
			@PathVariable(value = "kw") String kw,
			@RequestParam(value = "roomId", required = false, defaultValue = "%23SPLUS786F4E") String roomId)
			throws IOException, URISyntaxException, InvalidAttributesException, ParseException {
		// if (cal.containsKey(kw)) {
		// ListMultimap<String, Room> rooms = repo.getFreeRooms(semester, kw);
		// return new ResponseEntity(rooms.asMap(), HttpStatus.OK);
		// }
		
		//return new ResponseEntity(repo.getRoomPlan(semester, roomId, kw), HttpStatus.OK);
		Object obj = repo.getFreeRoom(semester, kw);
		return new ResponseEntity(obj, HttpStatus.OK);

	}
}
