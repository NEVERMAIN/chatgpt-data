package com.myapp.chatgpt.data.domain.openai.service.channel.impl;

import com.alibaba.fastjson.JSON;
import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.MessageEntity;
import com.myapp.chatgpt.data.domain.openai.service.channel.IGenerativeModelService;
import com.myapp.chatgpt.data.types.enums.OpenAiRole;
import com.myapp.openai.executor.parameter.Message;
import com.myapp.openai.executor.parameter.request.CompletionRequest;
import com.myapp.openai.executor.parameter.response.ChatChoice;
import com.myapp.openai.executor.parameter.response.CompletionResponse;
import com.myapp.openai.session.OpenAiSession;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 文本生成
 * @author: 云奇迹
 * @date: 2024/4/6
 */
@Service
@Slf4j
public class TextGenerativeModelServiceImpl implements IGenerativeModelService {

    @Autowired(required = false)
    private OpenAiSession openAiSession;

    @Override
    public void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws IOException {
        if (null == openAiSession) {
            emitter.send("openAiSession 通道，模型调用未开启，可以选择其他模型对话！");
            return;
        }
        try {
            // 1.请求参数
            ArrayList<Message> prompts = new ArrayList<>();

            List<MessageEntity> messages = chatProcess.getMessages();
            List<Message> list = messages.stream().map(entity -> {
                return Message.builder()
                        .role(entity.getRole())
                        .content(entity.getContent())
                        .build();
            }).collect(Collectors.toList());

            // 2.准备参数
            CompletionRequest completionRequest = new CompletionRequest();
            completionRequest.setMessages(list);
            completionRequest.setModel(chatProcess.getModel());
            completionRequest.setStream(true);

            // 3.调用服务
            openAiSession.completions(completionRequest, new EventSourceListener() {

                @Override
                public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                    // 1.解析参数
                    CompletionResponse completionResponse = JSON.parseObject(data, CompletionResponse.class);
                    List<ChatChoice> choices = completionResponse.getChoices();
                    for (ChatChoice choice : choices) {
                        Message delta = choice.getDelta();
                        // 判断是不是 assistant
                        if (!OpenAiRole.ASSISTANT.getCode().equals(delta.getRole())) continue;

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

                @Override
                public void onClosed(@NotNull EventSource eventSource) {
                    super.onClosed(eventSource);
                }

                @Override
                public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                    super.onFailure(eventSource, t, response);
                }
            });
        } catch (Exception e) {
            log.info("流式问答请求出现异常,异常信息:{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
