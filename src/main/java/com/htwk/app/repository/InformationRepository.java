package com.htwk.app.repository;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.common.cache.Cache;
import com.htwk.app.model.info.Building;
import com.htwk.app.model.info.Sport;
import com.htwk.app.model.info.Staff;
import com.htwk.app.model.info.StaffShort;
import com.htwk.app.repository.helper.impl.InformationConverter;

@Repository
public class InformationRepository {

	private static final Logger logger = LoggerFactory.getLogger(InformationRepository.class);

	@Autowired
	CacheManager cacheManager;

	RestTemplate restTemplate = null;
	ResponseEntity<String> response = null;
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

	private List<Staff> getStaffList() {
		staffCache.cleanUp();
		if (staffCache.size() > 0) {
			return new ArrayList<Staff>(new TreeMap<String, Staff>(staffCache.asMap()).values());
		}
		response = restTemplate.exchange(staffUrl, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

		if (response != null) {
			List<Staff> staffMembers = conv.getStaffList(response.getBody());
			for (Staff staff : staffMembers) {
				staffCache.put(staff.getCuid(), staff);
			}
			return staffMembers;
		}
		return null;
	}

	public List<StaffShort> getStaff() throws IOException, ParseException {
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

	public Staff getStaff(String cuid) throws IOException, ParseException {
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

	private Staff getStaffDetailed(Staff staff) throws IOException, ParseException {
		staff = conv.getStaffDetailed(staff);
		staffCache.put(staff.getCuid(), staff);
		return staff;
	}

	public ResponseEntity<byte[]> getStaffPic(String cuid) throws IOException, ParseException {
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "image/jpg; charset=binary");
	    HttpEntity<String> entity = new HttpEntity<String>(headers);
		return restTemplate.exchange(getStaff(cuid).getPictureLink(), HttpMethod.GET, entity, byte[].class);
	}

	private List<Building> getBuildingsList() throws IOException, ParseException {
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

	public List<Building> getBuildings() throws IOException, ParseException {
		return getBuildingsList();
	}

	public Building getBuilding(String id) throws IOException, ParseException {
		for (Building building : getBuildingsList()) {
			if (building.getId().equalsIgnoreCase(id)) {
				return building;
			}
		}
		return null;
	}

	private List<Sport> getSportList() throws IOException, ParseException {
		sportCache.cleanUp();
		if (sportCache.size() > 0) {
			return new ArrayList<Sport>(new TreeMap<String, Sport>(sportCache.asMap()).values());
		}

		response = restTemplate.exchange(sportUrl, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);
		if (response != null) {
			List<Sport> sports = conv.getSportList(response.getBody());
			for (Sport sport : sports) {
				sportCache.put(sport.getId(), sport);
			}
			return sports;
		}
		return null;
	}

	public List<Sport> getSport() throws IOException, ParseException {
		return getSportList();
	}

	public Sport getSport(String id) throws IOException, ParseException {
		sportCache.cleanUp();
		Sport cachedSport = sportCache.getIfPresent(id);
		if(cachedSport != null){
			return getSportDetailed(cachedSport);
		}
		
		for (Sport sport : getSportList()) {
			if (sport.getId().equalsIgnoreCase(id)) {
				return getSportDetailed(sport);
			}
		}
		return null;
	}

	private Sport getSportDetailed(Sport sport) throws IOException, ParseException {
		Sport sportDetailed = conv.getSportDetailed(sport);
		return sportDetailed;
	}

	public ResponseEntity<byte[]> getSportPic(String id) throws RestClientException, IOException, ParseException {
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "image/png; charset=binary");
	    HttpEntity<String> entity = new HttpEntity<String>(headers);
		return restTemplate.exchange(""+getSport(id).getPictureLink(), HttpMethod.GET, entity, byte[].class);
	}

}
