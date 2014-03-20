package com.htwk.app.repository;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

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
import com.google.common.collect.ArrayListMultimap;
import com.google.gson.Gson;
import com.htwk.app.model.impl.Day;
import com.htwk.app.model.room.Room;
import com.htwk.app.model.timetable.Subject;
import com.htwk.app.repository.helper.impl.RoomPlanConverter;
import com.htwk.app.repository.helper.impl.TimetableConverter;

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
	private Gson gson = null;

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

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.add("Content-Type", "text/xml;charset=UTF-8");
		conv = new RoomPlanConverter();
		timetableConv = new TimetableConverter();
		timeParser = new SimpleDateFormat("HH:mm");
		gson = new Gson();
		cache = (Cache<String, Object>) cacheManager.getCache("timeCache").getNativeCache();
	}

	public ArrayListMultimap<String, Room> getRooms(String semester) throws UnsupportedEncodingException {
		roomPlanRoomList = MessageFormat.format(roomPlanRoomList, semester);

		response = restTemplate.exchange(roomPlanUrl + roomPlanRoomList, HttpMethod.GET,
				new HttpEntity<Object>(headers), String.class);
		if (response.hasBody()) {
			return conv.getRoomList(response.getBody());
		}
		return null;
	}

	public List<Day<Subject>> getRoomPlan(String semester, String roomId, String kw, String day) throws IOException,
			URISyntaxException {
		List<Day<Subject>> plan = (List<Day<Subject>>) cache.getIfPresent(kw + day + roomId);
		if (plan != null) {
			return plan;
		}

		Map<String, String> cal = timetableRepo.getCalendar();
		if (!cal.containsKey(kw)) {
			return null;
		}
		URI uri = new URI(roomPlanUrl + MessageFormat.format(roomReport, semester, roomId, kw, day));
		logger.info("uri:" + uri.toString());
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

	public synchronized final Map<String, Collection<Room>> getFreeRoom(String semester, String kw) throws IOException,
			URISyntaxException, ParseException {
		Calendar cal = Calendar.getInstance(Locale.GERMAN);
		int calDay = (cal.get(Calendar.DAY_OF_WEEK) - 1) % 7;
		String now = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
		Date userDate = timeParser.parse(now);

		ArrayListMultimap<String, Room> roomMap = getRooms(semester);
		ArrayListMultimap<String, Room> whiteListedRooms = ArrayListMultimap.create();

		for (Entry<String, Collection<Room>> geb : roomMap.asMap().entrySet()) {
			boolean add = false;
			for (Room room : geb.getValue()) {
				Iterator<Day<Subject>> plan = getRoomPlan(semester, room.getId(), kw, "" + calDay).iterator();
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
}
