package com.myapp.chatgpt.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: 支付宝支付配置属性-沙箱
 * @author: 云奇迹
 * @date: 2024/3/24
 */
@Data
@ConfigurationProperties(value = "alipay",ignoreInvalidFields = true)
public class AliPayConfigProperties {

    private String appId;

    private String merchantPrivateKey;

    private String alipayPublicKey;

    private String notifyUrl;

    private String returnUrl;

    private String gatewayUrl;

    private String signType = "RSA2";

    private String charset= "utf-8";
    /** 字符串编码 */
    private String format = "json";



}
