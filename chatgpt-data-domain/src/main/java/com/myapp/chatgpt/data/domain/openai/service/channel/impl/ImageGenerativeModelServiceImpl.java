package com.myapp.chatgpt.data.domain.openai.service.channel.impl;

import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.MessageEntity;
import com.myapp.chatgpt.data.domain.openai.service.channel.IGenerativeModelService;
import com.myapp.chatgpt.data.types.enums.OpenAiRole;
import com.myapp.openai.executor.parameter.request.ImageRequest;
import com.myapp.openai.executor.parameter.response.ImageResponse;
import com.myapp.openai.session.OpenAiSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: 图片生成
 * @author: 云奇迹
 * @date: 2024/4/6
 */
@Service
@Slf4j
public class ImageGenerativeModelServiceImpl implements IGenerativeModelService {

    /**
     * OpenAi 服务
     */
    @Autowired(required = false)
    private OpenAiSession openAiSession;

    /**
     * 线程池
     */
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws IOException {

        if (null == openAiSession) {
            emitter.send("openAiSession 通道，模型调用未开启，可以选择其他模型对话！");
            return;
        }

        // 1.封装消息
        StringBuilder prompt = new StringBuilder();
        List<MessageEntity> messages = chatProcess.getMessages();
        for (MessageEntity message : messages) {
            String role = message.getRole();
            if (OpenAiRole.USER.getCode().equals(role)) {
                prompt.append(message.getContent());
                prompt.append("\r\n");
            }
        }

        // 2.会话请求信息
        ImageRequest imageRequest = ImageRequest.builder()
                .prompt(prompt.toString())
                .model(chatProcess.getModel())
                .build();

        emitter.send("您的\uD83D\uDE0A图片正在生成中,请耐心等待....\r\n");

        // 异步线程提交
        threadPoolExecutor.execute(() -> {
            ImageResponse imageResponse = null;
            try {
                // 3.调用服务
                imageResponse = openAiSession.genImages(imageRequest);
                List<ImageResponse.Item> data = imageResponse.getData();

                for (ImageResponse.Item item : data) {
                    String url = item.getUrl();
                    log.info("url:{}", url);
                    emitter.send("![](" + url + ")");
                }
                // 4.结束会话
                emitter.complete();
            } catch (Exception e) {
                try {
                    emitter.send("您的😭图片生成失败了，请调整说明... \r\n");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


    }
}
