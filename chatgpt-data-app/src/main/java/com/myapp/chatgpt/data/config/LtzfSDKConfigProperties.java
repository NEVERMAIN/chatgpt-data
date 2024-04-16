package com.myapp.chatgpt.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: 蓝兔支付配置属性
 * @author: 云奇迹
 * @date: 2024/4/16
 */
@Data
@ConfigurationProperties("ltzf.sdk.config")
public class LtzfSDKConfigProperties {

    /**
     * 是否启动
     */
    private Boolean enabled;
    /**
     * appid
     */
    private String appId;
    /**
     * 商户号
     */
    private String merchantId;
    /**
     * 商户密钥
     */
    private String partnerKey;

    /**
     * 支付回调地址
     */
    private String notifyUrl;

}
