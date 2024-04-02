package com.myapp.chatgpt.data.domain.weixin.service.behavior.logic.impl;

import com.myapp.chatgpt.data.domain.weixin.model.entity.BehaviorMatter;
import com.myapp.chatgpt.data.domain.weixin.service.behavior.logic.LogicFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @description: 处理订阅行为的过滤器
 * @author: 云奇迹
 * @date: 2024/3/20
 */
@Service("subscribe")
public class SubscribeFilter implements LogicFilter {

    private Logger logger = LoggerFactory.getLogger(SubscribeFilter.class);

    @Override
    public String filter(BehaviorMatter behaviorMessage) {
        return "感谢关注";
    }
}
