package com.htwk.app;

import java.io.IOException;
import java.util.Locale;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.cache.Cache;
import com.htwk.app.model.info.Building;
import com.htwk.app.model.info.Sport;
import com.htwk.app.model.info.Staff;
import com.htwk.app.repository.StatisticRepository;
import com.htwk.app.service.UpdateService;

@Controller
@RequestMapping(value = "/admin")
public class AdministrationController {

	@Autowired
	private UpdateService updateService;

	@Autowired
	CacheManager cacheManager;

	@Autowired
	private StatisticRepository stats;

	Cache<String, Staff> staffCache = null;
	Cache<String, Building> buildingCache = null;
	Cache<String, Sport> sportCache = null;

	@PostConstruct
	public void init() {
		staffCache = (Cache<String, Staff>) cacheManager.getCache("staffCache").getNativeCache();
		buildingCache = (Cache<String, Building>) cacheManager.getCache("buildingCache").getNativeCache();
		sportCache = (Cache<String, Sport>) cacheManager.getCache("sportCache").getNativeCache();
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String redirectIndex(Locale locale, Model model) {
		return "redirect:/admin/";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		model.addAttribute("staff", stats.getStaffRequests());
		model.addAttribute("building", stats.getBuildingRequests());
		model.addAttribute("sport", stats.getSportRequests());
		model.addAttribute("news", stats.getNewsRequests());
		model.addAttribute("mensa", stats.getMensaRequests().asMap());
		long mensaTotal = 0;
		for (Entry<Integer, Long> entry : stats.getMensaRequests().asMap().entrySet()) {
			mensaTotal += entry.getValue();
		}
		model.addAttribute("mensaTotal", mensaTotal);
		model.addAttribute("mailbox", stats.getMailboxRequests());
		model.addAttribute("weather", stats.getWeatherRequests());
		model.addAttribute("timetable", stats.getTimetableRequests());
		model.addAttribute("errors", stats.getErrorRequests());
		model.addAttribute("types", stats.getRequestType().asMap());
		model.addAttribute("execution", stats.getExecutionTimes());
		model.addAttribute("total", stats.getTotalRequests());
		return "home";
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String users(Locale locale, Model model) {
		model.addAttribute("users", updateService.getRegisteredUsers());
		return "users";
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity test(@RequestParam String regId, Locale locale, Model model) throws MessagingException, IOException {
		return new ResponseEntity(updateService.sendPushRequest(regId), HttpStatus.OK);
	}

	@RequestMapping(value = "/staff", method = RequestMethod.GET)
	public String staff(Locale locale, Model model) {
		model.addAttribute("staff", staffCache.asMap());
		return "staff";
	}

	@RequestMapping(value = "/staff/{cuid}", method = RequestMethod.GET)
	public String staff(Locale locale, Model model, @PathVariable String cuid) {
		model.addAttribute("member", staffCache.asMap().get(cuid));
		return "staff";
	}
}
