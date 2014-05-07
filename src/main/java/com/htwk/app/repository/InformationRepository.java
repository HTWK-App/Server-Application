package com.htwk.app.repository;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import org.apache.xerces.impl.dv.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.google.common.cache.Cache;
import com.htwk.app.model.info.Building;
import com.htwk.app.model.info.Sport;
import com.htwk.app.model.info.Staff;
import com.htwk.app.model.info.StaffShort;
import com.htwk.app.repository.helper.impl.InformationConverter;
import com.htwk.app.utils.images.ImageResizeAction;
import com.htwk.app.utils.images.ImageResizeRequest;
import com.htwk.app.utils.images.ImageResizeService;

@Repository
public class InformationRepository {

	private static final Logger logger = LoggerFactory.getLogger(InformationRepository.class);

	@Autowired
	CacheManager cacheManager;

	@Autowired
	private ImageResizeService handler;

	RestTemplate restTemplate = null;
	HttpHeaders headers = null;
	URI uri = null;
	InformationConverter conv = null;
	Cache<String, Staff> staffCache = null;
	Cache<String, Building> buildingCache = null;
	Cache<String, Sport> sportCache = null;

	@Value("${info.staff.url}")
	private String staffUrl;

	@Value("${info.sport.url}")
	private String sportUrl;

	@Value("${info.academical.url}")
	private String academicalUrl;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.add("Content-Type", "text/xml;charset=utf-8");
		conv = new InformationConverter();
		staffCache = (Cache<String, Staff>) cacheManager.getCache("staffCache").getNativeCache();
		buildingCache = (Cache<String, Building>) cacheManager.getCache("buildingCache").getNativeCache();
		sportCache = (Cache<String, Sport>) cacheManager.getCache("sportCache").getNativeCache();
	}

	private synchronized final List<Staff> getStaffList() {
		staffCache.cleanUp();
		if (staffCache.size() > 0) {
			return new ArrayList<Staff>(new TreeMap<String, Staff>(staffCache.asMap()).values());
		}
		ResponseEntity<String> response = restTemplate.exchange(staffUrl, HttpMethod.GET, new HttpEntity<Object>(
				headers), String.class);

		if (response != null) {
			List<Staff> staffMembers = conv.getStaffList(response.getBody());
			for (Staff staff : staffMembers) {
				staffCache.put(staff.getCuid(), staff);
			}
			return staffMembers;
		}
		return null;
	}

	public synchronized final List<StaffShort> getStaff() throws IOException, ParseException {
		List<StaffShort> shorts = new ArrayList<StaffShort>();
		for (Staff staff : getStaffList()) {
			shorts.add(new StaffShort(staff));
		}
		Collections.sort(shorts, new Comparator<StaffShort>() {
			@Override
			public int compare(StaffShort o1, StaffShort o2) {
				int facultyResult = o1.getFaculty().compareTo(o2.getFaculty());
				if (facultyResult != 0) {
					return facultyResult;
				}
				return o1.getName().compareTo(o2.getName());
			}
		});
		return shorts;
	}

	public synchronized final Staff getStaff(String cuid) throws Exception {
		staffCache.cleanUp();
		Staff staff = staffCache.getIfPresent(cuid);
		if (staff != null) {
			return getStaffDetailed(staff);
		}
		for (Staff staffMember : getStaffList()) {
			if (staffMember.getCuid().equals(cuid)) {
				staffMember = getStaffDetailed(staffMember);
				return staffMember;
			}
		}
		return null;
	}

	private synchronized final Staff getStaffDetailed(Staff staff) throws Exception {
		Staff staffDetailed = conv.getStaffDetailed(staff);
		staffDetailed.setPictureData(getPicData(staffDetailed.getPictureLink()));
		return staffDetailed;

	}

	public synchronized final ResponseEntity<byte[]> getStaffPic(String cuid) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "image/jpg");
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<byte[]> response = restTemplate.exchange(getStaff(cuid).getPictureLink(), HttpMethod.GET,
				entity, byte[].class);
		return new ResponseEntity<byte[]>(compressPic(response.getBody()), headers, HttpStatus.OK);
	}

	private synchronized final List<Building> getBuildingsList() throws IOException, ParseException {
		buildingCache.cleanUp();
		if (buildingCache.size() > 0) {
			return new ArrayList<Building>(new TreeMap<String, Building>(buildingCache.asMap()).values());
		}
		List<String> locations = new ArrayList<String>();
		List<Building> buildings = new ArrayList<Building>();

		for (Staff staff : getStaffList()) {
			String link = staff.getLocationLink();
			if (!locations.contains(link)) {
				locations.add(link);
				Building building = conv.getBuildingInfo(link);
				buildings.add(building);
				if (building != null && building.getId() != null) {
					buildingCache.put(building.getId(), building);
				}
			}
		}
		return buildings;
	}

	public synchronized final List<Building> getBuildings() throws IOException, ParseException {
		return getBuildingsList();
	}

	public synchronized final Building getBuilding(String id) throws Exception {
		for (Building building : getBuildingsList()) {
			if (building.getId().equalsIgnoreCase(id)) {
				building.setPictureData(getPicData(building.getPictureLink()));
				return building;
			}
		}
		return null;
	}

	private synchronized final List<Sport> getSportList() throws IOException, ParseException {
		sportCache.cleanUp();
		if (sportCache.size() > 0) {
			return new ArrayList<Sport>(new TreeMap<String, Sport>(sportCache.asMap()).values());
		}

		ResponseEntity<String> response = restTemplate.exchange(sportUrl, HttpMethod.GET, new HttpEntity<Object>(
				headers), String.class);
		if (response != null) {
			List<Sport> sports = conv.getSportList(response.getBody());
			for (Sport sport : sports) {
				sportCache.put(sport.getId(), sport);
			}
			return sports;
		}
		return null;
	}

	public synchronized final List<Sport> getSport() throws IOException, ParseException {
		return getSportList();
	}

	public synchronized final Sport getSport(String id) throws Exception {
		sportCache.cleanUp();
		Sport cachedSport = sportCache.getIfPresent(id);
		if (cachedSport != null) {
			return getSportDetailed(cachedSport);
		}
		for (Sport sport : getSportList()) {
			if (sport.getId().equalsIgnoreCase(id)) {
				return getSportDetailed(sport);
			}
		}
		return null;
	}

	private synchronized final Sport getSportDetailed(Sport sport) throws Exception {
		Sport sportDetailed = conv.getSportDetailed(sport);
		// sportDetailed.setPictureData(getSportPic(sport.getId()).getBody().toString());
		return sportDetailed;
	}

	public synchronized final ResponseEntity<byte[]> getSportPic(String id) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "image/png");
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return restTemplate.exchange("" + getSport(id).getPictureLink(), HttpMethod.GET, entity, byte[].class);
	}

	public synchronized final Map<String, Collection<String>> getAcademicalCalendar(String semester) {
		String url = MessageFormat.format(academicalUrl, semester);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(headers),
				String.class);
		if (response != null) {
			Map<String, Collection<String>> cal = conv.getAcademicalCalendar(response.getBody());
			return cal;
		}
		return null;
	}

	public synchronized final String getPicData(String uri) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		String prefix = "";
		boolean compress = true;
		if (uri.contains("gif")) {
			prefix = "data:image/gif;base64,";
			headers.set("Content-Type", "image/gif");
			compress = false;
		} else if (uri.contains("jpeg")) {
			prefix = "data:image/jpeg;base64,";
			headers.set("Content-Type", "image/jpeg");
		} else if (uri.contains("jpg")) {
			prefix = "data:image/jpg;base64,";
			headers.set("Content-Type", "image/jpeg");
		} else if (uri.contains("tiff")) {
			prefix = "data:image/tiff;base64,";
			headers.set("Content-Type", "image/tiff");
		} else {
			prefix = "data:image/png;base64,";
			headers.set("Content-Type", "image/png");
		}

		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
		return prefix + Base64.encode(compress ? compressPic(response.getBody()) : response.getBody());
	}

	private synchronized final byte[] compressPic(byte[] data) throws Exception {
		ImageResizeRequest request = new ImageResizeRequest();
		InputStream in = new ByteArrayInputStream(data);
		BufferedImage sourceImage = ImageIO.read(in);
		BufferedImage targetImage;

		request.setSourceImage(sourceImage);
		request.setTargetHeight(sourceImage.getHeight());
		request.setTargetWidth(sourceImage.getWidth());
		request.setMaintainAspect(true); // This is the default
		request.setCropToAspect(true); // This is the default
		request.setResizeAction(ImageResizeAction.IF_LARGER); // This is the
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {

			targetImage = handler.resize(request);
			ImageIO.write(targetImage, "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			return imageInByte;
		} catch (IIOException ex) {
			return data;
		} finally {
			baos.close();
		}
	}
}
