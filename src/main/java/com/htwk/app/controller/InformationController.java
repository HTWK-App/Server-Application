package com.htwk.app.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.htwk.app.model.info.Building;
import com.htwk.app.model.info.Sport;
import com.htwk.app.model.info.Staff;
import com.htwk.app.model.info.StaffShort;
import com.htwk.app.repository.InformationRepository;

@Controller
@RequestMapping(value = "/info")
public class InformationController {

	private static final Logger logger = LoggerFactory.getLogger(InformationController.class);

	@Autowired
	private InformationRepository repo;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String home() {
		return "";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/info";
	}

	@RequestMapping(value = "/staff", method = RequestMethod.GET)
	public @ResponseBody
	List<StaffShort> getStaff() throws IOException, ParseException {
		return repo.getStaff();
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/staff/{cuid}", method = RequestMethod.GET)
	public @ResponseBody
	Staff getStaff(@PathVariable(value = "cuid") String cuid) throws IOException, ParseException {
		return repo.getStaff(cuid);
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/staff/{cuid}/pic", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<byte[]> getStaffPic(@PathVariable(value = "cuid") String cuid) throws IOException, ParseException {
		return repo.getStaffPic(cuid);
	}

	@RequestMapping(value = "/building", method = RequestMethod.GET)
	public @ResponseBody
	List<Building> getBuildings() throws InvalidAttributesException, IOException, ParseException {
		return repo.getBuildings();
	}

	@RequestMapping(value = "/building/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Building getBuilding(@PathVariable(value = "id") String id) throws IOException, ParseException {
		return repo.getBuilding(id);
	}

	@RequestMapping(value = "/sport", method = RequestMethod.GET)
	public @ResponseBody
	List<Sport> getSport() throws IOException, ParseException {
		return repo.getSport();
	}

	@RequestMapping(value = "/sport/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Sport getSport(@PathVariable(value = "id") String id) throws IOException, ParseException {
		return repo.getSport(id);
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/sport/{id}/pic", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<byte[]> getSportPic(@PathVariable(value = "id") String id) throws IOException, ParseException {
		return repo.getSportPic(id);
	}
}
