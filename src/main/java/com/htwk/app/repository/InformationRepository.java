package com.htwk.app.repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.naming.directory.InvalidAttributesException;

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
import com.htwk.app.model.info.News;
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

	@Value("${info.staff.url}")
	private String staffUrl;

	@Value("${cache.time.time}")
	private long time;

	@Value("${cache.time.timeUnit}")
	private String timeUnit;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.add("Content-Type", "text/xml;charset=UTF-8");
		conv = new InformationConverter();
		staffCache = (Cache<String, Staff>) cacheManager.getCache("staffCache").getNativeCache();
		buildingCache = (Cache<String, Building>) cacheManager.getCache("buildingCache").getNativeCache();
	}

	private List<Staff> getStaffList() throws URISyntaxException, InvalidAttributesException {
		if (staffCache.size() > 0) {
			return new ArrayList<Staff>(new TreeMap<String, Staff>(staffCache.asMap()).values());
		}

		staffUrl = MessageFormat.format(staffUrl, "");

		uri = new URI(staffUrl);
		logger.debug("getData from URI: " + uri.toString());
		response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

		if (response != null) {
			List<Staff> staffMembers = conv.getStaffList(response.getBody());
			for (Staff staff : staffMembers) {
				staffCache.put(staff.getCuid(), staff);
			}
			return staffMembers;
		}

		return null;
	}

	public List<Staff> getStaff() throws URISyntaxException, InvalidAttributesException {
		return getStaffList();
	}

	public Staff getStaff(String cuid) throws InvalidAttributesException, URISyntaxException {
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

	public Staff getStaffDetailed(String cuid) throws InvalidAttributesException, URISyntaxException, IOException,
			ParseException {
		return conv.getStaffDetailed(getStaff(cuid));
	}

	private List<Building> getBuildingsList() throws InvalidAttributesException, URISyntaxException, IOException,
			ParseException {
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

	public List<Building> getBuildings() throws InvalidAttributesException, URISyntaxException, IOException,
			ParseException {
		return getBuildingsList();
	}

	public Building getBuilding(String id) throws InvalidAttributesException, URISyntaxException, IOException,
			ParseException {
		for (Building building : getBuildingsList()) {
			if (building.getId().equalsIgnoreCase(id)) {
				return building;
			}
		}
		return null;
	}

	public List<News> getNews() {
		return null;
	}

}
