package com.myapp.chatgpt.data.domain.atuth.service;

import com.myapp.chatgpt.data.domain.atuth.model.entity.AuthStateEntity;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * @description: 鉴权服务
 * @author: 云奇迹
 * @date: 2024/3/18
 */
public interface IAuthService {

    /**
     * 登录验证,生成 token 返回
     * @param code
     * @return
     */
    AuthStateEntity doLogin(String code);

    /**
     * 检查 token 是否有效
     * @param token
     * @return
     */
    boolean checkToken(String token);





}
