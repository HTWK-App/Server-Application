package com.htwk.app.cache;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Configuration
@EnableCaching
@ComponentScan(basePackages = { "com.htwk.app" })
public class CacheConfig {

	@Value("${cache.time.time}")
	private long time;

	@Value("${cache.time.timeUnit}")
	private String timeUnit;

	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();

		Cache<Object, Optional<Object>> timeCache = CacheBuilder.newBuilder()
				.expireAfterWrite(time, TimeUnit.valueOf(timeUnit)).build();
		Cache<Object, Optional<Object>> staffCache = CacheBuilder.newBuilder()
				.expireAfterWrite(time, TimeUnit.valueOf(timeUnit)).build();
		Cache<Object, Optional<Object>> buildingCache = CacheBuilder.newBuilder()
				.expireAfterWrite(time, TimeUnit.valueOf(timeUnit)).build();

		Cache<Object, Optional<Object>> maxSizeCache = CacheBuilder.newBuilder().maximumSize(10).build();

		cacheManager.setCaches(Arrays
				.asList(new GuavaCache("timeCache", timeCache), new GuavaCache("maxSizeCache", maxSizeCache),
						new GuavaCache("staffCache", staffCache), new GuavaCache("buildingCache", buildingCache)));

		return cacheManager;
	}

}