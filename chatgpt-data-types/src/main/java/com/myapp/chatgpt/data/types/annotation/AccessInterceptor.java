package com.myapp.chatgpt.data.types.annotation;

import java.lang.annotation.*;

/**
 * @description: 访问拦截注解
 * @author: 云奇迹
 * @date: 2024/4/6
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AccessInterceptor {

    /** 用哪个字段作为拦截标识，未配置则默认走全部 */
    String key() default "all";
    /** 限制频次(每秒请求次数) */
    double permitsPerSecond();
    /** 黑名单拦截(多少次限制后加入黑名单) 0 - 不限制 */
    double blacklistCount();
    /** 拦截后的执行方法 */
    String fallbackMethod();


}
