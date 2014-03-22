package com.htwk.app.warmup.worker;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;

import javax.management.InvalidAttributeValueException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.htwk.app.repository.RoomPlanRepository;
import com.htwk.app.repository.TimetableRepository;
import com.htwk.app.warmup.WarmUp;

@Component
@Qualifier("roomPlanWarmUp")
public class RoomPlanWarmUp implements WarmUp {

	private static final Logger logger = LoggerFactory.getLogger(RoomPlanWarmUp.class);

	@Autowired
	private RoomPlanRepository repo;

	@Autowired
	private TimetableRepository timetableRepo;

	@Override
	@Async
	public void warmUp() throws Exception {
		Map<String, String> cal = timetableRepo.getCalendar();
		String semester = cal.get("semester");
		repo.getRooms(semester);
		int kw = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
//		for (String kw : cal.keySet()) {
			for (int day = 1; day <= 7; day++) {
				warmUpFreeRooms(semester, ""+kw, day);
				logger.info(""+semester+ ":"+kw+ ":"+ day);
			}
//		}
		logger.debug("warmed up RoomPlanRepository");

	}

	@Async
	public void warmUpFreeRooms(String semester, String kw, int day) throws InvalidAttributeValueException,
			IOException, URISyntaxException, ParseException {
		repo.getFreeRoom(semester, kw, day);
	}
}
