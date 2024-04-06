package com.myapp.chatgpt.data.domain.openai.service.channel;

import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;

/**
 * @description: 模型生成文字|图片接口服务
 * @author: 云奇迹
 * @date: 2024/4/6
 */
public interface IGenerativeModelService {

    /**
     * 处理会话消息
     * @param chatProcess  消息聚合对象
     * @param emitter
     * @throws IOException
     */
    void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws IOException;
}
