package com.myapp.chatgpt.data.domain.login.service;

import com.google.common.cache.Cache;
import com.myapp.chatgpt.data.domain.login.adapter.ILoginAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/4/9
 */
@Service
@Slf4j
public class WeChatLoginServiceImpl implements ILoginService{

    @Resource
    private ILoginAdapter loginAdapter;

    @Resource
    private Cache<String, String> openidToken;

    @Override
    public String createQrCodeTicket() {
        try {
            return loginAdapter.createQrCodeTicket();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String checkLogin(String ticket) {
        // 通过 ticket 判断,用户是否登录,如果登陆了,会在内存中写入信息
        return openidToken.getIfPresent(ticket);
    }
}
