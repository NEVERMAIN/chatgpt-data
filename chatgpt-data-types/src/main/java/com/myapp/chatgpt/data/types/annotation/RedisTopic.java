package com.myapp.chatgpt.data.types.annotation;

import java.lang.annotation.*;

/**
 * @description: Redis 消息订阅服务的注解
 * @author: 云奇迹
 * @date: 2024/4/1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface RedisTopic {

    String topic() default "";
}
