package com.htwk.app.warmup.worker;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.gson.Gson;
import com.htwk.app.model.info.Building;
import com.htwk.app.model.info.Sport;
import com.htwk.app.model.info.Staff;
import com.htwk.app.model.info.StaffShort;
import com.htwk.app.repository.InformationRepository;
import com.htwk.app.warmup.WarmUp;

@Component
@Qualifier("informationWarmUp")
public class InformationWarmUp implements WarmUp {

	private static final Logger logger = LoggerFactory.getLogger(InformationWarmUp.class);

	@Autowired
	private InformationRepository repo;

	@Autowired
	CacheManager cacheManager;

	Cache<String, Staff> staffCache = null;
	Cache<String, Building> buildingCache = null;
	Cache<String, Sport> sportCache = null;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		staffCache = (Cache<String, Staff>) cacheManager.getCache("staffCache").getNativeCache();
		buildingCache = (Cache<String, Building>) cacheManager.getCache("buildingCache").getNativeCache();
		sportCache = (Cache<String, Sport>) cacheManager.getCache("sportCache").getNativeCache();
	}

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
		logger.info("finished warmUp for staff");

	}

	@Async
	private void warmUpBuildings() throws Exception {
		repo.getBuildings();
		logger.info("finished warmUp for buildings");
	}

	@Async
	private void warmUpSport() throws Exception {
		repo.getSport();
		logger.info("finished warmUp for sport");

	}

}
