package com.myapp.chatgpt.data.domain.openai.service;

import com.alibaba.fastjson.JSON;
import com.myapp.chatglm.model.Model;
import com.myapp.chatglm.model.Role;
import com.myapp.chatglm.model.chat.ChatCompletionRequest;
import com.myapp.chatglm.model.chat.ChatCompletionResponse;
import com.myapp.chatglm.session.OpenAiSession;
import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import com.myapp.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.openai.model.vo.LogicTypeVO;
import com.myapp.chatgpt.data.domain.openai.service.channel.impl.ChatGLMService;
import com.myapp.chatgpt.data.domain.openai.service.channel.impl.SparkService;
import com.myapp.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import com.myapp.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFilterFactory;
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
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @description: chat 服务实现类
 * @author: 云奇迹
 * @date: 2024/3/17
 */
@Service
@Slf4j
public class ChatService extends AbstractChatService {


    @Resource
    private OpenAiSession chatGLMOpenAiSession;

    @Resource
    private DefaultLogicFilterFactory defaultLogicFilterFactory;

    public ChatService(ChatGLMService chatGLMService, SparkService sparkService) {
        super(chatGLMService, sparkService);
    }

    @Override
    protected void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) {

        try {

            List<ChatCompletionRequest.Prompt> messages = chatProcess.getMessages().stream()
                    .map((entity) -> ChatCompletionRequest.Prompt.builder()
                            .role(Role.getRole(entity.getRole()).getCode())
                            .content(entity.getContent())
                            .build()).collect(Collectors.toList());

            // 准备参数
            ChatCompletionRequest chatCompletion = ChatCompletionRequest.builder()
                    .stream(true)
                    .model(Model.getModel(chatProcess.getModel()).getCode())
                    .messages(messages)
                    .build();

            // 处理应答
            chatGLMOpenAiSession.completions(chatCompletion, new EventSourceListener() {
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

    @Override
    protected RuleLogicEntity<ChatProcessAggregate> doCheckLogic(ChatProcessAggregate process, UserAccountQuotaEntity accountQuotaEntity, String... logics) {

        // 1.获得所有的权限策略
        Map<String, ILogicFilter> groups = defaultLogicFilterFactory.getLogicFilterGroups();

        RuleLogicEntity<ChatProcessAggregate> entity = null;
        for (String logic : logics) {
            // 2. 如果权限类型为 null ,枚举下一个
            if (DefaultLogicFilterFactory.LogicModel.NULL.getCode().equals(logic)) continue;
            // 3. logic 不为 null , 获取权限校验的策略
            ILogicFilter logicFilter = groups.get(logic);
            // 4. 进行权限的校验
            entity = logicFilter.filter(process, accountQuotaEntity);
            if (!LogicTypeVO.SUCCESS.getCode().equals(entity.getType().getCode())) return entity;
        }

        return entity != null ? entity :
                RuleLogicEntity.<ChatProcessAggregate>builder()
                        .type(LogicTypeVO.SUCCESS)
                        .data(process)
                        .info(LogicTypeVO.SUCCESS.getInfo()).build();
    }

    @Override
    public CompletableFuture<String> completions(ChatProcessAggregate process) {

        List<ChatCompletionRequest.Prompt> messages = process.getMessages().stream()
                .map((entity) -> ChatCompletionRequest.Prompt.builder()
                        .role(Role.getRole(entity.getRole()).getCode())
                        .content(entity.getContent())
                        .build()).collect(Collectors.toList());

        // 准备参数
        ChatCompletionRequest chatCompletion = ChatCompletionRequest.builder()
                .stream(true)
                .model(Model.getModel(process.getModel()).getCode())
                .messages(messages)
                .build();

        return this.chatGLMOpenAiSession.completions(chatCompletion);
    }
}
