package com.htwk.app.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.htwk.app.model.info.News;
import com.htwk.app.repository.NewsRepository;

@Controller
@RequestMapping(value = "/news")
public class NewsController {

	private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

	@Autowired
	private NewsRepository repo;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody
	List<News> home() throws UnsupportedEncodingException {
		return repo.getNews();
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/news";
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public @ResponseBody
	String home(@RequestParam(value = "feed", required = false, defaultValue = "") String key,
			@RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
			@RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {
		return new Gson().toJson(repo.getNewsFeed(key, limit, offset));
	}
}
