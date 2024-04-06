package com.myapp.chatgpt.data.config;

import com.myapp.chatgpt.data.aop.RateLimiterAOP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 登录频次 AOP 配置类
 * @author: 云奇迹
 * @date: 2024/4/6
 */
@Configuration
@Slf4j
public class RateLimiterAOPConfig {

    @Bean
    public RateLimiterAOP rateLimiter() {
        return new RateLimiterAOP();
    }

}
