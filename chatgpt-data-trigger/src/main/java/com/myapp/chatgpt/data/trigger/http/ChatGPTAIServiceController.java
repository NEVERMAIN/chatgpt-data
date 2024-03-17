package com.myapp.chatgpt.data.trigger.http;

import com.alibaba.fastjson.JSON;
import com.myapp.chatglm.model.Model;
import com.myapp.chatglm.model.Role;
import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.MessageEntity;
import com.myapp.chatgpt.data.domain.openai.service.IChatService;
import com.myapp.chatgpt.data.trigger.http.dto.ChatGLMRequestDTO;
import com.myapp.chatgpt.data.types.exception.ChatGPTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.CharacterCodingException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: AI服务的 http 方法
 * @author: 云奇迹
 * @date: 2024/3/17
 */
@Slf4j
@RestController
@RequestMapping("/api/${app.config.api-version}")
@CrossOrigin("*")
public class ChatGPTAIServiceController {


    @Resource
    private IChatService chatService;


    @PostMapping("chat/completions")
    public ResponseBodyEmitter completions(@RequestBody ChatGLMRequestDTO request,
                                           @RequestHeader("Authorization") String token,
                                           HttpServletResponse response){
        log.info("流式问答请求开始,使用的模型:{},请求信息:{}",request.getModel(), JSON.toJSONString(request.getMessages()));

        try {
            response.setContentType("text/event-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control","no-cache");

            // 构建聚合对象
            // 1. 获取用户请求的问题
            List<MessageEntity> messages = request.getMessages().stream().map((entity) -> {
                return MessageEntity.builder()
                        .role(Role.getRole(entity.getRole()).getCode())
                        .content(entity.getContent())
                        .build();
            }).collect(Collectors.toList());

            ChatProcessAggregate process = ChatProcessAggregate
                    .builder()
                    .token(token)
                    .Model(Model.getModel(request.getModel()).getCode())
                    .messages(messages)
                    .build();

            // 调用 openAI 服务
            return chatService.completions(process);
        } catch (Exception e) {
            log.error("流式问答请求出现异常,使用的模型:{},异常信息",request.getModel(),e);
            throw new ChatGPTException(e.getMessage());
        }

    }

}
