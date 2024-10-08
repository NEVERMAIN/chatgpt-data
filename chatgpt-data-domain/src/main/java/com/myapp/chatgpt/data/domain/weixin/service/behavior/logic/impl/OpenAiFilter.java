package com.myapp.chatgpt.data.domain.weixin.service.behavior.logic.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.MessageEntity;
import com.myapp.chatgpt.data.domain.openai.service.IChatService;
import com.myapp.chatgpt.data.domain.weixin.model.entity.BehaviorMatter;
import com.myapp.chatgpt.data.domain.weixin.service.behavior.logic.LogicFilter;
import com.myapp.chatgpt.data.types.enums.OpenAiModel;
import com.myapp.chatgpt.data.types.enums.OpenAiRole;
import com.myapp.openai.executor.parameter.Message;
import com.myapp.openai.executor.parameter.request.CompletionRequest;
import com.myapp.openai.session.OpenAiSession;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * @description: 处理 OpenAI 问答的过滤器
 * @author: 云奇迹
 * @date: 2024/3/20
 */
@Service("openAi")
public class OpenAiFilter implements LogicFilter {

    private final Logger logger = LoggerFactory.getLogger(OpenAiFilter.class);

    @Resource
    private ThreadPoolExecutor gptTaskExecutor;

    private Cache<String, OpenAiTaskInfo> gptTaskCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    @Resource
    private OpenAiSession openAiSession;


    /**
     * 考虑到生成式服务响应比较慢,需要根据公众号三次重试的机制设计策略
     *
     * @param behaviorMessage 用户行为实体对象
     * @return 响应结果
     */
    @Override
    public String filter(BehaviorMatter behaviorMessage) {

        String openId = behaviorMessage.getOpenId();
        String content = behaviorMessage.getContent().trim();
        logger.info("微信公众号接收用户消息:【openId:{},content:{}】", openId, content);
        String response = null;

        try {

            // 获取GPT对话任务,如果不存在,就创建
            if (null == gptTaskCache.getIfPresent(openId)) {
                OpenAiTaskInfo openAiTaskInfo = new OpenAiTaskInfo();
                openAiTaskInfo.setFuture(submitGptTask(content));
                openAiTaskInfo.setRetryTimes(0);
                gptTaskCache.put(openId, openAiTaskInfo);
            }

            // 获取相应的结果
            OpenAiTaskInfo openAiTaskInfo = gptTaskCache.getIfPresent(openId);
            try {
                // 重试次数 + 1
                assert openAiTaskInfo != null;
                openAiTaskInfo.addRetryTimes();
                Future<String> future = openAiTaskInfo.getFuture();
                // 获取响应的结果
                response = future.get(4, TimeUnit.SECONDS);
                logger.info("AI 回复用户信息:【openId:{},content:{}】", openId, response);
                gptTaskCache.invalidate(openId);
            } catch (TimeoutException e) {
                // 利用公众号的重试机制,保证 3 次重试内回复消息即可
                if (openAiTaskInfo.getRetryTimes() == 3) {
                    response = "AI 仍在思考中，请发送任意消息重新获取答案";
                } else if (openAiTaskInfo.getRetryTimes() == 6) {
                    response = "AI 被你的问题难倒了,请你换一个问题.";
                    gptTaskCache.invalidate(openId);
                } else {
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            logger.error("处理 GPT 对话任务异常:【OpenID:{} content:{}】", openId, content);
            response = "AI处理异常,请稍后重试";
            gptTaskCache.invalidate(openId);
        }

        return response;
    }

    private Future<String> submitGptTask(String content) throws InterruptedException {
        try {
            Future<CompletableFuture<String>> submit = gptTaskExecutor.submit(() -> {

                CompletionRequest completionRequest = CompletionRequest.builder()
                        .stream(true)
                        .model(OpenAiModel.GLM_3_TURBO.getCode())
                        .messages(new ArrayList<Message>() {
                            private static final long serialVersionUID = -7988151926241837899L;

                            {
                                add(Message.builder()
                                        .role(OpenAiRole.USER.getCode())
                                        .content(content)
                                        .build());
                            }
                        }).build();
                return openAiSession.completions(completionRequest);
            });
            // 返回异步请求的结果
            return submit.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    @Data
    public static class OpenAiTaskInfo {
        private Future<String> future;
        private Integer retryTimes;

        public void addRetryTimes() {
            retryTimes++;
        }
    }
}
