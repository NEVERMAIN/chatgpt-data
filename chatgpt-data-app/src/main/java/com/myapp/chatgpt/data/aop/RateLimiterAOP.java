package com.myapp.chatgpt.data.aop;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import com.myapp.chatgpt.data.infrastructure.redis.IRedisService;
import com.myapp.chatgpt.data.types.annotation.AccessInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import javax.annotation.RegEx;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @description: 登录方法的 AOP 拦截
 * @author: 云奇迹
 * @date: 2024/4/6
 */
@Slf4j
@Aspect
public class RateLimiterAOP {

    /**
     * 个人限频记录1分钟
     */
    private final Cache<String, RateLimiter> loginRecord = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    @Resource
    private IRedisService redisService;

    private static final String BLACK_LIST = "black_list";


    @Pointcut("@annotation(com.myapp.chatgpt.data.types.annotation.AccessInterceptor)")
    public void aopPoint() {
    }

    /**
     * 环绕通知
     *
     * @param joinPoint         切入点
     * @param accessInterceptor 拦截器注解
     * @return
     * @throws Throwable
     */
    @Around("aopPoint() && @annotation(accessInterceptor)")
    public Object doRouter(ProceedingJoinPoint joinPoint, AccessInterceptor accessInterceptor) throws Throwable {

        String key = accessInterceptor.key();
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("annotation RateLimiter key is null");
        }

        // 获取拦截字段
        String keyAttr = getAttrValue(key, joinPoint.getArgs());
        log.info("aop attr:{}", keyAttr);

        if (!"all".equals(keyAttr) && accessInterceptor.blacklistCount() != 0 && null != redisService.<Long>getValue(BLACK_LIST + "_" + keyAttr) && redisService.<Long>getValue(BLACK_LIST + "_" + keyAttr) > accessInterceptor.blacklistCount()) {
            log.info("限流-黑名单拦截(24h)：{}", keyAttr);
            return fallbackMethodResult(joinPoint, accessInterceptor.fallbackMethod());
        }

        // 获取限流 -> Guava 缓存1分钟
        RateLimiter rateLimiter = loginRecord.getIfPresent(keyAttr);
        if (null == rateLimiter) {
            rateLimiter = RateLimiter.create(accessInterceptor.permitsPerSecond());
            loginRecord.put(keyAttr, rateLimiter);
        }

        // 限流拦截
        if (!rateLimiter.tryAcquire()) {
            if (accessInterceptor.blacklistCount() != 0) {
                if (null == redisService.<Long>getValue(BLACK_LIST + "_" + keyAttr)) {
                    log.info("now:");
                    redisService.setValue(BLACK_LIST + "_" + keyAttr, 1L, 24 * 60 * 60 * 1000);
                } else {
                    Long count = redisService.<Long>getValue(BLACK_LIST + "_" + keyAttr);
                    log.info("add count:{}", count);
                    redisService.setValue(BLACK_LIST + "_" + keyAttr, count + 1L, 24 * 60 * 60 * 1000);
                }
            }
            log.info("限流-超频次拦截：{}", keyAttr);
            return fallbackMethodResult(joinPoint, accessInterceptor.fallbackMethod());
        }

        // 返回结果
        return joinPoint.proceed();

    }

    /**
     * 调用用户配置的回调方法，当拦截后，返回回调结果。
     *
     * @param jp
     * @param fallbackMethod
     * @return
     * @throws Exception
     */
    private Object fallbackMethodResult(JoinPoint jp, String fallbackMethod) throws Exception {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        Method method = jp.getTarget().getClass().getMethod(fallbackMethod, methodSignature.getParameterTypes());
        return method.invoke(jp.getThis(), jp.getArgs());
    }

    /**
     * 获取方法对象
     *
     * @param jp
     * @return
     * @throws NoSuchMethodException
     */
    private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }

    /**
     * 实际根据自身业务调整，主要是为了获取通过某个值做拦截
     *
     * @param attr
     * @param args
     * @return
     */
    public String getAttrValue(String attr, Object[] args) {
        if (args[0] instanceof String) {
            return args[0].toString();
        }

        String filedValue = null;
        for (Object arg : args) {
            try {
                if (StringUtils.isNotBlank(filedValue)) {
                    break;
                }
                //fix: 使用 lombok 时，uId这种字段的get方法与idea生成的get方法不同，会导致获取不到属性值，改成反射获取解决
                filedValue = String.valueOf(this.getValueByName(arg, attr));
            } catch (Exception e) {
                log.error("获取路由属性值是失败 attr:{}", attr, e);
            }
        }
        return filedValue;

    }

    /**
     * 获取对象的特定属性值
     *
     * @param item 对象
     * @param name 属性名
     * @return 属性值
     * @author tang
     */
    private Object getValueByName(Object item, String name) {
        try {
            Field field = getFieldByName(item, name);
            if (field == null) {
                return null;
            }
            field.setAccessible(true);
            Object o = field.get(item);
            field.setAccessible(false);
            return o;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * 根据名称获取方法，该方法同时兼顾继承类获取父类的属性
     *
     * @param item 对象
     * @param name 属性名
     * @return 该属性对应方法
     * @author tang
     */
    private Field getFieldByName(Object item, String name) {
        try {
            Field field;
            try {
                field = item.getClass().getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                field = item.getClass().getSuperclass().getDeclaredField(name);
            }
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

}
