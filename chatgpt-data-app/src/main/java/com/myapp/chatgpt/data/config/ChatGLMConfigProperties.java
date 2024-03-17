package com.myapp.chatgpt.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: ChatGLM 配置属性
 * @author: 云奇迹
 * @date: 2024/3/16
 */
@Data
@ConfigurationProperties(prefix = "chatglm.sdk.config",
        ignoreInvalidFields = true)
public class ChatGLMConfigProperties {

    private String apiHost;
    private String apiSecretKey;
}
