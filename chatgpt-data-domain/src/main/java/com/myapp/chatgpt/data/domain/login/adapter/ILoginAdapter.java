package com.myapp.chatgpt.data.domain.login.adapter;

/**
 * @description: 登录接口的适配
 * @author: 云奇迹
 * @date: 2024/4/9
 */
public interface ILoginAdapter {

    /**
     * 创建二维码
     * @return
     * @throws Exception
     */
    String createQrCodeTicket() throws Exception;

}
