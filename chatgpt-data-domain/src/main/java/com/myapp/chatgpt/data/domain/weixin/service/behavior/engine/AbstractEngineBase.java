package com.myapp.chatgpt.data.domain.weixin.service.behavior.engine;

import com.myapp.chatgpt.data.domain.weixin.model.entity.BehaviorMatter;
import com.myapp.chatgpt.data.domain.weixin.service.behavior.logic.LogicFilter;

import java.util.Map;

/**
 * @description: 消息引擎基础类
 * @author: 云奇迹
 * @date: 2024/3/20
 */
public abstract class AbstractEngineBase extends EngineConfig implements Engine{
    @Override
    public String process(BehaviorMatter behaviorMatter) {
        throw new RuntimeException("未实现消息引擎服务");
    }

    /**
     * 根据消息类型返回对应的处理过滤器
     * @param behaviorMatter
     * @return
     */
    protected LogicFilter router(BehaviorMatter behaviorMatter){

        Map<String, LogicFilter> logicGroup = logicFilterMap.get(behaviorMatter.getMsgType());

        // 处理事件
        if("event".equals(behaviorMatter.getMsgType())){
            return logicGroup.get(behaviorMatter.getEvent());
        }

        // 内容处理
        if("text".equals(behaviorMatter.getMsgType())){
            LogicFilter logicFilter = logicGroup.get(behaviorMatter.getContent().trim());
            if(logicFilter != null){
                return logicFilter;
            }
            return logicGroup.get("openAi");
        }

        return null;

    }
}
