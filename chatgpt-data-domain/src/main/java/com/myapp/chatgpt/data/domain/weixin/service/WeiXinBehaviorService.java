package com.myapp.chatgpt.data.domain.weixin.service;

import com.google.common.cache.Cache;
import com.myapp.chatgpt.data.domain.weixin.model.UserBehaviorMessageEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.SecureRandom;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/18
 */
@Slf4j
@Service
public class WeiXinBehaviorService implements IWeiXinBehaviorService {

    @Resource
    private Cache<String,String> codeCache;

    @Override
    public String post(UserBehaviorMessageEntity behaviorMessage) {
        /**
         * 3. 判断该用户的验证码是否存在
         *   a. 如果缓存中存在验证码，直接返回验证码
         *   b. 如果缓存中不存在验证码，生成验证码，放到缓存中
         */
        String openId = behaviorMessage.getOpenId();
        // 从缓存中获取验证码
        String code = codeCache.getIfPresent(openId);
        if(StringUtils.isBlank(code)){
            // 生成 4 位随机数
            code = String.valueOf(new SecureRandom().nextInt(9000) + 1000);
            // 放入缓存中
            codeCache.put(openId,code);
            codeCache.put(code,openId);
        }

        return code;
    }
}
