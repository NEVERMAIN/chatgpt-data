package com.myapp.chatgpt.data.domain.weixin.service.behavior.logic;

import com.myapp.chatgpt.data.domain.weixin.model.entity.BehaviorMatter;

/**
 * @description: 处理消息类型的接口 - 策略接口
 * @author: 云奇迹
 * @date: 2024/3/20
 */
public interface LogicFilter {

    /**
     * 处理消息类型的逻辑
     * @param behaviorMessage 行为消息对象
     * @return
     */
    String filter(BehaviorMatter behaviorMessage);
}
