package com.myapp.chatgpt.data.domain.weixin.service.behavior.logic.impl;

import com.google.common.cache.Cache;
import com.myapp.chatgpt.data.domain.weixin.model.entity.BehaviorMatter;
import com.myapp.chatgpt.data.domain.weixin.repository.IWeiXinRepository;
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

    private final Logger logger = LoggerFactory.getLogger(ValidCodeFilter.class);

    @Resource
    private IWeiXinRepository weiXinRepository;

    @Override
    public String filter(BehaviorMatter behaviorMessage) {
        // 1.获取唯一ID
        String openId = behaviorMessage.getOpenId();
        // 生成验证码
        String code = weiXinRepository.getCode(openId);
        return String.format("您的验证码是: " + code + " 有效期为3分钟");
    }
}
