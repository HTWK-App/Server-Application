package com.htwk.app.warmup.worker;

import java.util.Calendar;
import java.util.Map;

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
		for (String kw : cal.keySet()) {
			for (int day = 1; day <= 7; day++) {
				repo.getFreeRoom(semester, kw, day);
			}
		}
		logger.debug("warmed up RoomPlanRepository");

	}
}
