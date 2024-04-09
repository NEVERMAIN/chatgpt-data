package com.myapp.chatgpt.data.infrastructure.repository;

import com.myapp.chatgpt.data.domain.atuth.login.repository.ILoginRepository;
import com.myapp.chatgpt.data.infrastructure.redis.IRedisService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @description: 登录鉴权的仓储服务
 * @author: 云奇迹
 * @date: 2024/4/9
 */
@Repository
public class LoginRepository implements ILoginRepository {

    @Resource
    private IRedisService redisService;

    private static final String WECHAT_LOGIN_SCAN_KEY = "wechat_login_scan_key";

    @Override
    public String checkLogin(String ticket) {
        // 1.从缓存中获取
        return redisService.getValue(WECHAT_LOGIN_SCAN_KEY + "_" + ticket);
    }
}
