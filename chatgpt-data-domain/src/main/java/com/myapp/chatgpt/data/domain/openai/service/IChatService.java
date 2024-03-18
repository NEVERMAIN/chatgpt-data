package com.myapp.chatgpt.data.domain.openai.service;

import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * @description: chat服务的接口
 * @author: 云奇迹
 * @date: 2024/3/17
 */
public interface IChatService {

    /**
     * 流式反馈接口
     * @param process
     * @return
     */
    ResponseBodyEmitter completions(ChatProcessAggregate process,ResponseBodyEmitter emitter);
}
