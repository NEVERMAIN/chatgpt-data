package com.myapp.chatgpt.data.domain.openai.service.channel.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.myapp.chatglm.model.Model;
import com.myapp.chatglm.model.Role;
import com.myapp.chatglm.model.chat.ChatCompletionRequest;
import com.myapp.chatglm.model.chat.ChatCompletionResponse;
import com.myapp.chatglm.session.OpenAiSession;
import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.service.channel.OpenAiGroupService;
import com.myapp.chatgpt.data.types.enums.ChatGLMModel;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/29
 */
@Service
@Slf4j
public class ChatGLMService implements OpenAiGroupService {

    @Resource
    private OpenAiSession chatGLMOpenAiSession;


    @Override
    public void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) {
        try {


            List<ChatCompletionRequest.Prompt> messages = chatProcess.getMessages().stream()
                    .map((entity) -> ChatCompletionRequest.Prompt.builder()
                            .role(Role.getRole(entity.getRole()).getCode())
                            .content(entity.getContent())
                            .build()).collect(Collectors.toList());

            // 准备参数
            ChatCompletionRequest chatCompletion = ChatCompletionRequest.builder()
                    .stream(true)
                    .model(ChatGLMModel.get(chatProcess.getModel()).getCode())
                    .messages(messages)
                    .build();

            // 调用服务
            // new EventSourceListener(){} 是传了一个匿名内部类, 返回 EventSource 等待服务器响应触发
            this.chatGLMOpenAiSession.completions(chatCompletion, new EventSourceListener() {
                @Override
                public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                    // 解析 data
                    ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);
                    List<ChatCompletionResponse.Choice> choices = response.getChoices();
                    for (ChatCompletionResponse.Choice choice : choices) {
                        ChatCompletionResponse.Delta delta = choice.getDelta();
                        // 判断是不是 assistant
                        if (!Role.ASSISTANT.getCode().equals(delta.getRole())) {
                            continue;
                        }

                        // 判断时是否结束
                        String finishReason = choice.getFinishReason();
                        if (StringUtils.isNotBlank(finishReason) && "stop".equals(finishReason)) {
                            emitter.complete();
                            break;
                        }

                        // 发送消息
                        try {
                            emitter.send(delta.getContent());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

        } catch (Exception e) {
            log.info("流式问答请求出现异常,异常信息:{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
