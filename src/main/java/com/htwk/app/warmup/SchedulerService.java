package com.htwk.app.warmup;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Qualifier("warmUpService")
public class SchedulerService implements Processor {

	private static final Logger logger = LoggerFactory
			.getLogger(SchedulerService.class);
	
	@Autowired
	@Qualifier("informationWarmUp")
	private WarmUp informationWarmUp;
	
	@Autowired
	@Qualifier("roomPlanWarmUp")
	private WarmUp roomPlanWarmUp;


	@Scheduled(cron = "${processor.warmup.cron}")
	public void process() throws Exception {
		informationWarmUp.warmUp();
		roomPlanWarmUp.warmUp();
	}

}
