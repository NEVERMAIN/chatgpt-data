package com.myapp.chatgpt.data.domain.weixin.repository;

/**
 * @description: 微信服务仓储接口
 * @author: 云奇迹
 * @date: 2024/4/1
 */
public interface IWeiXinRepository {

    /**
     * 生成验证码
     * @param openId
     * @return
     */
    String getCode(String openId);

    /**
     * 保存 openid token 到 Redis
     * @param ticket 登录凭证
     * @param token  jwt token
     */
    void saveOpenidToken(String ticket,String token);


}
