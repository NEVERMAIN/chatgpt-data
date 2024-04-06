package com.myapp.chatgpt.data.domain.openai.service.channel.impl;

import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.MessageEntity;
import com.myapp.chatgpt.data.domain.openai.service.channel.IGenerativeModelService;
import com.myapp.chatgpt.data.types.enums.Role;
import com.myapp.openai.executor.parameter.request.ImageRequest;
import com.myapp.openai.executor.parameter.response.ImageResponse;
import com.myapp.openai.session.OpenAiSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.List;

/**
 * @description: 图片生成
 * @author: 云奇迹
 * @date: 2024/4/6
 */
@Service
@Slf4j
public class ImageGenerativeModelServiceImpl implements IGenerativeModelService {

    @Autowired(required = false)
    private OpenAiSession openAiSession;

    @Override
    public void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws IOException {

        if (null == openAiSession) {
            emitter.send("openAiSession 通道，模型调用未开启，可以选择其他模型对话！");
            return;
        }

        try {
            // 1.封装消息
            StringBuilder prompt = new StringBuilder();
            List<MessageEntity> messages = chatProcess.getMessages();
            for (MessageEntity message : messages) {
                String role = message.getRole();
                if(Role.USER.getCode().equals(role)){
                    prompt.append(message.getContent());
                    prompt.append("\r\n");
                }
            }

            // 2.会话请求信息
            ImageRequest imageRequest = ImageRequest.builder()
                    .prompt(prompt.toString())
                    .model(chatProcess.getModel())
                    .build();

            // 3.调用服务
            ImageResponse imageResponse = openAiSession.genImages(imageRequest);
            List<ImageResponse.Item> data = imageResponse.getData();

            for (ImageResponse.Item item : data) {
                String url = item.getUrl();
                log.info("url:{}",url);
                emitter.send("![]("+url+")");
            }
            // 4.结束会话
            emitter.complete();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
