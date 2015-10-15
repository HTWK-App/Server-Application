package com.htwk.app.repository;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.naming.directory.InvalidAttributesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.htwk.app.config.RssMapProperties;
import com.htwk.app.model.info.News;

@Repository
public class NewsRepository {

  private static Logger logger = LoggerFactory.getLogger(NewsRepository.class);

  private RestTemplate restTemplate = null;
  private JsonParser parser = new JsonParser();

  @Autowired
  CacheManager cacheManager;

  @Autowired
  private ApplicationContext context;

  @Autowired
  private RssMapProperties props;

  @PostConstruct
  public void init() {
    restTemplate = new RestTemplate();
    logger.debug("initialized NewsRepository");
  }

  @SuppressWarnings("unchecked")
  private Map<String, String> getNewsCategories() {
    // fix problems with missing rss.* for the keys
    TreeMap<String, String> rssMap = new TreeMap<String, String>();
    for (Entry<String, String> element : props.getRss().entrySet()) {
      rssMap.put("rss." + element.getKey(), element.getValue());
    }
    return rssMap;
  }

  public List<News> getNews() throws UnsupportedEncodingException, InvalidAttributesException {
    Map<String, String> rssMap = getNewsCategories();

    List<News> news = new ArrayList<News>();
    for (Entry<String, String> entry : rssMap.entrySet()) {

      JsonObject response = getNewsFeed(entry.getKey(), 1, 0);
      int status = response.get("responseStatus").getAsInt();

      if (response != null && response.has("responseData")) {
        response = response.get("responseData").getAsJsonObject();
      }

      News newsFeed = new News();
      newsFeed.setId(entry.getKey());
      newsFeed.setLink(entry.getValue());

      String title = null;
      if (status == 200 && response != null && response.has("feed")) {
        title = response.get("feed").getAsJsonObject().get("title").getAsString();
        title =
            (!title.isEmpty()) ? title : response.get("feed").getAsJsonObject().get("description")
                .getAsString();
      }

      newsFeed.setTitle("" + title);
      news.add(newsFeed);
    }

    return news;
  }

  public JsonObject getNewsFeed(String key, int limit, int offset)
      throws InvalidAttributesException {
    limit = limit + offset;
    JsonObject response = parser.parse(getFeed(key, limit, offset)).getAsJsonObject();
    JsonObject responseData = null;
    int status = response.get("responseStatus").getAsInt();
    if (status == 200 && response != null && response.has("responseData")
        && response.get("responseData") != null) {
      responseData = response.get("responseData").getAsJsonObject();
    } else {
      throw new InvalidAttributesException("the given feed-key was not found");
    }

    if (offset > 0) {
      if (status == 200 && responseData != null && responseData.has("feed")) {
        JsonObject feed = responseData.get("feed").getAsJsonObject();
        JsonArray entries = feed.get("entries").getAsJsonArray();
        JsonArray responseEntries = new JsonArray();
        for (int i = 0; i < entries.size(); i++) {
          if (i >= offset) {
            responseEntries.add(entries.get(i));
          }
        }
        feed.add("entries", responseEntries);
      }
    }
    response.add("responseData", responseData);
    return response;
  }

  private String getFeed(String key, int limit, int offset) {
    Map<String, String> map = getNewsCategories();
    String url = "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=";
    if (key == null || key.isEmpty() || !map.containsKey(key)) {
      url += map.get("rss.htwk.4"); // news feed
    } else {
      url += map.get(key);
    }
    url += "&num=" + limit;
    return restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody().toString();
  }
}
