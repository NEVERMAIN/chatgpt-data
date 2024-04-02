package com.myapp.chatgpt.data.domain.weixin.service;

/**
 * @description: 微信验签服务
 * @author: 云奇迹
 * @date: 2024/3/18
 */
public interface IWeiXinValidateService {

    /**
     * 验签方法
     */
     boolean checkValid(String signature,String timestamp,String nonce);
}
