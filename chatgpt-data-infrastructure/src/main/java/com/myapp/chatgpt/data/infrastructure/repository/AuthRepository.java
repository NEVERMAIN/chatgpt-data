package com.myapp.chatgpt.data.infrastructure.repository;

import com.myapp.chatgpt.data.domain.atuth.repository.IAuthRepository;
import com.myapp.chatgpt.data.infrastructure.redis.IRedisService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/4/1
 */
@Repository
public class AuthRepository implements IAuthRepository {

    private static final String key = "wechat_code";

    @Resource
    private IRedisService redisService;

    @Override
    public String getCodeByOpenId(String code) {
        return redisService.getValue(key+"_"+code);
    }

    @Override
    public void removeCodeByOpenId(String code, String openId) {
        redisService.remove(key+"_"+code);
        redisService.remove(key+"_"+openId);
    }
}
