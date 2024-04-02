package com.myapp.chatgpt.data.domain.atuth.repository;

/**
 * @description: 认证仓储服务接口
 * @author: 云奇迹
 * @date: 2024/4/1
 */
public interface IAuthRepository {

    /**
     * 从缓存中获取验证码
     * @param code
     * @return
     */
    String getCodeByOpenId(String code);

    /**
     * 删除缓存中的验证码
     * @param code
     * @param openId
     */
    void removeCodeByOpenId(String code,String openId);
}
