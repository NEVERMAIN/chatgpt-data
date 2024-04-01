package com.myapp.chatgpt.data.infrastructure.repository;

import com.myapp.chatgpt.data.domain.weixin.repository.IWeiXinRepository;
import com.myapp.chatgpt.data.infrastructure.redis.IRedisService;
import jodd.util.RandomString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @description: 微信服务仓储
 * @author: 云奇迹
 * @date: 2024/4/1
 */
@Repository
@Slf4j
public class WeiXinRepository implements IWeiXinRepository {

    private static final String Key = "wechat_code";

    @Resource
    private IRedisService redisService;

    @Override
    public String getCode(String openId) {
        // 1. 获取值
        String isExistCode = redisService.getValue(Key + "_" + openId);
        if (StringUtils.isNotBlank(isExistCode)) return isExistCode;

        // 2.生成值
        //  2.1. 先获取锁,保证唯一处理
        RLock lock = redisService.getLock(Key);
        try {
            // 锁住 15s
            lock.lock(15, TimeUnit.SECONDS);

            // 2.2. 生成四位的验证码
            String code = RandomStringUtils.randomNumeric(4);
            // 防重校验&重新生成
            for (int i = 0; i < 10 && StringUtils.isNotBlank(redisService.getValue(Key + "_" + openId)); i++) {
                if (i < 3) {
                    code = RandomStringUtils.randomNumeric(4);
                } else if (i < 5) {
                    code = RandomStringUtils.randomNumeric(5);
                } else if (i < 9) {
                    code = RandomStringUtils.randomNumeric(6);
                    log.warn("验证码重复,生成6位验证码:{} openid:{}", code, openId);
                } else {
                    return "您的验证码获取失败，请重新回复验证码获取";
                }
            }

            // 存储值【有效期3分钟】
            redisService.setValue(Key + "_" + code, openId, 3 * 60 * 1000);
            redisService.setValue(Key + "_" + openId, code, 3 * 60 * 1000);

            return code;

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
