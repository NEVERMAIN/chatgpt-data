package com.myapp.chatgpt.data.config;

import com.openicu.ltzf.factory.PayFactory;
import com.openicu.ltzf.factory.defaults.DefaultPayFactory;
import com.openicu.ltzf.payments.h5.H5PayService;
import com.openicu.ltzf.payments.jumph5.JumpH5PayService;
import com.openicu.ltzf.payments.nativepay.NativePayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 蓝兔支付配置
 * @author: 云奇迹
 * @date: 2024/4/16
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = LtzfSDKConfigProperties.class)
public class ltzfSDKConfig {

    @Bean("payFactory")
    @ConditionalOnProperty(value = "ltzf.sdk.config.enabled",havingValue = "true",matchIfMissing = false)
    public PayFactory payFactory(LtzfSDKConfigProperties properties) {
        com.openicu.ltzf.factory.Configuration configuration = new com.openicu.ltzf.factory.Configuration(
                properties.getAppId(),
                properties.getMerchantId(),
                properties.getPartnerKey()
        );

        return new DefaultPayFactory(configuration);
    }

    @Bean("nativePayService")
    @ConditionalOnProperty(value = "ltzf.sdk.config.enabled",havingValue = "true",matchIfMissing = false)
    public NativePayService nativePayService(PayFactory payFactory){
        log.info("蓝兔支付 SDK 启动成功,扫描支付服务已装配");
        return payFactory.nativePayService();
    }

    @Bean
    @ConditionalOnProperty(value = "ltzf.sdk.config.enabled",havingValue = "true",matchIfMissing = false)
    public H5PayService h5PayService(PayFactory payFactory){
        log.info("蓝兔支付 SDK 启动成功,h5 支付服务已装配");
        return payFactory.h5PayService();
    }

    @Bean
    @ConditionalOnProperty(value = "ltzf.sdk.config.enabled",havingValue = "true",matchIfMissing = false)
    public JumpH5PayService jumpH5PayService(PayFactory payFactory){
        log.info("蓝兔支付 SDK 启动成功, jump h5 支付服务已装配");
        return payFactory.jumpH5PayService();
    }


}
