package com.htwk.app.cache;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.common.cache.CacheBuilder;

@Configuration
@EnableCaching
@ComponentScan(basePackages = {"com.htwk.app"})
public class CacheConfig {

  @Value("${cache.expTime}")
  private Long time;

  @Value("${cache.expTimeUnit}")
  private String timeUnit;


  @Bean
  public CacheManager cacheManager() {
    SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
    GuavaCache timeCache =
        new GuavaCache("timeCache", CacheBuilder.newBuilder()
            .expireAfterAccess(time, TimeUnit.valueOf(timeUnit)).build());
    GuavaCache maxSizeCache =
        new GuavaCache("maxSizeCache", CacheBuilder.newBuilder().maximumSize(10).build());
    GuavaCache staffCache =
        new GuavaCache("staffCache", CacheBuilder.newBuilder()
            .expireAfterAccess(time, TimeUnit.valueOf(timeUnit)).build());
    GuavaCache sportCache =
        new GuavaCache("sportCache", CacheBuilder.newBuilder()
            .expireAfterAccess(time, TimeUnit.valueOf(timeUnit)).build());
    GuavaCache buildingCache =
        new GuavaCache("buildingCache", CacheBuilder.newBuilder()
            .expireAfterAccess(time, TimeUnit.valueOf(timeUnit)).build());

    simpleCacheManager.setCaches(Arrays.asList(timeCache, maxSizeCache, staffCache, sportCache,
        buildingCache));
    return simpleCacheManager;
  }


}
