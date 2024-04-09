package com.myapp.chatgpt.data.domain.atuth.service;

import com.myapp.chatgpt.data.domain.atuth.model.entity.AuthStateEntity;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Map;

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

    /**
     * 获取 token 中的 openId
     * @param token
     * @return
     */
    String getOpenId(String token);

    /**
     * 生成 jWT token
     * @param issuer 签发人
     * @param ttlMillis 过期时间
     * @param claims
     */
    String genJwtToken(String issuer,long ttlMillis,Map<String,Object> claims);





}
