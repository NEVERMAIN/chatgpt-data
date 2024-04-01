package com.myapp.chatgpt.data.domain.atuth.repository;

/**
 * @description: 认证仓储服务接口
 * @author: 云奇迹
 * @date: 2024/4/1
 */
public interface IAuthRepository {

    String getCodeByOpenId(String code);

    void removeCodeByOpenId(String code,String openId);
}
