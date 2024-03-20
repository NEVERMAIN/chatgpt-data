package com.myapp.chatgpt.data.domain.weixin.service.behavior.engine;

import com.myapp.chatgpt.data.domain.weixin.model.entity.BehaviorMatter;

/**
 * @description: 消息引擎接口
 * @author: 云奇迹
 * @date: 2024/3/20
 */
public interface Engine {

    /**
     * 根据消息类型选择不同的过滤器处理
     * @param behaviorMatter
     * @return
     */
    String process(final BehaviorMatter behaviorMatter);
}
