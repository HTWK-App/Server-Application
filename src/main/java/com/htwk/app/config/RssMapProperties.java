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
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties()
public class RssMapProperties {

    private HashMap<String, String> rss;

    public HashMap<String, String> getRss() {
        return rss;
    }

    public void setRss(HashMap<String, String> rss) {
        this.rss = rss;
    }

}
