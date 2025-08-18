package com.greencat.pre_project.infrastructure.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

  // 운영에선 Redis 사용 가능
  @Bean
  public CacheManager caffeineCacheManager() {
    var cm = new CaffeineCacheManager("todoAllCache");
    cm.setCaffeine(com.github.benmanes.caffeine.cache.Caffeine.newBuilder()
        .maximumSize(10_000)
        .expireAfterWrite(java.time.Duration.ofMinutes(10))
        .recordStats());
    return cm;
  }
}
