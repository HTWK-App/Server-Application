package com.htwk.app.repository;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
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
import org.springframework.web.client.RestTemplate;

import com.google.common.cache.Cache;
import com.htwk.app.model.info.Building;
import com.htwk.app.model.info.Sport;
import com.htwk.app.model.info.Staff;
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

	private List<Staff> getStaffList(){
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

	public List<Staff> getStaff() {
		return getStaffList();
	}
	
	public Staff getStaff(String cuid){
		Staff staff = staffCache.getIfPresent(cuid);
		if (staff != null) {
			return staff;
		}
		List<Staff> staffMembers = getStaffList();

		// int i = 0;
		for (Staff staffMember : staffMembers) {
			if (staffMember.getCuid().equals(cuid)) {
				// return staffMembers.subList(i, i+1);
				return staffMember;
			}
			// i++;
		}
		return null;
	}

	public Staff getStaffDetailed(String cuid) throws IOException, ParseException {
		Staff staff = conv.getStaffDetailed(getStaff(cuid));
		staffCache.put(cuid, staff);
		return staff;
	}

	private List<Building> getBuildingsList() throws IOException, ParseException {
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
		for (Sport sport : getSportList()) {
			if (sport.getId().equalsIgnoreCase(id)) {
				return sport;
			}
		}
		return null;
	}

	public Sport getSportDetailed(String id) throws IOException, ParseException {
		Sport sportDetailed = conv.getSportDetailed(getSport(id));
		sportCache.put(id, sportDetailed);
		return sportDetailed;
	}

}
