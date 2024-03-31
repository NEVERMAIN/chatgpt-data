package com.myapp.chatgpt.data.domain.openai.service.channel.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.MessageEntity;
import com.myapp.chatgpt.data.domain.openai.service.channel.OpenAiGroupService;
import com.myapp.spark.executor.listener.ChatListener;
import com.myapp.spark.executor.model.Model;
import com.myapp.spark.executor.model.chat.ChatCompletionRequest;
import com.myapp.spark.executor.model.chat.ChatCompletionResponse;
import com.myapp.spark.session.SparkAiSession;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/29
 */
@Service
@Slf4j
public class SparkService implements OpenAiGroupService {

    @Resource
    private SparkAiSession sparkOpenAiSession;

    private String appid = "dac4535e";

    @Override
    public void doMessageResponse(ChatProcessAggregate aggregate, ResponseBodyEmitter emitter) {

        try {
            // 请求的消息
            List<MessageEntity> messages = aggregate.getMessages();
            MessageEntity messageEntity = messages.get(messages.size() - 1);
            ChatCompletionRequest.Text text = ChatCompletionRequest.Text.builder()
                    .role(messageEntity.getRole())
                    .content(messageEntity.getContent())
                    .build();

            // 2.构造 入参 请求
            ChatCompletionRequest completionRequest = new ChatCompletionRequest();

            ChatCompletionRequest.Header header = ChatCompletionRequest.Header.builder().app_id(appid).build();
            ChatCompletionRequest.Chat chat = ChatCompletionRequest.Chat.builder()
                    .domain(Model.GENERALV_3_5.getCode())
                    .max_tokens(2048).top_k(4).temperature(0.5F).build();
            ChatCompletionRequest.Parameter parameter = ChatCompletionRequest.Parameter.builder().chat(chat).build();
            ChatCompletionRequest.Message message = ChatCompletionRequest.Message.builder().text(new ArrayList<ChatCompletionRequest.Text>() {{
                add(text);
            }}).build();
            ChatCompletionRequest.Payload payload = ChatCompletionRequest.Payload.builder().message(message).build();

            completionRequest.setHeader(header);
            completionRequest.setParameter(parameter);
            completionRequest.setPayload(payload);

            WebSocket webSocket = sparkOpenAiSession.completions(new ChatListener<ChatCompletionRequest>(completionRequest) {
                @Override
                public void onChatMessage(ChatCompletionResponse chatCompletionResponse) {
                    ChatCompletionResponse.ResponseText text = chatCompletionResponse.getPayload().getChoices().getText().get(0);
                    try {
                        emitter.send(text.getContent());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onEndMessage(ChatCompletionResponse chatCompletionResponse) {
                    emitter.complete();
                }

                @Override
                public void onChatError(Throwable throwable, Response response) {
                    try {
                        if (null != response) {
                            int code = response.code();
                            log.error("onFailure code:" + code);
                            log.error("onFailure body:" + response.body().string());
                            if (101 != code) {
                                log.error("connection failed");
                                System.exit(0);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
