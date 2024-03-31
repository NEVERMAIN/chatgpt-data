package com.myapp.chatgpt.data.config;

import com.myapp.chatglm.session.ChatGLMConfiguration;
import com.myapp.chatglm.session.OpenAiSession;
import com.myapp.chatglm.session.defaults.DefaultOpenAiSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: ChatGLM 大模型工厂配置类
 * @author: 云奇迹
 * @date: 2024/3/16
 */
@Configuration
@EnableConfigurationProperties(ChatGLMConfigProperties.class)
public class ChatGLMConfig {

    @Bean(name = "chatGLMOpenAiSession")
    @ConditionalOnProperty(value = "chatglm.sdk.config.enable", havingValue = "true",matchIfMissing = false)
    public OpenAiSession openAiSession(ChatGLMConfigProperties properties) {
        ChatGLMConfiguration glmConfiguration = new ChatGLMConfiguration();
        glmConfiguration.setApiHost(properties.getApiHost());
        glmConfiguration.setApiSecretKey(properties.getApiSecretKey());

        // 创建会话工厂
        DefaultOpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(glmConfiguration);

        // 开启会话
        return factory.openSession();
    }
}
