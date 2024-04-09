package com.myapp.chatgpt.data.domain.atuth.login.service;

/**
 * @description: 微信登录服务
 * @author: 云奇迹
 * @date: 2024/4/9
 */
public interface ILoginService {

    /**
     * 创建二维码
     * @return
     */
    String createQrCodeTicket();

    /**
     * 检测登录
     * @param ticket
     * @return
     */
    String checkLogin(String ticket);
}
