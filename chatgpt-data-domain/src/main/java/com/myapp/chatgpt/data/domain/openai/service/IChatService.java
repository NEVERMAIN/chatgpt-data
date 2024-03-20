package com.myapp.chatgpt.data.domain.openai.service;

import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.sun.xml.internal.ws.util.CompletedFuture;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.concurrent.CompletableFuture;

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

    /**
     * 异步处理返回的结果
     * @param process
     * @return
     */
    CompletableFuture<String> completions(ChatProcessAggregate process);



}
