package com.myapp.chatgpt.data.domain.openai.service.channel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/29
 */
public interface OpenAiGroupService {

    /**
     * 处理消息的方法
     * @param aggregate 消息聚合对象
     * @param emitter
     * @throws JsonProcessingException
     */
    void doMessageResponse(ChatProcessAggregate aggregate, ResponseBodyEmitter emitter) throws JsonProcessingException;
}
