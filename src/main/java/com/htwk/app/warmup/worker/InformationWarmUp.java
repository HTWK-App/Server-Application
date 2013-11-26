package com.htwk.app.warmup.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.htwk.app.repository.InformationRepository;
import com.htwk.app.warmup.WarmUp;

@Component
@Qualifier("informationWarmUp")
public class InformationWarmUp implements WarmUp {

	private static final Logger logger = LoggerFactory.getLogger(InformationWarmUp.class);

	RestTemplate restTemplate = null;

	@Autowired
	private InformationRepository repo;

	@Override
	@Async
	public void warmUp() throws Exception {
		// for(Staff staff:repo.getStaff()){
		// repo.getStaffDetailed(staff.getCuid());
		// }
		repo.getStaff();
		repo.getBuildings();
		repo.getSport();
		logger.debug("warmed up InformationRepository");

	}
}
