package com.myapp.chatgpt.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: 微信支付配置属性
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@Data
@ConfigurationProperties(value = "wxpay.config",ignoreInvalidFields = true)
public class WeChatPayConfigProperties {

    /** 申请支付主体的 appid */
    private String appid;
    /** 商户号 */
    private String merchantId;
    /** 回调地址 */
    private String notifyUrl;
    /** 商户API私钥路径 */
    private String privateKeyPath ;
    /** 商户证书序列号 */
    private String merchantSerialNumber;
    /** 商户 APIV3 密钥 */
    private String apiV3Key;

}
