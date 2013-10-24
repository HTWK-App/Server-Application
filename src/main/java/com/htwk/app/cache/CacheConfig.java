package com.htwk.app.cache;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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

	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();

		Cache<Object, Optional<Object>> tenMinutesCache = CacheBuilder.newBuilder()
				.expireAfterWrite(10, TimeUnit.MINUTES).build();

		Cache<Object, Optional<Object>> maxSizeCache = CacheBuilder.newBuilder().maximumSize(10).build();

		cacheManager.setCaches(Arrays.asList(new GuavaCache("tenMinutesCache", tenMinutesCache), new GuavaCache(
				"maxSizeCache", maxSizeCache)));

		return cacheManager;
	}

}