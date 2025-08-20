package org.kwakmunsu.dingdongpang.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.registerCustomCache("weekly-subscriptions",
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(7, TimeUnit.DAYS)
                        .recordStats()
                        .build()
        );

        return cacheManager;
    }

}