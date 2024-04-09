package com.myapp.chatgpt.data.domain.atuth.login.repository;

/**
 * @description: 登录鉴权的仓储服务
 * @author: 云奇迹
 * @date: 2024/4/9
 */
public interface ILoginRepository {

    /**
     * 校验是否登录
     * @param ticket 登录凭证
     * @return
     */
    String checkLogin(String ticket);
}
