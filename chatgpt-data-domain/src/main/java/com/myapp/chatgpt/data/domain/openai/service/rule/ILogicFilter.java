package com.myapp.chatgpt.data.domain.openai.service.rule;

import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;

import java.util.concurrent.ExecutionException;

/**
 * @description: 规则过滤的结果
 * @author: 云奇迹
 * @date: 2024/3/19
 */
public interface ILogicFilter {

    /**
     * 规则过滤方法
     * @param process
     * @return
     */
    RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate process);
}
