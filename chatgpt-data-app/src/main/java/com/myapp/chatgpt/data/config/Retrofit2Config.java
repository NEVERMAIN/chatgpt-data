package com.myapp.chatgpt.data.config;

import com.myapp.chatgpt.data.infrastructure.gateway.IWechatApiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @description: Retrofit2 配置
 * @author: 云奇迹
 * @date: 2024/4/9
 */
@Configuration
public class Retrofit2Config {

    private static final String BASE_URL = "https://api.weixin.qq.com/";

    @Bean
    public Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    /**
     * 创建并返回一个IWechatApiService实例。
     * 该方法使用Retrofit框架动态生成 IWechatApiService 接口的实现类，使得我们可以调用接口中定义的所有API。
     *
     * @param retrofit Retrofit 实例，用于构建 IWechatApiService 接口的实现类。
     * @return 返回通过 Retrofit 构建的 IWechatApiService 接口实现实例。
     */
    @Bean
    public IWechatApiService wechatApiService(Retrofit retrofit) {
        // 使用Retrofit的create方法，根据IWechatApiService接口生成具体的实现类
        return retrofit.create(IWechatApiService.class);
    }


}
