package com.myapp.chatgpt.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/28
 */
@Data
@ConfigurationProperties(value = "spark.sdk.config", ignoreInvalidFields = true)
public class SparkConfigProperties {

    /**
     * 是否可用
     */
    private String enable;
    /**
     * 应用ID
     */
    private String appid;
    /**
     * 密钥
     */
    private String apiKey;
    /**
     * 密钥
     */
    private String apiSecret;

}
