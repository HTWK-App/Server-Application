package com.htwk.app.warmup.worker;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.cache.Cache;
import com.htwk.app.model.info.Building;
import com.htwk.app.model.info.Sport;
import com.htwk.app.model.info.Staff;
import com.htwk.app.repository.InformationRepository;
import com.htwk.app.repository.helper.impl.InformationConverter;
import com.htwk.app.warmup.WarmUp;

@Component
@Qualifier("informationWarmUp")
public class InformationWarmUp implements WarmUp {

	private static final Logger logger = LoggerFactory.getLogger(InformationWarmUp.class);

	@Autowired
	private InformationRepository repo;

	@Override
	@Async
	public void warmUp() throws Exception {
		warmUpStaff();
		warmUpBuildings();
		warmUpSport();
		logger.debug("warmed up InformationRepository");

	}

	@Async
	private void warmUpStaff() throws Exception {
		repo.getStaff();
	}
	

	@Async
	private void warmUpBuildings() throws Exception {
		repo.getBuildings();
	}
	

	@Async
	private void warmUpSport() throws Exception {
		repo.getSport();
	}

}
