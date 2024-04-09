package com.myapp.chatgpt.data.domain.atuth.login.service;

import com.myapp.chatgpt.data.domain.atuth.login.adapter.ILoginAdapter;
import com.myapp.chatgpt.data.domain.atuth.login.repository.ILoginRepository;
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
    private ILoginRepository loginRepository;

    @Override
    public String createQrCodeTicket() {
        try {
            String qrCodeTicket = loginAdapter.createQrCodeTicket();
            if(qrCodeTicket != null){
                return qrCodeTicket;
            }else{
               throw new RuntimeException("微信公众号扫码登录获取 ticket");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String checkLogin(String ticket) {
        // 通过 ticket 判断,用户是否登录,如果登陆了,会在内存中写入信息
        return loginRepository.checkLogin(ticket);
    }
}
