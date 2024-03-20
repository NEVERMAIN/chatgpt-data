package com.myapp.chatgpt.data.domain.weixin.service.behavior.logic.impl;

import com.google.common.cache.Cache;
import com.myapp.chatgpt.data.domain.weixin.model.entity.BehaviorMatter;
import com.myapp.chatgpt.data.domain.weixin.service.behavior.logic.LogicFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 处理验证码的过滤器
 * @author: 云奇迹
 * @date: 2024/3/20
 */
@Service("validCode")
public class ValidCodeFilter implements LogicFilter {
    private Logger logger = LoggerFactory.getLogger(ValidCodeFilter.class);

    @Resource
    private Cache<String, String> codeCache;

    @Override
    public String fitler(BehaviorMatter behaviorMessage) {
        String openId = behaviorMessage.getOpenId();
        // 从缓存中获取验证码
        String isExistCode = codeCache.getIfPresent(openId);
        // 判断验证码是否存在 - 不考虑验证码重复问题
        if (StringUtils.isBlank(isExistCode)) {
            // 生成 4 位随机数
            String code = RandomStringUtils.randomNumeric(4);
            // 放入缓存中
            codeCache.put(openId, code);
            codeCache.put(code, openId);
            isExistCode = code;
        }

        return String.format("您的验证码是: " + isExistCode + " 有效期为3分钟");
    }
}
