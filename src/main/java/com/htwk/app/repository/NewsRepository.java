package com.htwk.app.repository;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class NewsRepository {

	private static final Logger logger = LoggerFactory.getLogger(InformationRepository.class);

	private RestTemplate restTemplate = null;

	@Autowired
	CacheManager cacheManager;

	@Autowired
	private ApplicationContext context;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
	}

	public Map<String, String> getNewsCategories() {
		return new TreeMap<String, String>((Map<String, String>) context.getBean("rssMap"));

	}

	public String getNewsFeed(String key) {
		Map<String, String> map = getNewsCategories();
		String url = "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=";
		if (key == null || key.isEmpty() || !map.containsKey(key)) {
			url+=map.get("rss.htwk.4"); //news feed 
		} else {
			url +=map.get(key);
		}
		return restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody().toString();
	}
}
