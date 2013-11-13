package com.htwk.app.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.htwk.app.repository.NewsRepository;

@Controller
@RequestMapping(value = "/news")
public class NewsController {

	private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

	@Autowired
	private NewsRepository repo;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	Map<String, String> home() {
		return repo.getNewsCategories();
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/news";
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String home(@RequestParam(value = "feed", required = false, defaultValue = "") String key) {
		return repo.getNewsFeed(key);
	}
}
