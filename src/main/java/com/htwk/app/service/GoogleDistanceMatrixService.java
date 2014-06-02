package com.htwk.app.service;

import java.util.List;
import java.util.Map;
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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.htwk.app.model.info.Building;
import com.htwk.app.repository.InformationRepository;

@Service
public class GoogleDistanceMatrixService {

	private static final Logger logger = LoggerFactory.getLogger(GoogleDistanceMatrixService.class);

	private RestTemplate restTemplate = null;
	private HttpHeaders headers = null;
	private JsonParser parser = null;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private InformationRepository repo;

	@Value("${google.geocode}")
	private String baseUri;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.add("Content-Type", "text/xml;charset=UTF-8");
		parser = new JsonParser();
	}

	public Map<Long, Building> getDistances(String location) throws Exception {
		return getDistance(location, getKnownBuildingLocations());
	}

	public Map<Long, Building> getDistances(String location, String destinations) throws Exception {
		return getDistance(location, destinations);
	}

	public Map<Long, Building> getDistances(String location, List<Building> buildings) throws Exception {
		String destinations = "";
		for (Building building : buildings) {
			destinations += building.getLatLng() + "|";
		}
		return getShortestDistance(getGoolgeDistance(location, destinations), buildings);
	}

	private Map<Long, Building> getDistance(String location, String destinations) throws Exception {
		return getShortestDistance(getGoolgeDistance(location, destinations));
	}

	private String getGoolgeDistance(String location, String destinations) {
		String distanceUri = baseUri + "origins=" + location;
		distanceUri += "&destinations=" + destinations + "&mode=walking&language=de_DE&sensor=false";
		logger.debug("" + distanceUri);
		ResponseEntity<String> response = restTemplate.exchange(distanceUri, HttpMethod.GET, new HttpEntity<Object>(
				headers), String.class);

		if (response.hasBody()) {
			return response.getBody();
		}
		return null;
	}

	private String getKnownBuildingLocations() throws Exception {
		String result = "";
		for (Building building : repo.getBuildings()) {
			result += building.getLatLng() + "|";
		}
		return result.trim();
	}

	private Map<Long, Building> getShortestDistance(String content) throws Exception {
		JsonObject response = parser.parse(content).getAsJsonObject();
		String status = response.get("status").getAsString();

		Map<Long, Building> distanceMap = new TreeMap<Long, Building>();
		int index = 0;
		if (status.equalsIgnoreCase("OK")) {
			JsonArray rows = response.get("rows").getAsJsonArray();
			if (rows.size() >= 0) {
				JsonArray elements = rows.get(0).getAsJsonObject().get("elements").getAsJsonArray();
				for (index = 0; index < elements.size(); index++) {

					long distance = elements.get(index).getAsJsonObject().get("distance").getAsJsonObject()
							.get("value").getAsLong();
					// wrong maappping
					Building building = repo.getBuildings().get(index);
					distanceMap.put(distance, building);
				}
			}
		}
		return distanceMap;
	}

	private Map<Long, Building> getShortestDistance(String content, List<Building> buildings) throws Exception {
		JsonObject response = parser.parse(content).getAsJsonObject();
		String status = response.get("status").getAsString();

		Map<Long, Building> distanceMap = new TreeMap<Long, Building>();
		int index = 0;
		if (status.equalsIgnoreCase("OK")) {
			JsonArray rows = response.get("rows").getAsJsonArray();
			if (rows.size() >= 0) {
				JsonArray elements = rows.get(0).getAsJsonObject().get("elements").getAsJsonArray();
				for (index = 0; index < elements.size(); index++) {

					long distance = elements.get(index).getAsJsonObject().get("distance").getAsJsonObject()
							.get("value").getAsLong();
					// wrong maappping
					Building building = buildings.get(index);
					distanceMap.put(distance, building);
				}
			}
		}
		return distanceMap;
	}
}
