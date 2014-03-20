package com.htwk.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.cache.Cache;
import com.htwk.app.model.info.Staff;
import com.htwk.app.warmup.SchedulerService;

@Controller
@RequestMapping(value = "/warmup")
public class WarmUpController {

	private static final Logger logger = LoggerFactory.getLogger(WarmUpController.class);

	@Autowired
	@Qualifier("warmUpService")
	private SchedulerService service;
	
	@Autowired
	CacheManager cacheManager;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public @ResponseBody String home() throws Exception {
		service.process();
		return "home";
	}

	@RequestMapping(value = "/stats", method = RequestMethod.GET)
	public @ResponseBody String cacheStats() throws Exception {
		String response = "";
		Cache c = (Cache<String, Staff>) cacheManager.getCache("staffCache").getNativeCache();
		response +="staffCache"+">>"+c.stats();
		response += "<br/>";
		
		c = (Cache<String, Staff>) cacheManager.getCache("sportCache").getNativeCache();
		response +="sportCache"+">>"+c.stats();
		response += "<br/>";
		
		c = (Cache<String, Staff>) cacheManager.getCache("buildingCache").getNativeCache();
		response +="buildingCache"+">>"+c.stats();
		response += "<br/>";
		
		c = (Cache<String, Staff>) cacheManager.getCache("timeCache").getNativeCache();
		response +="timeCache"+">>"+c.stats();
		response += "<br/>";
		return response;
	}
}
