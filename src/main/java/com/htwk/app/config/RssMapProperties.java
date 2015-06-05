package com.htwk.app.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@EnableConfigurationProperties
//@ConfigurationProperties
public class RssMapProperties {

  private static final Logger logger = LoggerFactory.getLogger(RssMapProperties.class);

//  @Value("${rss}")
  private Map<String, Object> rssMap;

  public RssMapProperties() {}

  @PostConstruct
  public void init() {
    logger.info(getRssMap().toString());
  }

  public Map<String, Object> getRssMap() {
    return rssMap;
  }

  public void setRssMap(Map<String, Object> rssMap) {
    if(rssMap == null ){
      rssMap = new HashMap<String, Object>();
    }
    this.rssMap = rssMap;
  }



}
