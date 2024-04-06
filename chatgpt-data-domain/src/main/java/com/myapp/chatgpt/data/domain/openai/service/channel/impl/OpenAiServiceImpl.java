package com.myapp.chatgpt.data.domain.openai.service.channel.impl;

import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.valobj.GenerativeModelVO;
import com.myapp.chatgpt.data.domain.openai.service.channel.IOpenAiService;
import com.myapp.chatgpt.data.domain.openai.service.channel.IGenerativeModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: OpenAI 服务实现类
 * @author: 云奇迹
 * @date: 2024/4/6
 */
@Service
@Slf4j
public class OpenAiServiceImpl implements IOpenAiService {

    private final Map<GenerativeModelVO, IGenerativeModelService> generativeModelGroup = new HashMap<>();

    public OpenAiServiceImpl(TextGenerativeModelServiceImpl textGenerativeModelServiceImpl, ImageGenerativeModelServiceImpl imageGenerativeModelService) {
        generativeModelGroup.put(GenerativeModelVO.TEXT, textGenerativeModelServiceImpl);
        generativeModelGroup.put(GenerativeModelVO.IMAGES, imageGenerativeModelService);
    }

    @Override
    public void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws Exception {
        GenerativeModelVO generativeModelVO = chatProcess.getGenerativeModelVO();
        generativeModelGroup.get(generativeModelVO).doMessageResponse(chatProcess, emitter);
    }
}
