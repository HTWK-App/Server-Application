package com.htwk.app.repository;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.management.InvalidAttributeValueException;

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
import com.google.common.collect.ArrayListMultimap;
import com.htwk.app.model.impl.Day;
import com.htwk.app.model.info.Building;
import com.htwk.app.model.room.Room;
import com.htwk.app.model.timetable.Subject;
import com.htwk.app.repository.helper.impl.RoomPlanConverter;
import com.htwk.app.repository.helper.impl.TimetableConverter;
import com.htwk.app.service.GoogleDistanceMatrixService;

@Repository
public class RoomPlanRepository {

	private static final Logger logger = LoggerFactory.getLogger(RoomPlanRepository.class);

	private RestTemplate restTemplate = null;
	private ResponseEntity<String> response = null;
	private HttpHeaders headers = null;
	private RoomPlanConverter conv = null;
	private TimetableConverter timetableConv = null;
	private Cache<String, Object> cache = null;
	private SimpleDateFormat timeParser = null;

	@Value("${timetable.url}")
	private String roomPlanUrl;

	@Value("${timetable.roomList}")
	private String roomPlanRoomList;

	@Value("${timetable.roomReport}")
	private String roomReport;

	@Value("${timetable.profs}")
	private String timetableProfs;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private TimetableRepository timetableRepo;
	
	@Autowired
	private InformationRepository informationRepo;
	
	@Autowired
	private GoogleDistanceMatrixService distanceService;


	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.add("Content-Type", "text/xml;charset=UTF-8");
		conv = new RoomPlanConverter();
		timetableConv = new TimetableConverter();
		timeParser = new SimpleDateFormat("HH:mm");
		cache = (Cache<String, Object>) cacheManager.getCache("timeCache").getNativeCache();
	}

	public final ArrayListMultimap<String, Room> getRooms(String semester)
			throws UnsupportedEncodingException {
		roomPlanRoomList = MessageFormat.format(roomPlanRoomList, semester);

		response = restTemplate.exchange(roomPlanUrl + roomPlanRoomList, HttpMethod.GET,
				new HttpEntity<Object>(headers), String.class);
		if (response.hasBody()) {
			return conv.getRoomList(response.getBody());
		}
		return null;
	}

	public final List<Day<Subject>> getRoomPlan(String semester, String roomId, String kw, String day)
			throws IOException, URISyntaxException, InvalidAttributeValueException {
		List<Day<Subject>> plan = (List<Day<Subject>>) cache.getIfPresent(kw + day + roomId);
		if (plan != null) {
			return plan;
		}

		Map<String, String> cal = timetableRepo.getCalendar();
		if (!cal.containsKey(kw)) {
			throw new InvalidAttributeValueException("this week is not in the given semester");
		}
		URI uri = new URI(roomPlanUrl + MessageFormat.format(roomReport, semester, roomId, kw, day));

		response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(headers), String.class);

		String profsUri = roomPlanUrl + MessageFormat.format(timetableProfs, semester);
		ResponseEntity<String> profsContent = restTemplate.exchange(profsUri, HttpMethod.GET, new HttpEntity<Object>(
				headers), String.class);
		if (response.hasBody()) {

			List<Day<Subject>> timetable = timetableConv.getTimetable(response.getBody(), profsContent.getBody());
			cache.put(kw + day + roomId, timetable);
			return timetable;
		}
		return null;
	}

	public final Map<String, Collection<Room>> getFreeRoom(String semester, String kw) throws IOException,
			URISyntaxException, ParseException, InvalidAttributeValueException {
		Calendar cal = Calendar.getInstance(Locale.GERMAN);
		int calDay = (cal.get(Calendar.DAY_OF_WEEK) - 1) % 7;
		return getFreeRooms(semester, kw, calDay);
	}

	public final Map<String, Collection<Room>> getFreeRoom(String semester, String kw, int day)
			throws IOException, URISyntaxException, ParseException, InvalidAttributeValueException {
		return getFreeRooms(semester, kw, day);
	}

	private final Map<String, Collection<Room>> getFreeRooms(String semester, String kw, int day)
			throws IOException, URISyntaxException, ParseException, InvalidAttributeValueException {
		Map<String, String> studCal = timetableRepo.getCalendar();
		if (!studCal.containsKey(kw)) {
			throw new InvalidAttributeValueException("this week is not in the given semester");
		}
		if (day < 1 && day > 7) {
			throw new InvalidAttributeValueException("the given day is out of range (1-7)");
		}

		Calendar cal = Calendar.getInstance(Locale.GERMAN);
		String now = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
		Date userDate = timeParser.parse(now);

		ArrayListMultimap<String, Room> roomMap = getRooms(semester);
		ArrayListMultimap<String, Room> whiteListedRooms = ArrayListMultimap.create();

		for (Entry<String, Collection<Room>> geb : roomMap.asMap().entrySet()) {
			boolean add = false;
			for (Room room : geb.getValue()) {
				Iterator<Day<Subject>> plan = getRoomPlan(semester, room.getId(), kw, "" + day).iterator();
				label: while (plan.hasNext()) {
					for (Subject subject : plan.next().getDayContent()) {
						Date begin = timeParser.parse(subject.getBegin());
						Date end = timeParser.parse(subject.getEnd());
						if (userDate.after(begin) && userDate.before(end)) {
							add = false;
							break label;
						} else {
							add = true;
						}
					}
				}
				if (add) {
					whiteListedRooms.put(geb.getKey(), room);
				}
			}
		}
		return whiteListedRooms.asMap();
	}

	public Map<Long, Collection<Room>> getFreeRoomByLocation(String semester, String kw, int day, String location)
			throws Exception {
		List<Building> destinations = new ArrayList<Building>();
		Map<String, Collection<Room>> roomMap = getFreeRoom(semester, kw, day);
		for (Entry<String, Collection<Room>> entry : roomMap.entrySet()) {
			String key = entry.getKey();
			String bid = "";
			if (key.contains("=")) {
				bid= key.split("=")[0].trim();
				logger.info(""+bid);
				destinations.add(informationRepo.getBuilding(bid));
				
			}
		}
		Map<Long, Collection<Room>> map = new TreeMap<Long,Collection<Room>>();
		int i = 0;
		for(Entry<Long, Building> entry: distanceService.getDistances(location, destinations).entrySet()){
			map.put(entry.getKey(), new ArrayList<Collection<Room>>(roomMap.values()).get(i));
			i++;
		}
		return map;
	}
}
