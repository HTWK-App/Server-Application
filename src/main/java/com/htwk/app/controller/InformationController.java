package com.htwk.app.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.management.InvalidAttributeValueException;
import javax.naming.directory.InvalidAttributesException;

import org.apache.xerces.impl.dv.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;

import com.htwk.app.model.info.Building;
import com.htwk.app.model.info.Sport;
import com.htwk.app.model.info.Staff;
import com.htwk.app.model.info.StaffShort;
import com.htwk.app.repository.InformationRepository;
import com.htwk.app.utils.images.ImageResizeAction;
import com.htwk.app.utils.images.ImageResizeRequest;
import com.htwk.app.utils.images.ImageResizeService;

@Controller
@RequestMapping(value = "/info")
public class InformationController {

	private static final Logger logger = LoggerFactory.getLogger(InformationController.class);

	@Autowired
	private InformationRepository repo;

	@Autowired
	private ImageResizeService handler;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	String home() {
		return "";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/info";
	}
	
	@RequestMapping(value = "/academical/{semester}", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Collection<String>> getAcademicalCalendar(@PathVariable String semester) throws IOException,
			ParseException, InvalidAttributeValueException {
		if(semester.equals("ss")){
			return repo.getAcademicalCalendar("sommersemester");
		} else if(semester.equals("ws")){
			return repo.getAcademicalCalendar("wintersemester");
		}else {
			throw new InvalidAttributeValueException("wrong parameter was used! possible parameters{ss|ws}");
		}		
	}

	@RequestMapping(value = "/staff", method = RequestMethod.GET)
	public @ResponseBody
	List<StaffShort> getStaff() throws IOException, ParseException {
		return repo.getStaff();
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/staff/{cuid}", method = RequestMethod.GET)
	public @ResponseBody
	Staff getStaff(@PathVariable(value = "cuid") String cuid) throws Exception {
		Staff staff = repo.getStaff(cuid);
		if(staff == null){
			throw new InvalidAttributeValueException("invalid staff-id");
		}
		staff.setPictureData(getStaffPicData(cuid));
		return staff;
	}

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

	@Cacheable("timeCache")
	@RequestMapping(value = "/building/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Building getBuilding(@PathVariable(value = "id") String id) throws IOException, ParseException, InvalidAttributeValueException {
		Building building = repo.getBuilding(id); 
		if(building == null){
			throw new InvalidAttributeValueException("invalid building-id");
		}
		return building;
	}

	@RequestMapping(value = "/sport", method = RequestMethod.GET)
	public @ResponseBody
	List<Sport> getSport() throws IOException, ParseException {
		return repo.getSport();
	}

	@Cacheable("timeCache")
	@RequestMapping(value = "/sport/{id}", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity getSport(@PathVariable(value = "id") String id) throws IOException, ParseException,
			InvalidAttributeValueException {
		if(id.equals("pic")){
			//return new ResponseEntity(repo.getSportPics(), HttpStatus.OK);
		}
		Sport sport = repo.getSport(id);
		if(sport == null){
			throw new InvalidAttributeValueException("invalid sport-id");
		}
		return new ResponseEntity(sport, HttpStatus.OK);
	}

	@RequestMapping(value = "/sport/{id}/pic", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<byte[]> getSportPic(@PathVariable(value = "id") String id) throws IOException, ParseException {
		return repo.getSportPic(id);
	}

	@RequestMapping(value = "/staff/{cuid}/thumb", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<byte[]> getPic(@PathVariable(value = "cuid") String cuid) throws Exception {
		logger.debug("reeeesize");

		ImageResizeRequest request = new ImageResizeRequest();
		InputStream in = new ByteArrayInputStream(repo.getStaffPic(cuid).getBody());
		long before = System.currentTimeMillis();
		BufferedImage sourceImage = ImageIO.read(in);
		BufferedImage targetImage;

		request.setSourceImage(sourceImage);
		request.setTargetHeight(64);
		request.setTargetWidth(64);
		request.setMaintainAspect(true); // This is the default
		request.setCropToAspect(true); // This is the default
		request.setResizeAction(ImageResizeAction.IF_LARGER); // This is the
																// default
		targetImage = handler.resize(request);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(targetImage, "jpg", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "image/png");
		logger.debug("{}", System.currentTimeMillis()-before);
		return new ResponseEntity<byte[]>(imageInByte, headers, HttpStatus.OK);
	}
	
	private synchronized final String getStaffPicData(String cuid) throws Exception {
		return "data:image/png;base64,"+Base64.encode(getPic(cuid).getBody());
	}
}
