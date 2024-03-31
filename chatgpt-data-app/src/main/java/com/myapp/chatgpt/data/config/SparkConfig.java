package com.myapp.chatgpt.data.config;

import com.myapp.spark.session.SparkAiSession;
import com.myapp.spark.session.defaults.DefaultSparkAiSessionFactory;
import com.myapp.spark.session.defaults.SparkContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 星火大模型配置类
 * @author: 云奇迹
 * @date: 2024/3/28
 */
@Configuration
@EnableConfigurationProperties(SparkConfigProperties.class)
public class SparkConfig {

    @Bean(name = "sparkOpenAiSession")
    @ConditionalOnProperty(value = "spark.config.enable", havingValue = "true",matchIfMissing = false)
    public SparkAiSession sparkAiSession(SparkConfigProperties properties) {
        // 1. 配置文件
        SparkContext sparkContext = new SparkContext();
        sparkContext.setAPP_ID(properties.getAppid());
        sparkContext.setAPI_KEY(properties.getApiKey());
        sparkContext.setAPI_SECRET(properties.getApiSecret());
        // 2.会话工厂
        DefaultSparkAiSessionFactory factory = new DefaultSparkAiSessionFactory(sparkContext);
        // 3.开启会话
        SparkAiSession session = factory.openSession();
        return session;
    }


}
