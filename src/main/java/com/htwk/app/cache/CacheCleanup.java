package com.htwk.app.cache;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheCleanup {

	private static final Logger logger = LoggerFactory.getLogger(CacheCleanup.class);

	private ScheduledExecutorService scheduler;

	@Value("${cache.initialDelay}")
	private String initialDelay;

	@Value("${cache.delay}")
	private String delay;

	@Value("${cache.timeUnit}")
	private String timeUnit;

	@Autowired
	CacheManager cacheManager;

	@PreDestroy
	public void shutdown() {
		scheduler.shutdown();
	}

	@PostConstruct
	public void cacheCleanup() {
		scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				for (String cacheName : cacheManager.getCacheNames()) {
					logger.trace("{} : cleanup of Cache:{}", new Date(), cacheName);
					((GuavaCache) cacheManager.getCache(cacheName)).getNativeCache().cleanUp();
				}
			}
		}, new Long(initialDelay), new Long(delay), TimeUnit.valueOf(timeUnit));

	}
}