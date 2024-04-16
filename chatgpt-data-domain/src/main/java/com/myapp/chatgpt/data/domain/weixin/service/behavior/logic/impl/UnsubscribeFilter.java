package com.myapp.chatgpt.data.domain.weixin.service.behavior.logic.impl;

import com.myapp.chatgpt.data.domain.weixin.model.entity.BehaviorMatter;
import com.myapp.chatgpt.data.domain.weixin.service.behavior.logic.LogicFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @description: 处理取消订阅行为的过滤器
 * @author: 云奇迹
 * @date: 2024/3/20
 */
@Service("unsubscribe")
public class UnsubscribeFilter implements LogicFilter {

    private final Logger logger = LoggerFactory.getLogger(UnsubscribeFilter.class);

    @Override
    public String filter(BehaviorMatter behaviorMatter) {
        logger.info("微信公众号 用户{},取消关注",behaviorMatter.getOpenId());
        return null;
    }
}
