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
    /**
     * 是否有效
     */
    private String enable;
    /**
     * API 请求地址
     */
    private String apiHost;
    /**
     * 密钥
     */
    private String apiSecretKey;
}
