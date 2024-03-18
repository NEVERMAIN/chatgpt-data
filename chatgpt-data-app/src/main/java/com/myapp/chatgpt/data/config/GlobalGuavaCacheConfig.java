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
    public Cache<String,String> codeCache(){
       return  CacheBuilder.newBuilder()
               .expireAfterWrite(3,TimeUnit.MINUTES)
               .build();
    }
}
