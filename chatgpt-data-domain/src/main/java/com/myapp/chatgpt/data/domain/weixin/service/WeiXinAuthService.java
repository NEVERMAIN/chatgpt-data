package com.myapp.chatgpt.data.domain.weixin.service;

import com.myapp.chatgpt.data.types.sdk.weixin.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/18
 */
@Service
@Slf4j
public class WeiXinAuthService implements IWeiXinAuthService{

    @Value("${wx.config.token}")
    private String token;

    @Override
    public boolean checkValid(String signature, String timestamp, String nonce) {
        return SignatureUtil.check(token,signature,timestamp,nonce);
    }
}
