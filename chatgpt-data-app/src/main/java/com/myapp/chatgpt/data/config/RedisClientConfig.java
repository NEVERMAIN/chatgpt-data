package com.myapp.chatgpt.data.config;

import com.myapp.chatgpt.data.types.annotation.RedisTopic;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.config.Config;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: Redis 客户端配置
 * @author: 云奇迹
 * @date: 2024/4/1
 */
@Configuration
@EnableConfigurationProperties(RedisClientConfigProperties.class)
public class RedisClientConfig {

    @Bean("redissonClient")
    public RedissonClient redissonClient(ConfigurableApplicationContext applicationContext, RedisClientConfigProperties properties) {
        Config config = new Config();

        config.useSingleServer()
                .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
                .setPassword(properties.getPassword())
                .setConnectionPoolSize(properties.getPoolSize())
                .setConnectionMinimumIdleSize(properties.getMinIdleSize())
                .setIdleConnectionTimeout(properties.getIdleTimeout())
                .setConnectTimeout(properties.getConnectTimeout())
                .setRetryAttempts(properties.getRetryAttempts())
                .setRetryInterval(properties.getRetryInterval())
                .setPingConnectionInterval(properties.getPingInterval())
                .setKeepAlive(properties.isKeepAlive());

        RedissonClient redissonClient = Redisson.create(config);

        // 通过自定义注解，来完成动态监听和将对象动态注入到 Spring 容器中，让需要注入的属性，可以被动态注入。
        // 1.获取所有的 MessageListener 对象名字
        String[] beanNamesForType = applicationContext.getBeanNamesForType(MessageListener.class);
        for (String beanName : beanNamesForType) {
            // 2.获取 beanName 的实例对象
            MessageListener bean = applicationContext.getBean(beanName, MessageListener.class);
            // 3.判断这个对象上有没有 RedisTopic 这个注解
            Class<? extends MessageListener> aClass = bean.getClass();
            if (aClass.isAnnotationPresent(RedisTopic.class)) {
                RedisTopic redisTopic = aClass.getAnnotation(RedisTopic.class);

                RTopic topic = redissonClient.getTopic(redisTopic.topic());
                topic.addListener(String.class, bean);

                // 获取容器工厂
                ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
                beanFactory.registerSingleton(redisTopic.topic(), topic);

            }
        }

        return redissonClient;
    }


}
