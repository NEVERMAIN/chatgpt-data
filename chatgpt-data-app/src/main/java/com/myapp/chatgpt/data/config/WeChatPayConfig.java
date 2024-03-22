package com.myapp.chatgpt.data.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.xml.ws.WebEndpoint;
import java.io.File;
import java.io.InputStream;


/**
 * @description: 微信支付服务配置
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(WeChatPayConfigProperties.class)
public class WeChatPayConfig {

    /**
     * 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
     * @param properties
     * @return
     */
    @Bean
    public NativePayService buildNativePayService(WeChatPayConfigProperties properties){
        // 支付配置: 用于自动更新平台证书。
        RSAAutoCertificateConfig config = new RSAAutoCertificateConfig.Builder()
                .merchantId(properties.getMerchantId())
                .privateKeyFromPath(getFilePath(properties.getPrivateKeyPath()))
                .merchantSerialNumber(properties.getMerchantSerialNumber())
                .apiV3Key(properties.getApiV3Key())
                .build();

        // NativePay 支付服务
        return new NativePayService.Builder().config(config).build();
    }

    /**
     * 初始化 RSAAutoCertificateConfig,用来构造回调需要的 NotificationParser
     * 微信支付平台证书由 SDK 的自动更新平台能力提供，也可以使用本地证书
     * @param properties
     * @return
     */
    @Bean
    public NotificationConfig buildNotificationConfig(WeChatPayConfigProperties properties){

        return new RSAAutoCertificateConfig.Builder()
                .merchantId(properties.getMerchantId())
                .privateKeyFromPath(getFilePath(properties.getPrivateKeyPath()))
                .apiV3Key(properties.getApiV3Key())
                .merchantSerialNumber(properties.getMerchantSerialNumber())
                .build();
    }


    /**
     * 初始化 NotificationParser
     * 验签、解密并将 JSON 转换成具体的通知回调对象。如果验签失败，SDK 会抛出 ValidationException。
     * @param notificationConfig
     * @return
     */
    @Bean
    public NotificationParser buildNotificationParser(NotificationConfig notificationConfig){
        return new NotificationParser(notificationConfig);
    }

    public static String getFilePath(String classFilePath) {
        String filePath = "";
        try {
            String templateFilePath = "tempfiles/classpathfile/";
            File tempDir = new File(templateFilePath);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            String[] filePathList = classFilePath.split("/");
            String checkFilePath = "tempfiles/classpathfile";
            for (String item : filePathList) {
                checkFilePath += "/" + item;
            }
            File tempFile = new File(checkFilePath);
            if (tempFile.exists()) {
                filePath = checkFilePath;
            } else {
                //解析
                ClassPathResource classPathResource = new ClassPathResource(classFilePath);
                InputStream inputStream = classPathResource.getInputStream();
                checkFilePath = "tempfiles/classpathfile";
                for (int i = 0; i < filePathList.length; i++) {
                    checkFilePath += "/" + filePathList[i];
                    if (i == filePathList.length - 1) {
                        //文件
                        File file = new File(checkFilePath);
                        if (!file.exists()) {
                            FileUtils.copyInputStreamToFile(inputStream, file);
                        }
                    } else {
                        //目录
                        tempDir = new File(checkFilePath);
                        if (!tempDir.exists()) {
                            tempDir.mkdirs();
                        }
                    }
                }
                inputStream.close();
                filePath = checkFilePath;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }




}
