package com.htwk.app;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.cache.Cache;
import com.htwk.app.model.info.Building;
import com.htwk.app.model.info.Sport;
import com.htwk.app.model.info.Staff;
import com.htwk.app.service.UpdateService;

@Controller
@RequestMapping(value = "/admin")
public class AdministrationController {

	@Autowired
	private UpdateService updateService;

	@Autowired
	CacheManager cacheManager;
	
	Cache<String, Staff> staffCache = null;
	Cache<String, Building> buildingCache = null;
	Cache<String, Sport> sportCache = null;
	
	@PostConstruct
	public void init() {
		staffCache = (Cache<String, Staff>) cacheManager.getCache("staffCache").getNativeCache();
		buildingCache = (Cache<String, Building>) cacheManager.getCache("buildingCache").getNativeCache();
		sportCache = (Cache<String, Sport>) cacheManager.getCache("sportCache").getNativeCache();
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "home";
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String users(Locale locale, Model model) {
		model.addAttribute("users", updateService.getRegisteredUsers());
		return "users";
	}

	@RequestMapping(value = "/staff", method = RequestMethod.GET)
	public String staff(Locale locale, Model model) {
		model.addAttribute("staff", staffCache.asMap());
		return "staff";
	}

	@RequestMapping(value = "/staff/{cuid}", method = RequestMethod.GET)
	public String staff(Locale locale, Model model, @PathVariable String cuid) {
		model.addAttribute("member", null);
		return "staff";
	}
}
