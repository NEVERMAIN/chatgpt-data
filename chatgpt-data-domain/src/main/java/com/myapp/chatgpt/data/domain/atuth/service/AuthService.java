package com.myapp.chatgpt.data.domain.atuth.service;

import com.google.common.cache.Cache;
import com.myapp.chatgpt.data.domain.atuth.model.entity.AuthStateEntity;
import com.myapp.chatgpt.data.domain.atuth.model.vo.AuthTypeVO;
import com.myapp.chatgpt.data.domain.atuth.repository.IAuthRepository;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 鉴权服务实现类
 * @author: 云奇迹
 * @date: 2024/3/18
 */
@Service
@Slf4j
public class AuthService  extends AbstractAuthService{

    @Resource
    private Cache<String,String> codeCache;

    @Resource
    private IAuthRepository authRepository;

    @Override
    public AuthStateEntity checkCode(String code) {

        // 1. 从缓存中获取用户凭证
        String openId = authRepository.getCodeByOpenId(code);
        if(StringUtils.isBlank(openId)){
            log.info("鉴权,用户输入的验证码不存在");
            return AuthStateEntity.builder()
                    .code(AuthTypeVO.NOT_EXIST.getCode())
                    .info(AuthTypeVO.NOT_EXIST.getInfo())
                    .build();
        }

        // 移除缓存中的 key 值
        authRepository.removeCodeByOpenId(code,openId);

        // 3.校验验证码成功
        return AuthStateEntity.builder()
                .openid(openId)
                .code(AuthTypeVO.SUCCESS.getCode())
                .info(AuthTypeVO.SUCCESS.getInfo())
                .build();
    }


    @Override
    public boolean checkToken(String token) {
        return isVerify(token);
    }

    @Override
    public String getOpenId(String token) {
        Claims claims = decode(token);
        return claims.get("openId").toString();
    }
}
