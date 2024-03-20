package com.myapp.chatgpt.data.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @description: 全局的缓存
 * @author: 云奇迹
 * @date: 2024/3/18
 */
@Configuration
public class GlobalGuavaCacheConfig {

    @Bean
    public Cache<String, String> codeCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .build();
    }

    @Bean
    public Cache<String, Integer> visitCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(12, TimeUnit.HOURS)
                .build();
    }

    /**
     * 访问频率的缓存,一分钟过期
     * @return
     */
    @Bean
    public Cache<String,Integer> frequencytCache(){
        return CacheBuilder.newBuilder()
                .expireAfterWrite(1,TimeUnit.MINUTES)
                .build();
    }
}
