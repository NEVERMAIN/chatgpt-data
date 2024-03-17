package com.myapp.chatgpt.data.trigger.http;

import com.alibaba.fastjson.JSON;
import com.myapp.chatglm.model.Model;
import com.myapp.chatglm.model.Role;
import com.myapp.chatglm.model.chat.ChatCompletionRequest;
import com.myapp.chatglm.model.chat.ChatCompletionResponse;
import com.myapp.chatglm.session.OpenAiSession;
import com.myapp.chatgpt.data.trigger.http.dto.ChatGLMRequestDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 测试开发 chat 方法
 * @author: 云奇迹
 * @date: 2024/3/16
 */
@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("api/v0")
public class ChatGPTAIServiceControllerOld {

    @Autowired
    private OpenAiSession openAiSession;


    @PostMapping("/chat/completions")
    public ResponseBodyEmitter completionsStream(@RequestBody ChatGLMRequestDTO request,
                                                 @RequestHeader("Authorization") String token ,
                                                 HttpServletResponse response){
        log.info("流式问答请求开始,使用的模型:{},请求信息:{}",request.getModel(), JSON.toJSONString(request.getMessages()));
        try{

            // 1.设置返回体
            response.setContentType("text/event-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control","no-cache");

            if(!"openai".equals(token)){
                throw new RuntimeException("token error,Not Allow");
            }

            // 2. 异步处理 Http 请求
            ResponseBodyEmitter emitter = new ResponseBodyEmitter(3 * 60 * 1000L);
            // 2.1 设置处理完毕的回调
            emitter.onCompletion(()->{
                log.info("流式问答请求完成,使用的模型:{}",request.getModel());
            });

            emitter.onError((e)->{
                log.info("流式问答请求出现异常,使用的模型:{},异常信息:{}",request.getModel(),e.getMessage());
            });

            // 3.获取请求头的数据
            List<ChatCompletionRequest.Prompt> messages = request.getMessages().stream()
                    .map((entity) -> ChatCompletionRequest.Prompt.builder()
                            .role(Role.getRole(entity.getRole()).getCode())
                            .content(entity.getContent())
                            .build())
                    .collect(Collectors.toList());

            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(Model.getModel(request.getModel()).getCode())
                    .messages(messages)
                    .stream(true)
                    .build();

            // 调用 AI 服务
            this.openAiSession.completions(chatCompletionRequest, new EventSourceListener() {
                @Override
                public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                    ChatCompletionResponse chatCompletionResponse = JSON.parseObject(data, ChatCompletionResponse.class);
                    List<ChatCompletionResponse.Choice> choices = chatCompletionResponse.getChoices();
                    for (ChatCompletionResponse.Choice choice : choices) {
                        ChatCompletionResponse.Delta delta = choice.getDelta();
                        if(!Role.ASSISTANT.getCode().equals(delta.getRole())) continue;


                        // 应答完成
                        String finishReason = choice.getFinishReason();
                        if(StringUtils.isNotBlank(finishReason) && "stop".equals(finishReason)){
                            emitter.complete();
                            break;
                        }

                        // 发送信息
                        try {
                            emitter.send(delta.getContent());
                        }catch (IOException e){
                            throw new RuntimeException(e);
                        }

                    }

                }
            });

            return emitter;


        }catch (Exception e){
            log.info("流式问答请求出现异常,使用的模型:{},异常信息:{}",request.getModel(),e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
