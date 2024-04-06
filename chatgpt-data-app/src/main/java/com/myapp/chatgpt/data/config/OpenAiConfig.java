package com.myapp.chatgpt.data.config;

import com.myapp.openai.executor.model.chatglm.config.ChatGLMConfig;
import com.myapp.openai.executor.model.spark.config.SparkConfig;
import com.myapp.openai.session.OpenAiConfiguration;
import com.myapp.openai.session.OpenAiSession;
import com.myapp.openai.session.defaults.DefaultOpenAiSessionFactory;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: OpenAi SDK 配置类
 * @author: 云奇迹
 * @date: 2024/4/5
 */
@Configuration
@EnableConfigurationProperties(value = {ChatGLMConfigProperties.class,SparkConfigProperties.class})
public class OpenAiConfig {

    @Bean("openAiSession")
    public OpenAiSession openAiSession(ChatGLMConfigProperties chatGLMProperties,SparkConfigProperties sparkProperties){

        // 1.ChatGLM 配置类
        ChatGLMConfig chatGLMConfig = new ChatGLMConfig();
        chatGLMConfig.setApiHost(chatGLMProperties.getApiHost());
        chatGLMConfig.setApiSecretKey(chatGLMProperties.getApiSecretKey());

        // 2.Spark 配置类
        SparkConfig sparkConfig = new SparkConfig();
        sparkConfig.setAppid(sparkProperties.getAppid());
        sparkConfig.setApiKey(sparkProperties.getApiKey());
        sparkConfig.setApiSecret(sparkProperties.getApiSecret());

        // 3. OpenAi 配置类
        OpenAiConfiguration openAiConfiguration = new OpenAiConfiguration();
        openAiConfiguration.setChatGLMConfig(chatGLMConfig);
        openAiConfiguration.setSparkConfig(sparkConfig);
        openAiConfiguration.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 4.创建工厂
        DefaultOpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(openAiConfiguration);

        // 5.创建会话
        return factory.openSession();
    }


}
